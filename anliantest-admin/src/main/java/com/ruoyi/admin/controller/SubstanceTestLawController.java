package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.admin.domain.dto.SubstanceTestLawDto;
import com.ruoyi.admin.entity.SubstanceTestLawEntity;
import com.ruoyi.admin.service.SubstanceTestLawService;
import com.ruoyi.common.utils.R;
import com.ruoyi.common.utils.pageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author gy
 * @version 1.0
 * @date 2023-06-06 19:06
 */
@RestController
@Api("物质检测标准法律法规表")
@RequestMapping("/liangyuan/substancetestlaw")
public class SubstanceTestLawController {

    @Autowired
    private SubstanceTestLawService substanceTestLawService;
    private final String moreBlank = "  ";
    private final String testStandard = "test_standards";

    /**
     * 获取检测标准/法律法规列表
     * @return R
     */
    @GetMapping("/list")
    @ApiOperation("获取物质检测标准/法律法规表列表")
    public R list(SubstanceTestLawDto substanceTestLawdto) {
        pageUtil.startPage();
        return R.resultData(substanceTestLawService.listByCondition(substanceTestLawdto));
    }

    /**
     * 新增检测标准/法律法规
     * @return R
     */
    @PostMapping("/insert")
    @ApiOperation("新增物质检测标准/法律法规表")
    public R insert(@RequestBody @Validated SubstanceTestLawDto substanceTestLawdto,BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            if (substanceTestLawdto.getTestStandardsName().contains(moreBlank)) {
                return R.error("名称包含多个空格,请检查");
            }
            if (substanceTestLawService.list(new QueryWrapper<SubstanceTestLawEntity>().eq(testStandard, substanceTestLawdto.getTestStandards())).size() > 0) {
                return R.error("该文号已存在,请检查");
            }
            return substanceTestLawService.saveByCondition(substanceTestLawdto) ? R.ok("新增成功") : R.error("新增失败");
        } else {
            return R.error(Objects.requireNonNull(bindingResult.getFieldError()).getField() + bindingResult.getFieldError().getDefaultMessage());
        }
    }

    /**
     * 修改检测标准/法律法规
     * @return R
     */
    @PutMapping("/update")
    @ApiOperation("修改物质检测标准/法律法规表")
    public R update(@RequestBody @Validated SubstanceTestLawDto substanceTestLawdto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            if (substanceTestLawdto.getTestStandardsName().contains(moreBlank)) {
                return R.error("名称包含多个空格,请检查");
            }
            return substanceTestLawService.updateByCondition(substanceTestLawdto) ? R.ok("修改成功") : R.error("修改失败");
        } else {
            return R.error(Objects.requireNonNull(bindingResult.getFieldError()).getField() + bindingResult.getFieldError().getDefaultMessage());
        }
    }

    /**
     * 删除检测标准/法律法规
     * @return R
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除物质检测标准/法律法规表")
    public R delete(Long id) {
        return substanceTestLawService.deleteByCondition(id) ? R.ok("删除成功") : R.error("删除失败");
    }
}
