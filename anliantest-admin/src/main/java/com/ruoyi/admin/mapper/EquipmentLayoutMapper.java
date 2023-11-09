package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.domain.dto.EquipmentDto;
import com.ruoyi.admin.entity.EquipmentLayout;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-12
 * @desc : 设备布局
 */
@Mapper
public interface EquipmentLayoutMapper extends BaseMapper<EquipmentLayout> {

    /**
     * 项目车间岗位设备列表
     *
     * @param planId
     * @return
     */
    @Select(" SELECT el.id,el.plan_id,el.project_id,el.workshop_id,el.work_id,el.building_id,el.equipment,el.spot_code,el.number,el.operation_number,el.layout, \n" +
            "el.substance_ids,el.`status`,pw.`name` post ,pw2.`name` workshop,pw3.`name` building \n" +
            "FROM `ly_equipment_layout`  el\n" +
            "left join ly_project_workshop  pw on el.workshop_id=pw.id\n" +
            "left join ly_project_workshop  pw2 on el.work_id=pw2.id\n" +
            "left join ly_project_workshop  pw3 on el.building_id=pw3.id\n" +
            "where el.plan_id=#{planId } " +
            "ORDER BY el.building_id,el.work_id,el.workshop_id   ")
    List<EquipmentDto> equipmentLayoutList(Long planId);
}
