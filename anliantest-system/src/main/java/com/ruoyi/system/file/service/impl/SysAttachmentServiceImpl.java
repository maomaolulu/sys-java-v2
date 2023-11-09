package com.ruoyi.system.file.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.AttachmentConstants;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.file.domain.Res;
import com.ruoyi.system.file.domain.SysAttachment;
import com.ruoyi.system.file.mapper.SysAttachmentMapper;
import com.ruoyi.system.file.service.SysAttachmentService;
import com.ruoyi.system.login.utils.ShiroUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zx
 * @date 2021/12/19 10:07
 */
@Service
@Slf4j
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentService {
    @Autowired
    public RedisTemplate redisTemplate;
    @Resource
    private MinioClient minioClient;

    private final SysAttachmentMapper attachmentMapper;

    @Autowired
    public SysAttachmentServiceImpl(SysAttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    /**
     * 保存附件信息
     *
     * @param attachment
     * @return
     */
    @Override
    public int addAttachment(SysAttachment attachment) {
        String userName = ShiroUtils.getUserName();
        attachment.setCreatedBy(userName);
        attachment.setUpdatedBy(userName);
        attachment.setCreatedTime(new Date());
        attachment.setUpdatedTime(new Date());
        return attachmentMapper.insert(attachment);
    }

    /**
     * 临时文件转有效文件
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transform() {
        try {
            String userName = ShiroUtils.getUserName();
            QueryWrapper<SysAttachment> wrapper = new QueryWrapper<>();
            wrapper.eq("temp_id", AttachmentConstants.TEMPORARY);

            List<SysAttachment> sysAttachments = attachmentMapper.selectList(wrapper);
            if (sysAttachments.isEmpty()) {
                return 1;
            }
            sysAttachments.stream().forEach(attachment -> {
                attachment.setUpdatedBy(userName);
                attachment.setUpdatedTime(new Date());
                attachment.setTempId(AttachmentConstants.FOREVER);
                attachmentMapper.updateById(attachment);
            });

            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }

    }


    /**
     * 获取附件列表
     *
     * @param bucketName
     * @return
     */
    @Override
    public List<SysAttachment> getList(String bucketName) {
        QueryWrapper<SysAttachment> wrapper = new QueryWrapper<>();
        wrapper.eq("bucket_name", bucketName)
                .eq("temp_id", AttachmentConstants.FOREVER)
                .eq("del_flag", DeleteFlag.NO.ordinal());
        return attachmentMapper.selectList(wrapper);
    }

    /**
     * 获取附件列表
     *
     * @param types
     * @param tempId
     * @return
     */
/*    @Override
    public List<SysAttachment> getListByTempId(String types, String tempId) {
        QueryWrapper<SysAttachment> wrapper = new QueryWrapper<>();
        wrapper.eq("types", types);
        wrapper.eq("temp_id", tempId);
        wrapper.eq("del_flag", "0");
        List<SysAttachment> sysAttachments = attachmentMapper.selectList(wrapper);
        return sysAttachments;
    }*/

    /**
     * 删除附件
     *
     * @param bucketName
     * @param path
     */
    @Override
    public void delete(String bucketName, String path) {
        QueryWrapper<SysAttachment> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(bucketName), "bucket_name", bucketName);
        wrapper.eq(StrUtil.isNotBlank(path), "path", path);
        attachmentMapper.delete(wrapper);
    }

    /**
     * 获取临时文件
     *
     * @return
     */
    @Override
    public List<SysAttachment> getTempList() {

        return attachmentMapper.selectList(new QueryWrapper<SysAttachment>()
                .eq("temp_id", AttachmentConstants.TEMPORARY));
    }


    /**
     * 批量删除文件
     *
     * @param pathList
     * @param bucketName
     * @return
     */
    @Override
    public List<DeleteError> deleteBatch(List<String> pathList, String bucketName) {
        List<DeleteError> deleteErrors = new ArrayList<>();
        List<DeleteObject> deleteObjects = pathList.stream()
                .map(value -> new DeleteObject(value)).collect(Collectors.toList());
        this.remove(new QueryWrapper<SysAttachment>()
                .eq(StringUtils.isNotBlank(bucketName), "bucketName", bucketName)
                .in("path", pathList));
        Iterable<Result<DeleteError>> results =
                minioClient.removeObjects(
                        RemoveObjectsArgs
                                .builder()
                                .bucket(bucketName)
                                .objects(deleteObjects)
                                .build());
        try {
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrors.add(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleteErrors;
    }


    /**
     * 预览url
     *
     * @param bucketName
     * @param path
     * @return
     */
    @SneakyThrows(Exception.class)
    private String getFileUrl(String bucketName, String path) {
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(path).method(Method.GET).expiry(60, TimeUnit.MINUTES).build());

        return url;
    }


    /**
     * 上传文件
     *
     * @param sysAttachment
     * @return
     */
    @Override
    public Res upload(SysAttachment sysAttachment) {
        Res res = new Res();
        res.setCode(500);

        MultipartFile[] file = sysAttachment.getFile();
        String bucketName = sysAttachment.getBucketName();

        if (file == null || file.length == 0) {
            res.setMessage("上传文件不能为空");
            return res;
        }

        List<Map<String, Object>> originFileNameList = new ArrayList<>(file.length);

        try {
            for (MultipartFile multipartFile : file) {
                Date now = DateUtils.getNowDate();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
                String timestamp = format.format(now);
                String timestamp1 = String.valueOf(date.getTime());
                String originFileName = multipartFile.getOriginalFilename();
                String type = originFileName.substring(originFileName.lastIndexOf("."));
                String path = timestamp.concat("/" + timestamp1 + originFileName);
                InputStream in = multipartFile.getInputStream();
                PutObjectArgs args = PutObjectArgs.builder()
                        //路径
                        .bucket(bucketName)
                        //文件名
                        .object(path)
                        //流
                        .stream(in, multipartFile.getSize(), -1)
                        //后缀
                        .contentType(multipartFile.getContentType())
                        .build();
                // 没有bucket则创建
                boolean exist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!exist) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }
                minioClient.putObject(args);
                in.close();
                sysAttachment.setFileName(originFileName);
                sysAttachment.setPath(path);
                sysAttachment.setFileType(type);
                sysAttachment.setTempId(AttachmentConstants.TEMPORARY);
                sysAttachment.setDelFlag(DeleteFlag.NO.ordinal());
                //保存附件信息
                this.addAttachment(sysAttachment);

                Map<String, Object> map = new HashMap<>(3);
                String fileUrl = getFileUrl(bucketName, path);
                String preUrl = fileUrl.substring(fileUrl.lastIndexOf("/ly-"));
                map.put("preUrl", preUrl);
                map.put("path", path);
                map.put("fileName", originFileName);
                map.put("fileType", type);
                map.put("id", sysAttachment.getId());
                originFileNameList.add(map);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            res.setMessage("上传失败");
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return res;
        }

        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("url", originFileNameList);
        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(data);

        return res;
    }
}
