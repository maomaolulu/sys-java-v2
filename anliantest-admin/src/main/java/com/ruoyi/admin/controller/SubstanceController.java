package com.ruoyi.admin.controller;

import com.ruoyi.admin.domain.vo.SubstanceVo;
import com.ruoyi.admin.entity.SubstanceEntity;
import com.ruoyi.admin.service.SubstanceService;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/24 10:48
 */
@RestController
@Api("检测项目")
@RequestMapping("/liangyuan/substance")
public class SubstanceController  {

    @Autowired
    private SubstanceService substanceService;


    /**
     * 获取检测项目列表
     * @return
     */
    @GetMapping("/getSubstanceList")
    @ApiOperation("获取检测项目列表")
    public R getSubstanceList() {
        List<SubstanceVo> substanceVoList = substanceService.getSubstanceList();
        return R.ok("查询成功",substanceVoList);
    }

    
    @GetMapping("/getAllAndList")
    public List<SubstanceEntity> getAllAndList() {
        List<SubstanceEntity> list = substanceService.getAllAndList();
        return list;
    }
}
