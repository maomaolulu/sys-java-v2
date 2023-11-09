package com.ruoyi.system.user.controller;

import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.user.entity.PersonnelEntity;
import com.ruoyi.system.user.service.PersonnelService;
import com.ruoyi.system.user.vo.PersonnelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 14:55
 */
@RestController
@Api("人事档案管理")
@RequestMapping("/file")
public class PersonnelController {

    @Autowired
    private PersonnelService personnelService;


    /**
     * 新增人事档案
     * @param personnelEntity
     * @return
     */
    @ApiOperation("新增人事档案")
    @OperateLog(title = "新增人事档案")
    @PostMapping("/addFile")
    public R addFile(@RequestBody PersonnelEntity personnelEntity){

        personnelService.addFile(personnelEntity);
        return R.ok();
    }

    /**
     * 批量新增人事档案
     * @param personnelVo
     * @return
     */
    @ApiOperation("批量新增人事档案")
    @OperateLog(title = "批量新增人事档案")
    @PostMapping("/addFileBatch")
    public R addFileBatch(@RequestBody PersonnelVo personnelVo) {

        personnelService.addFileBatch(personnelVo);
        return R.ok();
    }


    /**
     * 人事档案列表(不分页)
     * @param userId
     * @return
     */
    @ApiOperation("人事档案列表(不分页)")
    @GetMapping("/list/{userId}")
    public R getFileList(@PathVariable("userId") Long userId) {

        List<PersonnelEntity> personnelEntityList = personnelService.getFileList(userId);

        return R.ok().put("list", personnelEntityList);
    }


    /**
     * 修改人事档案
     * @param personnelVo
     * @return
     */
    @ApiOperation("修改人事档案")
    @OperateLog(title = "修改人事档案")
    @PutMapping("/updateFile")
    public R updateFile(@RequestBody PersonnelVo personnelVo){

        personnelService.updateFile(personnelVo);

        return R.ok();
    }


    /**
     * 删除人事档案
     * @param userId
     * @return
     */
    @ApiOperation("删除人事档案")
    @OperateLog(title = "删除人事档案")
    @DeleteMapping("/deleteFile/{userId}")
    public R deleteFile(@PathVariable("userId") Long userId){

        personnelService.deleteFile(userId);
        return R.ok();
    }
}
