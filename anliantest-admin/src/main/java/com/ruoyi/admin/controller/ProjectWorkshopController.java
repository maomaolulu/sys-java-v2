package com.ruoyi.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.admin.domain.dto.ProjectWorkshopDto;
import com.ruoyi.admin.entity.ProjectWorkshop;
import com.ruoyi.admin.service.ProjectWorkshopService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.exception.RRException;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-08
 * @desc : 项目车间岗位
 */
@RestController
@Api(tags = "项目车间岗位")
@RequestMapping("/liangyuan/project_workshop")
public class ProjectWorkshopController {


    private ProjectWorkshopService projectWorkshopService;

    @Autowired
    public ProjectWorkshopController(ProjectWorkshopService projectWorkshopService) {
        this.projectWorkshopService = projectWorkshopService;
    }


    /**
     * 项目车间岗位点位列表
     *
     * @param projectWorkshop
     * @return
     */
    @GetMapping("pointList")
    public R pointList(ProjectWorkshopDto projectWorkshop) {

        return R.data(projectWorkshopService.pointList(projectWorkshop));
    }


    /**
     * 项目车间岗位点位表格列表
     *
     * @param planId
     * @return
     */
    @GetMapping("pointFormList")
    public R pointList(Long planId) {
        return R.data(projectWorkshopService.pointFormList(planId));
    }


    /**
     * 新增车间岗位
     *
     * @param projectWorkshopDto
     * @return
     */
    @PostMapping("saveWorkshop")
    @OperateLog(title = "新增车间岗位")
    public R saveWorkshop(@RequestBody ProjectWorkshopDto projectWorkshopDto) {
        verify(projectWorkshopDto);

        ProjectWorkshop projectWorkshop = BeanUtil.copyProperties(projectWorkshopDto, ProjectWorkshop.class);
        projectWorkshopService.save(projectWorkshop);
        return R.ok("新增成功");
    }

    /**
     * 修改车间岗位
     *
     * @param projectWorkshopDto
     * @return
     */
    @PostMapping("updateWorkshop")
    @OperateLog(title = "修改车间岗位")
    public R updateWorkshop(@RequestBody ProjectWorkshopDto projectWorkshopDto) {
        verify(projectWorkshopDto);

        ProjectWorkshop projectWorkshop = BeanUtil.copyProperties(projectWorkshopDto, ProjectWorkshop.class);
        projectWorkshopService.updateById(projectWorkshop);
        return R.ok("修改车间岗位成功");
    }


    /**
     * 删除项目车间岗位/点位
     */
    @DeleteMapping("/del")
    @OperateLog(title = "删除项目车间岗位")
    public R remove(@RequestBody ProjectWorkshopDto projectWorkshopDto) {

        projectWorkshopService.removeProjectWorkshop(projectWorkshopDto);
        return R.ok("删除成功");
    }


//    /**
//     * 选择车间岗位（评价）
//     */
//    @GetMapping("/selectProjectWorkshop")
//    public R selectProjectWorkshop(Long planId) {
//
//        List<ProjectWorkshopDto> projectWorkshopDtos = projectWorkshopService.selectProjectWorkshop(planId);
//
//        return R.data(projectWorkshopDtos);
//    }


    //重复验证
    public void verify(ProjectWorkshopDto projectWorkshopDto) {

        int count = projectWorkshopService.count(new LambdaQueryWrapper<ProjectWorkshop>()
                .eq(ProjectWorkshop::getPlanId, projectWorkshopDto.getPlanId())
                .eq(ProjectWorkshop::getType, projectWorkshopDto.getType())
                .eq(ProjectWorkshop::getPid, projectWorkshopDto.getPid())
                .eq(ProjectWorkshop::getName, projectWorkshopDto.getName())

        );
        if (count > 0) {
            throw new RRException("建筑、车间、岗位不能重复");
        }

    }


}
