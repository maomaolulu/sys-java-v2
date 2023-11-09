package com.ruoyi.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.ProjectPlanDto;
import com.ruoyi.admin.entity.*;
import com.ruoyi.admin.mapper.ProjectPlanMapper;
import com.ruoyi.admin.service.ProjectPlanService;
import com.ruoyi.admin.service.ProjectRecordService;
import com.ruoyi.admin.service.ProjectService;
import com.ruoyi.admin.service.ProjectUserService;
import com.ruoyi.system.login.utils.ShiroUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-19
 * @desc : 项目排单表
 */
@Service
public class ProjectPlanServiceImpl extends ServiceImpl<ProjectPlanMapper, ProjectPlan> implements ProjectPlanService {

    private final ProjectService projectService;
    private final ProjectRecordService projectRecordService;
    private final ProjectUserService projectUserService;

    public ProjectPlanServiceImpl(ProjectService projectService, ProjectRecordService projectRecordService, ProjectUserService projectUserService) {
        this.projectService = projectService;
        this.projectRecordService = projectRecordService;
        this.projectUserService = projectUserService;
    }

    /**
     * 评价主管排单
     *
     * @param projectPlanDto
     * @return
     */
    @Override
    public List<ProjectPlanDto> planPjList(ProjectPlanDto projectPlanDto) {
        List<ProjectPlanDto> projectPlanDtos = baseMapper.planPjList(new QueryWrapper<Object>()
                //状态在下发（即启动）之后 终止之前
                // 项目状态(0:未启动，1：已启动，2：已完成，3：已结束 4：终止 5：挂起/暂停)
                .between("pr.status", 1, 4)
                //只查询评价的单子
                .in("pr.type", "职业病危害预评价", "职业病防护设施设计专篇", "职业病危害控制效果评价", "职业病危害现状评价")
                //排单状态搜索 排单状态（ 0：未派单 1：待确认 2.已确认    ）
                .eq(projectPlanDto.getPlanStatus() != null, "pp.plan_status", projectPlanDto.getPlanStatus())
                //项目编号
                .like(StrUtil.isNotBlank(projectPlanDto.getIdentifier()), "pr.identifier", projectPlanDto.getIdentifier())
                //企业名称
                .like(StrUtil.isNotBlank(projectPlanDto.getCompany()), "pr.company", projectPlanDto.getCompany())
                //调查人 负责人
                .like(StrUtil.isNotBlank(projectPlanDto.getCharge()), "pr.charge", projectPlanDto.getCharge())
        );

        return projectPlanDtos;
    }

    /**
     * 调查任务
     *
     * @param projectPlanDto
     * @return
     */
    @Override
    public List<ProjectPlanDto> surveyList(ProjectPlanDto projectPlanDto) {
        Long userId = ShiroUtils.getUserId();
        QueryWrapper<Object> wrapper = new QueryWrapper<Object>()
                //状态在下发（即启动）之后 终止之前
                // 项目状态(0:未启动，1：已启动，2：已完成，3：已结束 4：终止 5：挂起/暂停)
                .between("pr.status", 1, 4)
                //排单状态搜索 排单状态（ 0：未派单 1：待确认 2.已确认    ）
                .in("pp.plan_status", 1, 2)
                //排单状态搜索 排单状态（ 0：未派单 1：待确认 2.已确认    ）
                .eq(projectPlanDto.getPlanStatus() != null, "pp.plan_status", projectPlanDto.getPlanStatus())
                //项目编号
                .like(StrUtil.isNotBlank(projectPlanDto.getIdentifier()), "pr.identifier", projectPlanDto.getIdentifier())
                //企业名称
                .like(StrUtil.isNotBlank(projectPlanDto.getCompany()), "pr.company", projectPlanDto.getCompany())
                //当前人自己的任务
                .eq(!ShiroUtils.isAdmin(), "pr.charge_id", userId);
        List<ProjectPlanDto> projectPlanDtos = baseMapper.planPjList(wrapper);
        return projectPlanDtos;
    }

    /**
     * 派单、调查详情
     *
     * @param projectPlanDto
     * @return
     */
    @Override
    public ProjectPlanDto info(ProjectPlanDto projectPlanDto) {

        ProjectPlanDto info = baseMapper.info(projectPlanDto.getId());


        //派单记录
        List<ProjectRecord> list1 = projectRecordService.list(
                new LambdaQueryWrapper<ProjectRecord>()
                        .eq(ProjectRecord::getProjectId, projectPlanDto.getProjectId())
                        .eq(ProjectRecord::getType, 1)
                        .orderByDesc(ProjectRecord::getCreateTime)
        );
        if (CollUtil.isNotEmpty(list1)) {


            info.setPlanRecordList(list1);
        } else {
            info.setPlanRecordList(new ArrayList<>());
        }
        return info;
    }


    /**
     * 派单   变更
     *
     * @param projectPlanDto
     * @return
     */
    @Override
    public ProjectPlanDto plan(ProjectPlanDto projectPlanDto) {
        //项目id
        Long projectId = projectPlanDto.getProjectId();
        //项目排单批次
        Integer planNumber = projectPlanDto.getPlanNumber();
        if (planNumber == 1) {//第一次派单可以选择负责人 和 更改负责人

            //项目信息
            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setId(projectId);
            projectEntity.setChargeId(projectPlanDto.getChargeId());
            projectEntity.setCharge(projectPlanDto.getCharge());
            projectService.updateById(projectEntity);
        }
        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setId(projectPlanDto.getId());
        //改为待确认
        projectPlan.setPlanStatus(1);
        //初始排单日期
        projectPlan.setPlanDate(new Date());
        this.updateById(projectPlan);

        //记录
        ProjectRecord projectRecord = new ProjectRecord();
        projectRecord.setProjectId(projectId);
        //派单时间(创建时间)
        //派单人信息
        projectRecord.setCreateById(ShiroUtils.getUserId());
        projectRecord.setCreateBy(ShiroUtils.getUserName());
        //调查人
        projectRecord.setChargeId(projectPlanDto.getChargeId());
        projectRecord.setCharge(projectPlanDto.getCharge());
        //0：下发 1：调查排单
        projectRecord.setType(1);
        //0：下发 1：派单 2：确认 3：修改
        projectRecord.setSubType(1);

        if (planNumber == 1) {
            projectRecord.setTitle("排单");
        } else {
            projectRecord.setTitle("(" + planNumber + ")排单");
        }
        //记录详情
        projectRecord.setPlanDetails("任务派发给 " + projectPlanDto.getCharge());
        projectRecordService.save(projectRecord);
        return projectPlanDto;
    }

    /**
     * 确认派单
     *
     * @param projectPlanDto
     * @return
     */
    @Override
    @Transactional
    public ProjectPlanDto planConfirm(ProjectPlanDto projectPlanDto) {


        //项目id
        Long projectId = projectPlanDto.getProjectId();
        //排单id
        Long id = projectPlanDto.getId();
        //排单批次
        Integer planNumber = projectPlanDto.getPlanNumber();


        //修改排单内信息
        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setId(id)
                //调查日期
                .setPlanSurveyDate(projectPlanDto.getPlanSurveyDate())
                //状态改为确认
                .setPlanStatus(2)
                //报告调查日期
                .setReportSurveyDate(projectPlanDto.getReportSurveyDate());

        //报告调查人
        String reportSurveyUser = projectPlanDto.getReportSurveyUser();
        Long reportSurveyUserId = projectPlanDto.getReportSurveyUserId();
        //调查复核人
        String checkUser = projectPlanDto.getCheckUser();
        Long checkUserId = projectPlanDto.getCheckUserId();
        //删除 当前派单项目人员表报告调查人和调查复核人
        projectUserService.remove(new LambdaQueryWrapper<ProjectUser>().eq(ProjectUser::getProjectId, projectId).eq(ProjectUser::getPlanId, id).in(ProjectUser::getTypes, 1, 2));

        List<ProjectUser> projectUsers = new ArrayList<>();
        if (reportSurveyUserId != null) {
            projectPlan.setReportSurveyUser(reportSurveyUser);
            projectPlan.setReportSurveyUserId(reportSurveyUserId);
            //修改人员同时 初始化项目人员表
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProjectId(projectId);
            projectUser.setPlanId(id);
            projectUser.setTypes(1);
            projectUser.setUsername(reportSurveyUser);
            projectUser.setUserId(reportSurveyUserId);
            projectUsers.add(projectUser);
        }

        if (checkUserId != null) {
            projectPlan.setCheckUser(checkUser);
            projectPlan.setCheckUserId(checkUserId);
            //修改人员同时 初始化项目人员表
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProjectId(projectId);
            projectUser.setPlanId(id);
            projectUser.setTypes(2);
            projectUser.setUsername(checkUser);
            projectUser.setUserId(checkUserId);

            projectUsers.add(projectUser);
        }

        this.updateById(projectPlan);

        //人员信息
        if (CollUtil.isNotEmpty(projectUsers)) {
            projectUserService.saveBatch(projectUsers);
        }


        //记录
        ProjectRecord projectRecord = new ProjectRecord();
        projectRecord.setProjectId(projectId);
        //确认信息
        projectRecord.setCreateById(ShiroUtils.getUserId());
        projectRecord.setCreateBy(ShiroUtils.getUserName());
        //0：下发 1：调查排单
        projectRecord.setType(1);
        //0：下发 1：派单 2：确认 3：修改
        projectRecord.setSubType(2);
        if (planNumber == 1) {
            projectRecord.setTitle("确认");
        } else {
            projectRecord.setTitle("(" + planNumber + ")确认");
        }
        projectRecord.setPlanDetails("已确认任务,调查日期：" + DateUtil.format(projectPlanDto.getPlanSurveyDate(), "yyyy-MM-dd"));
        projectRecordService.save(projectRecord);


        return projectPlanDto;
    }

    /**
     * 修改排单信息
     *
     * @param projectPlanDto
     * @return
     */
    @Override
    @Transactional
    public ProjectPlanDto planUpdate(ProjectPlanDto projectPlanDto) {

        //项目id
        Long projectId = projectPlanDto.getProjectId();
        //排单id
        Long id = projectPlanDto.getId();

        //排单批次
        Integer planNumber = projectPlanDto.getPlanNumber();

        //修改排单内信息
        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setId(id)
                //报告调查日期
                .setReportSurveyDate(projectPlanDto.getReportSurveyDate());

        //报告调查人
        String reportSurveyUser = projectPlanDto.getReportSurveyUser();
        Long reportSurveyUserId = projectPlanDto.getReportSurveyUserId();
        //调查复核人
        String checkUser = projectPlanDto.getCheckUser();
        Long checkUserId = projectPlanDto.getCheckUserId();
        //删除 当前派单项目人员表报告调查人和调查复核人
        projectUserService.remove(new LambdaQueryWrapper<ProjectUser>().eq(ProjectUser::getProjectId, projectId).eq(ProjectUser::getPlanId, id).in(ProjectUser::getTypes, 1, 2));

        List<ProjectUser> projectUsers = new ArrayList<>();
        if (reportSurveyUserId != null) {
            projectPlan.setReportSurveyUser(reportSurveyUser);
            projectPlan.setReportSurveyUserId(reportSurveyUserId);
            //修改人员同时 初始化项目人员表
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProjectId(projectId);
            projectUser.setPlanId(id);
            projectUser.setTypes(1);
            projectUser.setUsername(reportSurveyUser);
            projectUser.setUserId(reportSurveyUserId);
            projectUsers.add(projectUser);
        }

        if (checkUserId != null) {
            projectPlan.setCheckUser(checkUser);
            projectPlan.setCheckUserId(checkUserId);
            //修改人员同时 初始化项目人员表
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProjectId(projectId);
            projectUser.setPlanId(id);
            projectUser.setTypes(2);
            projectUser.setUsername(checkUser);
            projectUser.setUserId(checkUserId);

            projectUsers.add(projectUser);
        }


        //人员信息
        if (CollUtil.isNotEmpty(projectUsers)) {
            projectUserService.saveBatch(projectUsers);
        }
        //排单修改之前数据
        ProjectPlan oldProjectPlan = this.getOne(new LambdaQueryWrapper<ProjectPlan>().eq(ProjectPlan::getId, id));
        //之前调查日期
        Date oldPlanSurveyDate = oldProjectPlan.getPlanSurveyDate();
        //新调查日期
        Date newPlanSurveyDate = projectPlanDto.getPlanSurveyDate();
        if (!(DateUtil.between(newPlanSurveyDate, oldPlanSurveyDate, DateUnit.DAY) == 0L)) {
            //更新调查日期
            projectPlan.setPlanSurveyDate(newPlanSurveyDate);

            //记录
            ProjectRecord projectRecord = new ProjectRecord();
            projectRecord.setProjectId(projectId);
            //确认信息
            projectRecord.setCreateById(ShiroUtils.getUserId());
            projectRecord.setCreateBy(ShiroUtils.getUserName());
            //0：下发 1：调查排单
            projectRecord.setType(1);
            //0：下发 1：派单 2：确认 3：修改
            projectRecord.setSubType(3);
            if (planNumber == 1) {
                projectRecord.setTitle("修改");
            } else {
                projectRecord.setTitle("(" + planNumber + ")修改");
            }
            projectRecord.setPlanDetails("修改调查日期为：" + DateUtil.format(projectPlanDto.getPlanSurveyDate(), "yyyy-MM-dd"));
            projectRecordService.save(projectRecord);
        }

        this.updateById(projectPlan);


        return projectPlanDto;

    }

}