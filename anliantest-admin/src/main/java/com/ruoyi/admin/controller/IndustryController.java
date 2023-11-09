package com.ruoyi.admin.controller;

import com.ruoyi.admin.entity.IndustryEntity;
import com.ruoyi.admin.service.IndustryService;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行业类别
 */
@RestController
@RequestMapping("/industry")
public class IndustryController {
    @Autowired
    private IndustryService industryService;

    /**
     * 获取行业类别列表
     * @param joint
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据joint显示项目类别")
    public R list(String joint){
        List<IndustryEntity> list = industryService.listJoint(joint);

        return R.ok().put("list", list);
    }
}