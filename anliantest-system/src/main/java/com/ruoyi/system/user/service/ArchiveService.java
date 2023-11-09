package com.ruoyi.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.user.entity.ArchiveEntity;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/5/17 18:31
 */
public interface ArchiveService extends IService<ArchiveEntity> {


    /**
     *新增用户档案
     * @param archiveEntity
     * @return
     */
    Boolean addArchive(ArchiveEntity archiveEntity);

    /**
     * 批量新增用户档案
     * @param archiveEntityList
     * @return
     */
    Boolean addFileBatch(List<ArchiveEntity> archiveEntityList);


    /**
     * 根据档案类型，userId获取用户档案列表
     * @param type
     * @param userId
     * @return
     */
    List<ArchiveEntity> getArchiveList(Integer type, Long userId) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    /**
     * 修改用户档案信息
     * @param archiveEntity
     * @return
     */
//    Boolean updateArchive(List<ArchiveEntity> archiveEntityList);
    Boolean updateArchive(ArchiveEntity archiveEntity);
    /**
     * 逻辑删除用户档案信息
     * @param id
     * @return
     */
    Boolean deleteArchive(Long id);


}
