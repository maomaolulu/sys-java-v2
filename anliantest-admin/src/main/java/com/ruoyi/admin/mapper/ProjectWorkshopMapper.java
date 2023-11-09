package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.domain.dto.ProjectFormWorkshopDto;
import com.ruoyi.admin.entity.ProjectWorkshop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-08
 * @desc : 项目车间岗位
 */
@Mapper
public interface ProjectWorkshopMapper extends BaseMapper<ProjectWorkshop> {
    /**
     * 项目车间岗位点位表格列表
     *
     * @param planId
     * @return
     */
        @Select(" SELECT pw1.`name` building, pw2.`name` workshop ,pw3.`name` post ,po.point ,po.spot_code FROM (\n" +
            "( SELECT `name`,pid,id FROM `ly_project_workshop` pw WHERE pw.type = 0 and plan_id= #{planId } ) pw1\n" +
            "LEFT JOIN ( SELECT `name`,pid,id FROM `ly_project_workshop` pw WHERE pw.type = 1 ) pw2 ON pw2.pid = pw1.id\n" +
            "left join ( SELECT `name`,pid,id FROM `ly_project_workshop` pw WHERE pw.type = 2 ) pw3  on pw3.pid=pw2.id\n" +
            "left join ( SELECT point,spot_code,workshop_id,id FROM `ly_point`                                 ) po  on po.workshop_id=pw3.id\n" +
            ")\n" +
            "ORDER BY pw1.id,pw2.id,pw3.id,po.id ")
    List<ProjectFormWorkshopDto> pointFormList(Long planId);

}
