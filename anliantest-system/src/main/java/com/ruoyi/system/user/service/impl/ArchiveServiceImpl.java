package com.ruoyi.system.user.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.AttachmentConstants;
import com.ruoyi.common.constant.BucketNumConstants;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.file.controller.MinioController;
import com.ruoyi.system.file.domain.SysAttachment;
import com.ruoyi.system.file.mapper.SysAttachmentMapper;
import com.ruoyi.system.file.service.SysAttachmentService;
import com.ruoyi.system.login.utils.ShiroUtils;
import com.ruoyi.system.user.entity.ArchiveEntity;
import com.ruoyi.system.user.mapper.ArchiveMapper;
import com.ruoyi.system.user.service.ArchiveService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ZhuYiCheng
 * @date 2023/5/17 18:32
 */
@Service
public class ArchiveServiceImpl extends ServiceImpl<ArchiveMapper, ArchiveEntity> implements ArchiveService {

    @Autowired
    private SysAttachmentService sysAttachmentService;

    /**
     * 新增用户档案
     *
     * @param archiveEntity
     * @return
     */
    @Override
    public Boolean addArchive(ArchiveEntity archiveEntity) {
        Date nowDate = DateUtils.getNowDate();
        String userName = ShiroUtils.getUserName();
        //填充相关人员日期
        archiveEntity.setCreatedBy(userName);
        archiveEntity.setUpdatedBy(userName);
        archiveEntity.setCreatedTime(nowDate);
        archiveEntity.setUpdatedTime(nowDate);
        boolean b = this.save(archiveEntity);
        //将档案id存入对应附件表parentId
        Long archiveId = archiveEntity.getId();
        List<SysAttachment> sysAttachmentList = archiveEntity.getSysAttachmentList();
        if (sysAttachmentList != null && sysAttachmentList.size() > 0) {
            for (SysAttachment sysAttachment : sysAttachmentList) {
                sysAttachment.setPId(archiveId);
                //临时转有效
                sysAttachment.setTempId(AttachmentConstants.FOREVER);
            }
        }
        boolean b1 = sysAttachmentService.updateBatchById(sysAttachmentList);
        return b && b1;
    }

    /**
     * 批量新增用户档案
     *
     * @param archiveEntityList
     * @return
     */
    @Override
    public Boolean addFileBatch(List<ArchiveEntity> archiveEntityList) {
        Date nowDate = DateUtils.getNowDate();
        for (ArchiveEntity archiveEntity : archiveEntityList) {
            //填充相关日期
            archiveEntity.setCreatedTime(nowDate);
            archiveEntity.setUpdatedTime(nowDate);
        }
        boolean b = this.saveBatch(archiveEntityList);

        List<SysAttachment> attachmentList1 = new ArrayList<>();
        for (ArchiveEntity archiveEntity : archiveEntityList) {
            Long archiveId = archiveEntity.getId();
            //获取该档案的附件列表
            List<SysAttachment> sysAttachmentList = archiveEntity.getSysAttachmentList();
            for (SysAttachment sysAttachment : sysAttachmentList) {
                //将档案id存入对应附件表parentId
                sysAttachment.setPId(archiveId);
                //临时转有效
                sysAttachment.setTempId(AttachmentConstants.FOREVER);
                attachmentList1.add(sysAttachment);
            }
        }
        //将加过parentId，转有效的附件实体更改到附加表
        boolean b1 = sysAttachmentService.updateBatchById(attachmentList1);
        return b && b1;
    }


    /**
     * 根据档案类型，userId获取用户档案列表
     *
     * @param type
     * @param userId
     * @return
     */
    @Override
    public List<ArchiveEntity> getArchiveList(Integer type, Long userId) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //获取档案列表
        List<ArchiveEntity> archiveEntityList = this.list(new QueryWrapper<ArchiveEntity>()
                .eq(userId != null, "user_id", userId)
                .eq(type != null, "type", type)
                .eq("del_flag", DeleteFlag.NO.ordinal()));

        if (archiveEntityList != null && archiveEntityList.size() > 0) {
            //获取各档案的附件列表
            List<Long> archiveIdList = new ArrayList<Long>();
            for (ArchiveEntity archiveEntity : archiveEntityList) {
                archiveIdList.add(archiveEntity.getId());
            }
            //根据档案类型获取桶名
            String bucketName = getBucketName(type);
            //根据桶名和档案id获取附件列表
            List<SysAttachment> sysAttachmentList = sysAttachmentService.list(new QueryWrapper<SysAttachment>()
                    .in("p_id", archiveIdList)
                    .eq(StringUtils.isNotBlank(bucketName), "bucket_name", bucketName)
                    .eq("temp_id", AttachmentConstants.FOREVER)
                    .eq("del_flag", DeleteFlag.NO.ordinal()));
            //给每一个文件拼preUrl
            for (SysAttachment sysAttachment : sysAttachmentList) {
                String fileUrl = getFileUrl(sysAttachment.getBucketName(), sysAttachment.getPath());
                String preUrl = fileUrl.substring(fileUrl.lastIndexOf("/ly-"));
                sysAttachment.setPreUrl(preUrl);
            }
            Map<Long,List<SysAttachment> > sysAttachmentListByGroup = sysAttachmentList.stream()
                    .collect(Collectors.groupingBy(SysAttachment::getPId));
            //根据档案id=附件p_id，为档案分配附件
            for (ArchiveEntity archiveEntity : archiveEntityList) {
                archiveEntity.setSysAttachmentList(sysAttachmentListByGroup.get(archiveEntity.getId()));
            }
        }

        return archiveEntityList;
    }

    @Resource
    private MinioClient minioClient;

    private String getFileUrl(String bucketName, String path) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(path).method(Method.GET).expiry(60, TimeUnit.MINUTES).build());
        return url;
    }

    /**
     * 根据档案类型获取桶名
     *
     * @param type
     * @return
     */
    public String getBucketName(Integer type) {
        if (type == 1) {
            return BucketNumConstants.TYPE_1;
        } else {
            return BucketNumConstants.TYPE_2;
        }
    }

    /**
     * 修改用户档案信息
     *
     * @param archiveEntity
     * @return
     */
    @Override
    public Boolean updateArchive(ArchiveEntity archiveEntity) {
        Date nowDate = DateUtils.getNowDate();
        String userName = ShiroUtils.getUserName();

        Long archiveId = archiveEntity.getId();
        //删除原来的附件，及minio文件
        //把原来的档案附件改为临时的
        boolean b = sysAttachmentService.update(new UpdateWrapper<SysAttachment>()
                .eq(archiveId != null, "p_id", archiveId)
                .eq("del_flag", DeleteFlag.NO.ordinal())
                .set("temp_id", AttachmentConstants.TEMPORARY));

        //将新传过来的附件存入parentId
        List<SysAttachment> sysAttachmentList = archiveEntity.getSysAttachmentList();
        if (sysAttachmentList != null && sysAttachmentList.size() > 0) {
            for (SysAttachment sysAttachment : sysAttachmentList) {
                sysAttachment.setPId(archiveId);
                //临时转有效
                sysAttachment.setTempId(AttachmentConstants.FOREVER);
                sysAttachment.setUpdatedBy(userName);
                sysAttachment.setUpdatedTime(nowDate);
            }
        }
        boolean b1 = sysAttachmentService.updateBatchById(sysAttachmentList);

        //如果档案有效期从定期改为长期有效的，把原来的时间删掉
        if (archiveEntity.getIsForever() != null && archiveEntity.getIsForever() == 1) {
            archiveEntity.setExpirationDate("");
        }
        //填充相关人员日期
        archiveEntity.setUpdatedBy(userName);
        archiveEntity.setUpdatedTime(nowDate);
        boolean b2 = this.updateById(archiveEntity);

        return b && b1 && b2;
    }

    /**
     * 逻辑删除用户档案信息
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteArchive(@RequestParam("id") Long id) {
        //根据id逻辑删除对应附件表
        boolean b = sysAttachmentService.update(new UpdateWrapper<SysAttachment>()
                .eq(id != null, "p_id", id)
                .set("del_flag", DeleteFlag.YES.ordinal()));
        //逻辑删除档案信息
        ArchiveEntity archiveEntity = new ArchiveEntity();
        archiveEntity.setId(id);
        archiveEntity.setDelFlag(DeleteFlag.YES.ordinal());
        boolean b1 = this.updateById(archiveEntity);
        return b && b1;
    }

    //    /**
//     * 修改用户档案信息
//     *
//     * @param archiveEntityList
//     * @return
//     */
//    @Override
//    public Boolean updateArchive(List<ArchiveEntity> archiveEntityList) {
//
//        //删除原来的档案信息
//        ArchiveEntity archiveEntity = archiveEntityList.get(0);
//        Long userId = archiveEntity.getUserId();
//        boolean b = this.remove(new QueryWrapper<ArchiveEntity>().eq("user_id", userId));
//        //加入新的档案信息
//        Date nowDate = DateUtils.getNowDate();
//        for (ArchiveEntity archiveEntity1 : archiveEntityList) {
//            //填充相关日期
////            archiveEntity.setCollectionDate(nowDate);
//            archiveEntity1.setCreatedTime(nowDate);
//            archiveEntity1.setUpdatedTime(nowDate);
//        }
//
//        //批量存之后会直接赋值id
//
//        boolean b1 = this.saveBatch(archiveEntityList);
//        //删除原来的附件
//        List<Long> idList = new ArrayList<>();
//        for (ArchiveEntity archiveEntity2 : archiveEntityList) {
//            idList.add(archiveEntity2.getId());
//        }
//        List<SysAttachment> sysAttachmentList = sysAttachmentService.list(new QueryWrapper<SysAttachment>().in(idList != null, "p_id", idList)
//                .eq("temp_id", AttachmentConstants.FOREVER)
//                .eq("del_flag", DeleteFlag.NO.ordinal()));
//        List<String> pathList = new ArrayList<>();
//        for (SysAttachment sysAttachment : sysAttachmentList) {
//            String path = sysAttachment.getPath();
//            pathList.add(path);
//        }
//        String bucketName = getBucketName(archiveEntityList.get(0).getType());
//        List<DeleteError> deleteErrors = sysAttachmentService.deleteBatch(pathList, bucketName);
//        if (deleteErrors.size() != 0) {
//            return false;
//        }
//
//        //加入新的附件
//        List<SysAttachment> sysAttachmentList1 = new ArrayList<>();
//        for (ArchiveEntity archiveEntity2 : archiveEntityList) {
//            Long archiveId = archiveEntity2.getId();
//            //获取该档案的附件列表
//            List<SysAttachment> sysAttachmentList2 = archiveEntity2.getSysAttachmentList();
//            for (SysAttachment sysAttachment : sysAttachmentList2) {
//                //将档案id存入对应附件表parentId
//                sysAttachment.setPId(archiveId);
//                //临时转有效
//                sysAttachment.setTempId(AttachmentConstants.FOREVER);
//                sysAttachmentList1.add(sysAttachment);
//            }
//        }
//        //将加过parentId，转有效的附件实体更改到附加表
//        boolean b2 = sysAttachmentService.updateBatchById(sysAttachmentList1);
//
//        return b && b1 && b2;
//    }

}
