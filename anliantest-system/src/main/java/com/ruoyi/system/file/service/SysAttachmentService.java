package com.ruoyi.system.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.file.domain.Res;
import com.ruoyi.system.file.domain.SysAttachment;
import io.minio.messages.DeleteError;

import java.util.List;

/**
 * @author zx
 * @date 2021/12/19 10:07
 */
public interface SysAttachmentService extends IService<SysAttachment> {
    /**
     * 保存附件信息
     * @param attachment
     * @return
     */
    int addAttachment(SysAttachment attachment);

    /**
     * 临时文件转有效文件
     * @return
     */
    int transform();

    /**
     * 获取附件列表
     * @param bucketName
     * @return
     */
    List<SysAttachment> getList(String bucketName);

    /**
     * 获取附件列表
     * @param types
     * @param tempId
     * @return
     */
/*    List<SysAttachment> getListByTempId(String types, String tempId);*/

    /**
     * 删除附件
     * @param bucketName
     * @param path
     */
    void delete(String bucketName, String path);

    /**
     * 获取临时文件
     * @return
     */
    List<SysAttachment> getTempList();

    /**
     * 批量删除文件
     * @param pathList
     * @param bucketName
     * @return
     */
    List<DeleteError> deleteBatch(List<String> pathList, String bucketName);

    /**
     * 上传文件
     * @param sysAttachment
     * @return
     */
    Res upload(SysAttachment sysAttachment);
}
