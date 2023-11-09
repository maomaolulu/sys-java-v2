package com.ruoyi.admin.controller;

import com.ruoyi.admin.domain.dto.ProjectPlanDto;
import com.ruoyi.admin.entity.ProjectPlan;
import com.ruoyi.admin.service.ProjectPlanService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import com.ruoyi.common.utils.pageUtil;
import com.ruoyi.system.login.utils.ShiroUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-05-17
 */
@RestController
@Api(tags = "排单")
@RequestMapping("/liangyuan/plan")
public class PlanController {


    private ProjectPlanService projectPlanService;
    @Autowired
    public PlanController(  ProjectPlanService projectPlanService) {
        this.projectPlanService = projectPlanService;
    }


    /**
     * 评价主管排单界面
     * @param projectPlanDto
     * @return
     */
    @GetMapping("planPjList")
    public R planPjList(ProjectPlanDto projectPlanDto){
        pageUtil.startPage();
        List<ProjectPlanDto> projectPlanDtos = projectPlanService.planPjList(projectPlanDto);
        return R.resultData(projectPlanDtos);
    }

    /**
     * 调查任务
     * @param projectPlanDto
     * @return
     */
    @GetMapping("surveyList")
    public R surveyList(ProjectPlanDto projectPlanDto){
        pageUtil.startPage();
        List<ProjectPlanDto> projectPlanDtos = projectPlanService.surveyList(projectPlanDto);
        return R.resultData(projectPlanDtos);
    }

    /**
     * 派单详情、调查任务详情
     * @param projectPlanDto
     * @return
     */
    @GetMapping("/info")
    public R info(ProjectPlanDto projectPlanDto){

        ProjectPlanDto projectPlanDtos = projectPlanService.info(projectPlanDto);

        return R.data(projectPlanDtos);
    }


    /**
     * 调查任务(派单/变更)
     * @param projectPlanDto
     * @return
     */
    @PostMapping("plan")
    @OperateLog(title = "调查任务(派单/变更)")
    public R plan(@RequestBody  ProjectPlanDto projectPlanDto){

        projectPlanService.plan(projectPlanDto);
        return R.ok("派单成功");
    }


    /**
     * 确认排单
     * @param projectPlanDto
     * @return
     */
    @OperateLog(title = "确认排单")
    @PostMapping("planConfirm")
    public R planConfirm(@RequestBody  ProjectPlanDto projectPlanDto){

        projectPlanService.planConfirm(projectPlanDto);
        return R.ok("确认成功");
    }

    /**
     * 修改排单
     * @param projectPlanDto
     * @return
     */
    @PostMapping("planUpdate")
    @OperateLog(title = "修改排单")
    public R planUpdate(@RequestBody  ProjectPlanDto projectPlanDto){
        projectPlanService.planUpdate(projectPlanDto);
        return R.ok("修改成功");
    }

    /**
     * 申请新增调查排单
     * @param projectPlan
     * @return
     */
    @PostMapping("applyPlan")
    @OperateLog(title = "申请新增调查排单")
    public R applyPlan(@RequestBody ProjectPlan projectPlan){

        projectPlan.setPlanStatus(0)
                .setCreateBy(ShiroUtils.getUserName())
                .setCreateById(ShiroUtils.getUserId())
                .setPlanNumber(projectPlan.getPlanNumber()+1);
        projectPlanService.save(projectPlan);
        return R.ok("申请新增调查排单成功");
    }



}
