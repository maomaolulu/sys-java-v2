package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.EquipmentDto;
import com.ruoyi.admin.entity.EquipmentLayout;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-12
 * @desc : 设备布局
 */
public interface EquipmentLayoutService extends IService<EquipmentLayout> {

    /**
     * 项目车间岗位设备列表
     *
     * @param planId
     * @return
     */
    List<EquipmentDto> equipmentLayoutList(Long planId);
}