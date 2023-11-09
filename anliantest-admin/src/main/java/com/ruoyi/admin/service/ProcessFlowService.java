package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.ProcessFlowDto;
import com.ruoyi.admin.entity.ProcessFlowEntity;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author gy
 * @date 2023-06-12 14:08
 */
public interface ProcessFlowService extends IService<ProcessFlowEntity> {

    /**
     * 查询树形结构List
     * @param processFlowDto 查询条件
     * @return List<Object>
     * @throws ServerException
     */
    Object selectWithPage(ProcessFlowDto processFlowDto) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    /**
     * 保存工艺流程并完成
     * @param processFlowDto 新增内容
     * @return Boolean
     */
    Boolean saveAndModify(ProcessFlowDto processFlowDto);

    /**
     * 更新一个或多个工艺流程
     * @param processFlowDto 更新条件
     * @return Boolean
     */
    Boolean updateOneOrMore(ProcessFlowDto processFlowDto);

    /**
     * 删除一个或多个工艺流程
     * @param id 需要删除的id
     * @return Boolean
     */
    Boolean deleteOnOrMore(Long id);

}
