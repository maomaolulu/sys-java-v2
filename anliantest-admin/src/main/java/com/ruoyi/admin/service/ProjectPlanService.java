package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.ProjectPlanDto;
import com.ruoyi.admin.entity.ProjectPlan;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-19
 * @desc : 项目排单表 */
public interface ProjectPlanService extends IService<ProjectPlan> {
    /**
     * 评价主管排单
     * @param projectPlanDto
     * @return
     */
    List<ProjectPlanDto> planPjList(ProjectPlanDto projectPlanDto);

    /**
     * 调查任务
     * @param projectPlanDto
     * @return
     */
    List<ProjectPlanDto> surveyList(ProjectPlanDto projectPlanDto);

    /**
     * 派单、调查详情
     * @param projectPlanDto
     * @return
     */
    ProjectPlanDto info(ProjectPlanDto projectPlanDto);

    /**
     * 派单。变更
     * @param projectPlanDto
     * @return
     */
    ProjectPlanDto plan(ProjectPlanDto projectPlanDto);

    /**
     * 确认派单
     * @param projectPlanDto
     * @return
     */
    ProjectPlanDto planConfirm(ProjectPlanDto projectPlanDto);

    /**
     * 修改排单信息
     * @param projectPlanDto
     * @return
     */
    ProjectPlanDto planUpdate(ProjectPlanDto projectPlanDto);
}