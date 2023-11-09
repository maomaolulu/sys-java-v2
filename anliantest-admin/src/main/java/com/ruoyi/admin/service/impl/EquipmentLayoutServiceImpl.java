package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.EquipmentDto;
import com.ruoyi.admin.entity.EquipmentLayout;
import com.ruoyi.admin.mapper.EquipmentLayoutMapper;
import com.ruoyi.admin.service.EquipmentLayoutService;
import com.ruoyi.admin.service.ProjectWorkshopService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-12
 * @desc : 设备布局
 */
@Service
public class EquipmentLayoutServiceImpl extends ServiceImpl<EquipmentLayoutMapper, EquipmentLayout> implements EquipmentLayoutService {
    private final ProjectWorkshopService projectWorkshopService;

    public EquipmentLayoutServiceImpl(ProjectWorkshopService projectWorkshopService) {
        this.projectWorkshopService = projectWorkshopService;
    }

    /**
     * 查询设备列表
     *
     * @param planId
     * @return
     */
    @Override
    public List<EquipmentDto> equipmentLayoutList(Long planId) {
        List<EquipmentDto> equipmentDtos = baseMapper.equipmentLayoutList(planId);


        return equipmentDtos;
    }

}