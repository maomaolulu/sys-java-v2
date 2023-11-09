package com.ruoyi.admin.controller;

import com.ruoyi.admin.entity.SubstanceStateLimitEntity;
import com.ruoyi.admin.service.SubstanceStateLimitService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gy
 * @date 2023/6/06 19:00
 */
@RestController
@Api("物质不同条件限值表")
@RequestMapping("/liangyuan/substancestatelimit")
public class SubstanceStateLimitController {

    @Autowired
    private SubstanceStateLimitService substanceStateLimitService;


    /**
     * 获取物质不同条件限值列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获取检测项目列表")
    public R list(SubstanceStateLimitEntity stateLimit) {
        List<SubstanceStateLimitEntity> stateLimits = substanceStateLimitService.list();
        return R.ok("查询成功", stateLimits);
    }


    /**
     * 新增危害因素限值
     *
     * @return
     */
    @PostMapping("/addLimit")
    @ApiOperation("新增危害因素限值")
    @OperateLog(title = "新增危害因素限值")
    public R addLimit(@RequestBody SubstanceStateLimitEntity stateLimit) {

        Boolean b = substanceStateLimitService.addLimit(stateLimit);
        return b ? R.ok("新增成功") : R.error("新增失败");
    }

    /**
     * 获取危害因素限值
     *
     * @return
     */
    @GetMapping("/getBySubstanceId")
    @ApiOperation("获取危害因素限值")
    public R getListBySubstanceInfoId(Long substanceInfoId) {
        List<SubstanceStateLimitEntity> stateLimitEntityList = substanceStateLimitService.getListBySubstanceInfoId(substanceInfoId);
        return R.data(stateLimitEntityList);
    }


    /**
     * 修改危害因素限值
     *
     * @param stateLimitEntity
     * @return
     */
    @PutMapping("/updateLimit")
    @ApiOperation("修改危害因素限值")
    @OperateLog(title = "修改危害因素限值")
    public R updateLimit(@RequestBody SubstanceStateLimitEntity stateLimitEntity) {
        Boolean b = substanceStateLimitService.updateLimit(stateLimitEntity);
        return b ? R.ok("修改成功") : R.error("修改失败");
    }

    /**
     * 逻辑删除危害因素限值
     *
     * @return
     */
    @PutMapping("/deleteLimit")
    @ApiOperation("逻辑删除危害因素限值")
    @OperateLog(title = "逻辑删除危害因素限值")
    public R deleteLimit(Long id) {
        Boolean b = substanceStateLimitService.deleteLimit(id);
        return b ? R.ok("删除成功") : R.error("删除失败");
    }

}
