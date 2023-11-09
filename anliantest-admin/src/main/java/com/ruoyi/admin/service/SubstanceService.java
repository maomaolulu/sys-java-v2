package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.vo.SubstanceVo;
import com.ruoyi.admin.entity.SubstanceEntity;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/24 10:52
 */
public interface SubstanceService extends IService<SubstanceEntity> {


    /**
     * 获取检测项目列表
     */
    List<SubstanceVo> getSubstanceList();

    /**
     * 获取数据用于清洗
     */
    List<SubstanceEntity> getAllAndList();
}
