package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.admin.domain.dto.ProjectPlanDto;
import com.ruoyi.admin.entity.ProjectPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-19
 * @desc : 项目排单表
 */
@Mapper
public interface ProjectPlanMapper extends BaseMapper<ProjectPlan> {
    /**
     * 评价主管排单列表
     * @param wrapper
     * @return
     */
    @Select(" SELECT  pr.id projectId, pr.identifier,pr.type,pr.office_address,pr.company,pr.netvalue,pr.`status`, " +
            " pr.charge_id,pr.charge,pp.id,pp.plan_number,pp.plan_status ,pp.plan_date,pp.plan_survey_date,  " +
            " pp.create_time ,pp.report_survey_date,pp.report_survey_user,pp.report_survey_user_id,pp.check_user,pp.check_user_id  FROM  ly_project_plan pp  " +
            " left join  ly_project pr on pr.id=pp.project_id " +
            " ${ew.customSqlSegment}   ")
    List<ProjectPlanDto> planPjList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    /**
     * 详情
     * @param id
     * @return
     */
    @Select(" SELECT pr.id projectId,pr.identifier,pr.type,pr.project_name,pr.company,pr.office_address,\n" +
            " pr.contact,pr.telephone,pr.salesmenid,pr.salesmen,pr.`status`,  pr.total_money, pr.netvalue,\n" +
            " pr.remarks, pr.charge_id, pr.charge,pp.id,pp.plan_number,pp.plan_status ,pp.plan_date,pp.plan_survey_date,  \n" +
            " pp.create_time ,pp.report_survey_date,pp.report_survey_user,pp.report_survey_user_id,pp.check_user,pp.check_user_id from `ly_project_plan` pp\n" +
            " left join ly_project pr   on  pr.id=pp.project_id " +
            "where pp.id=#{id}  ")
    ProjectPlanDto info(Long id);

}
