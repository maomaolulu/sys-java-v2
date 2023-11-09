package com.ruoyi.system.user.controller;

import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.user.entity.TechnologyEntity;
import com.ruoyi.system.user.service.TechnologyService;
import com.ruoyi.system.user.vo.TechnologyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 16:26
 */
@RestController
@Api("技术档案管理")
@RequestMapping("/technology")
public class TechnologyController  {

    @Autowired
    private TechnologyService technologyService;


    /**
     * 校验证书编号
     * @param code
     * @return
     */
    @ApiOperation("校验证书编号")
    @GetMapping("/checkCode/{code}")
    public R checkCode(@PathVariable("code") String code) {
        Boolean b = technologyService.checkCode(code);
        if (b) {
            return R.error("当前证书存在，请勿重复添加");
        }
        return R.ok();
    }

    /**
     * 新增技术档案
     * @param technologyEntity
     * @return
     */
    @ApiOperation("新增技术档案")
    @OperateLog(title = "新增技术档案")
    @PostMapping("/addTechnology")
    public R addTechnology(@RequestBody TechnologyEntity technologyEntity){

        technologyService.addTechnology(technologyEntity);
        return R.ok();
    }


    /**
     * 批量新增技术档案
     * @param technologyVo
     * @return
     */
    @ApiOperation("批量新增技术档案")
    @OperateLog(title = "批量新增技术档案")
    @PostMapping("/addTechnologyBatch")
    public R addTechnologyBatch(@RequestBody TechnologyVo technologyVo){

        technologyService.addTechnologyBatch(technologyVo);
        return R.ok();
    }


    /**
     * 技术档案列表(不分页)
     * @param userId
     * @return
     */
    @ApiOperation("技术档案列表(不分页)")
    @GetMapping("/list/{userId}")
    public R getTechnologyList(@PathVariable("userId") Long userId) {

        List<TechnologyEntity> technologyEntityList = technologyService.getFileList(userId);

        return R.ok().put("list",technologyEntityList);
    }


    /**
     * 修改技术档案
     * @param technologyVo
     * @return
     */
    @ApiOperation("修改技术档案")
    @OperateLog(title = "修改技术档案")
    @PutMapping("/updateTechnology")
    public R updateTechnology(@RequestBody TechnologyVo technologyVo){

        technologyService.updateTechnology(technologyVo);

        return R.ok();
    }


    /**
     * 删除技术档案
     * @param userId
     * @return
     */
    @ApiOperation("删除技术档案")
    @OperateLog(title = "删除技术档案")
    @DeleteMapping("/deleteTechnology/{userId}")
    public R deleteTechnology(@PathVariable("userId") Long userId){

        technologyService.deleteTechnology(userId);
        return R.ok();
    }


}
