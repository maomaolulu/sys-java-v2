package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.entity.SubstanceStateLimitEntity;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
public interface SubstanceStateLimitService extends IService<SubstanceStateLimitEntity> {

    /**
     * 新增危害因素限值
     * @param stateLimit
     * @return
     */
    Boolean addLimit(SubstanceStateLimitEntity stateLimit);


    /**
     * 获取危害因素限值
     * @param substanceInfoId
     * @return
     */
    List<SubstanceStateLimitEntity> getListBySubstanceInfoId(Long substanceInfoId);


    /**
     * 修改危害因素限值
     * @param stateLimitEntity
     * @return
     */
    Boolean updateLimit(SubstanceStateLimitEntity stateLimitEntity);

    /**
     * 逻辑删除危害因素限值
     * @param id
     * @return
     */
    Boolean deleteLimit(Long id);
}
