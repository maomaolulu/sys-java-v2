package com.ruoyi.system.log.controller;

import com.ruoyi.common.utils.R;
import com.ruoyi.system.log.dto.SysOperateLogPageRequestDTO;
import com.ruoyi.system.log.entity.SysOperateLog;
import com.ruoyi.system.log.service.SysOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 日志管理
 * @Date 2023/4/25 13:11
 * @Author maoly
 **/
@Api("日志管理")
@RestController
@RequestMapping("/log")
public class SysOperateLogController{

    @Autowired
    private SysOperateLogService sysOperateLogService;

    @ApiOperation("操作日志查询")
    @PostMapping("/page")
    public R page(@RequestBody SysOperateLogPageRequestDTO queryDto){
        List<SysOperateLog> pageList = sysOperateLogService.queryPage(queryDto);
        return R.resultData(pageList);
    }

}
