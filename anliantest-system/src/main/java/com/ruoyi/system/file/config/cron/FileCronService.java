package com.ruoyi.system.file.config.cron;

import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.system.file.domain.SysAttachment;
import com.ruoyi.system.file.service.SysAttachmentService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * 文件定时任务
 * @author zx
 * @date 2022-5-18 22:09:48
 */
@Slf4j
@Component
public class FileCronService {

    /**
     * 每个月最后一天的23:59:59执行 定时清理临时文件
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "59 59 23 L * ?")
    public void deleteTempFile(){
        log.info("开始执行删除临时文件");
        SysAttachmentService attachmentService = SpringUtil.getBean(SysAttachmentService.class);
        MinioClient minioClient = SpringUtil.getBean(MinioClient.class);
        try {
            List<SysAttachment> tempList = attachmentService.getTempList();
            for (SysAttachment attachment : tempList) {
                log.info("删除临时文件："+attachment.getBucketName()+"---"+attachment.getPath()+"---"+attachment.getTempId());
                String bucketName = attachment.getBucketName();
                String path = attachment.getPath();
                attachmentService.delete(bucketName, path);
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

}