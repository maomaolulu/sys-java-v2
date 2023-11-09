package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.ImageStatueDto;
import com.ruoyi.admin.domain.dto.ProcessFlowDto;
import com.ruoyi.admin.domain.vo.OneProcessFlowVo;
import com.ruoyi.admin.domain.vo.ProcessFlowVo;
import com.ruoyi.admin.entity.ProcessCanvasEntity;
import com.ruoyi.admin.entity.ProcessFlowEntity;
import com.ruoyi.admin.mapper.ProcessFlowMapper;
import com.ruoyi.admin.service.ProcessCanvasService;
import com.ruoyi.admin.service.ProcessFlowService;
import com.ruoyi.common.enums.FlowType;
import com.ruoyi.common.utils.ObjectUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.file.domain.SysAttachment;
import com.ruoyi.system.file.service.SysAttachmentService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author gy
 * @date 2023-06-12 14:10
 */
@Service
public class ProcessFlowServiceImpl extends ServiceImpl<ProcessFlowMapper, ProcessFlowEntity> implements ProcessFlowService {

    @Resource
    private MinioClient minioClient;

    @Resource
    private ProcessCanvasService processCanvasService;

    @Resource
    private SysAttachmentService sysAttachmentService;
    private final String patternString = "src=\"(.*?)\"";
    private final int first = 1;

    @Override
    public Object selectWithPage(ProcessFlowDto processFlowDto) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (processFlowDto.getId()!=null){
            List<ImageStatueDto> images = new ArrayList<>();
            ProcessFlowEntity entity = this.getById(processFlowDto.getId());
            OneProcessFlowVo oneProcessFlowVo = ObjectUtils.transformObj(entity, OneProcessFlowVo.class);
            String content = oneProcessFlowVo.getContent();
            Matcher m = Pattern.compile(patternString).matcher(content);
            while(m.find()){
                //填充imageList信息
                ImageStatueDto attachment = new ImageStatueDto();
                String path = m.group(1);
                attachment.setPath(path);
                SysAttachment sysAttachment = sysAttachmentService.getOne(new QueryWrapper<SysAttachment>().eq("path", path));
                if (sysAttachment!=null) {
                    attachment.setId(sysAttachment.getId());
                }
                String bucketName = "ly-process-img";
                String fileUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(path).method(Method.GET).expiry(60, TimeUnit.MINUTES).build());
                String preUrl = "/proxyAnlianShlySysJavaImg" + fileUrl.substring(fileUrl.lastIndexOf("/ly-"));
                attachment.setPreUrl(preUrl);
                images.add(attachment);
                //替换预览路径
                content = content.replace(path,preUrl);
            }
            oneProcessFlowVo.setImageList(images);
            oneProcessFlowVo.setContent(content);
            return oneProcessFlowVo;
        }
        QueryWrapper<ProcessFlowEntity> qw = new QueryWrapper<ProcessFlowEntity>()
                .eq(processFlowDto.getPlanId()!=null,"plan_id",processFlowDto.getPlanId())
                .eq(processFlowDto.getProjectId()!=null,"project_id",processFlowDto.getProjectId())
                .like(StringUtils.isNotBlank(processFlowDto.getTitle()),"title",processFlowDto.getTitle())
                .like(StringUtils.isNotBlank(processFlowDto.getConclusion()),"conclusion",processFlowDto.getConclusion());
        List<ProcessFlowEntity> list = this.list(qw);
        if (list.size() == 0) { return null; }
        List<ProcessFlowVo> voList = ObjectUtils.transformArrObj(list, ProcessFlowVo.class);
        List<ProcessFlowVo> fathers = voList.stream().filter(pf -> pf.getFlowType() == FlowType.YES.ordinal()).collect(Collectors.toList());
        Map<String, List<ProcessFlowVo>> sons = voList.stream().filter(pf -> pf.getFlowType() == FlowType.NO.ordinal()).collect(Collectors.groupingBy(ProcessFlowVo::getTitle));
        for (ProcessFlowVo father:fathers){
            if (sons.containsKey(father.getTitle())){
                father.setSons(sons.get(father.getTitle()));
            }
        }
        return fathers;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveAndModify(ProcessFlowDto processFlowDto){
        ProcessFlowEntity processFlowEntity = ObjectUtils.transformObj(processFlowDto, ProcessFlowEntity.class);
        // 新增一般都是不带content 不排除带的情况
        if (processFlowDto.getContent()!=null){
            List<SysAttachment> updateList = new ArrayList<>();
            replaceContentAndAddAttachment(processFlowDto, processFlowEntity, updateList);
            return sysAttachmentService.updateBatchById(updateList) && this.save(processFlowEntity);
        }else {
            return this.save(processFlowEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateOneOrMore(ProcessFlowDto processFlowDto){
        ProcessFlowEntity lastOne = this.getById(processFlowDto.getId());
        if (processFlowDto.getConclusion()!=null && lastOne.getFlowType() == FlowType.YES.ordinal() && !lastOne.getTitle().equals(processFlowDto.getTitle())) {
            // 主工艺名称有变动,则还要更改所有其子工艺名称
            List<ProcessFlowEntity> list = this.list(new QueryWrapper<ProcessFlowEntity>().eq("flow_type",FlowType.NO.ordinal()).eq("plan_id", lastOne.getPlanId()).eq("title", lastOne.getTitle()));
            list.forEach(pe -> pe.setTitle(processFlowDto.getTitle()));
            lastOne.setConclusion(processFlowDto.getConclusion()).setTitle(processFlowDto.getConclusion());
            list.add(lastOne);
            return this.updateBatchById(list);
        }else {
            ProcessFlowEntity processFlowEntity = ObjectUtils.transformObj(processFlowDto, ProcessFlowEntity.class);
            if (processFlowDto.getContent() != null && StringUtils.isNotBlank(processFlowDto.getContent())){
                // 如果改的content 则需要遍历处理文件
                List<SysAttachment> updateList = new ArrayList<>();
                replaceContentAndAddAttachment(processFlowDto, processFlowEntity, updateList);
                return sysAttachmentService.updateBatchById(updateList) && this.updateById(processFlowEntity);
            }else {
                //更改conclusion这种普通树形直接update
                return this.updateById(processFlowEntity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteOnOrMore(Long id){
        //同步删除所有对应的流程画布 把上传的文件标为临时
        ProcessFlowEntity lastOne = this.getById(id);
        if (lastOne == null) {return false;}
        List<SysAttachment> deleteList = new ArrayList<>();
        if (lastOne.getFlowType() == FlowType.YES.ordinal()){
            List<ProcessFlowEntity> list = this.list(new QueryWrapper<ProcessFlowEntity>().eq("plan_id", lastOne.getPlanId()).eq("title", lastOne.getTitle()));
            List<Long> ids = list.stream().map(ProcessFlowEntity::getId).collect(Collectors.toList());
            List<ProcessCanvasEntity> canvases = processCanvasService.list(new QueryWrapper<ProcessCanvasEntity>().in("flow_id",ids));
            List<String> contents = canvases.stream().map(ProcessCanvasEntity::getContent).collect(Collectors.toList());
            for (String content:contents){
                addDeleteAttachment(deleteList,content);
            }
            return this.removeByIds(ids) && processCanvasService.remove(new QueryWrapper<ProcessCanvasEntity>().in("flow_id",ids)) && sysAttachmentService.updateBatchById(deleteList);
        }else {
            ProcessCanvasEntity canvases = processCanvasService.getOne(new QueryWrapper<ProcessCanvasEntity>().eq("flow_id",lastOne.getId()));
            String content = canvases.getContent();
            addDeleteAttachment(deleteList,content);
            return processCanvasService.remove(new QueryWrapper<ProcessCanvasEntity>().eq("flow_id",id)) && this.removeById(id) && sysAttachmentService.updateBatchById(deleteList);
        }
    }

    private void replaceContentAndAddAttachment(ProcessFlowDto processFlowDto,final ProcessFlowEntity processFlowEntity, final List<SysAttachment> updateList){
        List<ImageStatueDto> imageList = processFlowDto.getImageList();
        String content = processFlowEntity.getContent();
        for (ImageStatueDto imageStatue:imageList){
            SysAttachment attachment = new SysAttachment();
            String preUrl = "/proxyAnlianShlySysJavaImg" + imageStatue.getPreUrl();
            preUrl = preUrl.replace("&","&amp;");
            //将预览路径替换真实路径
            if (content.contains(preUrl)){
                content = content.replace(preUrl,imageStatue.getPath());
                int zero = 0;
                attachment.setTempId(zero);
            }else {
                attachment.setTempId(first);
            }
            attachment.setId(imageStatue.getId());
            updateList.add(attachment);
        }
        processFlowEntity.setContent(content);
    }

    private void addDeleteAttachment(final List<SysAttachment> images,String content){
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(content);
        while(m.find()){
            SysAttachment sysAttachment = sysAttachmentService.getOne(new QueryWrapper<SysAttachment>().eq("path", m.group(first)));
            sysAttachment.setTempId(first);
            images.add(sysAttachment);
        }
    }
}
