package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.vo.SubstanceInfoVo;
import com.ruoyi.admin.entity.SubstanceInfoEntity;

import java.util.List;

public interface SubstanceInfoService extends IService<SubstanceInfoEntity> {

    /**
     * 新增危害因素
     * @param substanceInfo
     * @return
     */
    Boolean addSubstanceInfo(SubstanceInfoEntity substanceInfo);

    /**
     * 获取危害因素列表
     * @param substanceInfoVo
     * @return
     */
    List<SubstanceInfoEntity> getSubstanceInfoList(SubstanceInfoVo substanceInfoVo);

    /**
     * 获取危害因素详情
     * @param id
     * @return
     */
    SubstanceInfoEntity getSubstanceInfo(Long id);

    /**
     * 修改危害因素信息
     * @param substanceInfo
     * @return
     */
    Boolean updateSubstanceInfo(SubstanceInfoEntity substanceInfo);

    /**
     * 逻辑删除危害因素信息
     * @param id
     * @return
     */
    Boolean deleteSubstanceInfo(Long id);


}
