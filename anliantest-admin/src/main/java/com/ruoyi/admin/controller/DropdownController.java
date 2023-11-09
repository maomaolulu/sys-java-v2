package com.ruoyi.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.admin.entity.SubstanceInfoEntity;
import com.ruoyi.admin.entity.SubstanceTestLawEntity;
import com.ruoyi.admin.service.SubstanceInfoService;
import com.ruoyi.admin.service.SubstanceTestLawService;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 下拉框
 *
 * @author zh
 * @date 2023-06-28
 */
@RestController
@Api(tags = "下拉框")
@RequestMapping("/liangyuan/dropdown")
public class DropdownController {


    private final SubstanceInfoService substanceInfoService;
    private final SubstanceTestLawService substanceTestLawService;

    @Autowired
    public DropdownController(SubstanceInfoService substanceInfoService, SubstanceTestLawService substanceTestLawService) {
        this.substanceInfoService = substanceInfoService;
        this.substanceTestLawService = substanceTestLawService;
    }

    /**
     * 物质检测方法：危害因素下拉
     */
    @GetMapping("/substanceInfoDropdownList")
    public List<SubstanceInfoEntity> substanceInfoDropdownList(String substanceName) {
        return substanceInfoService.list(new QueryWrapper<SubstanceInfoEntity>()
                .like(StrUtil.isNotBlank(substanceName), "substance_name", substanceName)
                .eq("delete_flag",0)
                .last("limit 30 "));
    }


    /**
     * 物质检测方法:检测对象、标准号、标准名下拉框
     *
     * @return R
     */
    @GetMapping("/dropdownList")
    @ApiOperation("物质检测方法:检测对象、标准号、标准名下拉框")
    public R dropdownList(SubstanceTestLawEntity substanceTestLaw) {

        List<SubstanceTestLawEntity> list = substanceTestLawService.list(new QueryWrapper<SubstanceTestLawEntity>()
                .like(StrUtil.isNotBlank(substanceTestLaw.getTestStandards()), "test_standards", substanceTestLaw.getTestStandards())
                .like(StrUtil.isNotBlank(substanceTestLaw.getTestStandardsName()), "test_standards_name", substanceTestLaw.getTestStandardsName())
                .eq(substanceTestLaw.getTestCategory() != null, "test_category", substanceTestLaw.getTestCategory())
                .isNotNull("test_category")
                .eq("delete_flag",0)
                .last("limit 30")
        );
        return R.data(list);
    }
}
