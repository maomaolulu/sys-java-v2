package com.ruoyi.admin.controller;

import com.ruoyi.admin.domain.dto.CompanyDto;
import com.ruoyi.admin.domain.vo.CompanyBasicVo;
import com.ruoyi.admin.domain.vo.CompanyContactVo;
import com.ruoyi.admin.domain.vo.CompanyVo;
import com.ruoyi.admin.entity.CompanyEntity;
import com.ruoyi.admin.service.CompanyService;
import com.ruoyi.admin.domain.vo.AssignmentVo;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.login.utils.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/3/24 19:10
 */
@RestController
@Api(tags = "客户管理")
@RequestMapping("/liangyuan/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 校验信用社代码
     */
    @ApiOperation("校验信用社代码")
    @GetMapping("/checkCode/{code}")
//    @RequiresPermissions("liangyuan:company:getDetail")
    public R checkCode(@PathVariable("code") String code) {
        Boolean b = companyService.checkCode(code);
        if (b) {
            return R.error("当前客户存在，请勿重复添加");
        }
        return R.ok("请确认客户信息，客户的统一信用社会代码成功创建后无法更改！");
    }

    /**
     * 新增客户
     */
    @PostMapping("/addCompany")
    @OperateLog(title = "新增客户")
    @ApiOperation("新增客户")
//    @RequiresPermissions("liangyuan:company:addCompany")
    public R addCompany(@RequestBody CompanyVo companyVo) throws ParseException {
        Boolean b = companyService.addCompany(companyVo);

        return b ? R.ok("创建成功") : R.error("创建失败");
    }

    /**
     * 客户列表查询
     */
    @GetMapping("/list")
    @ApiOperation("客户列表查询")
//    @RequiresPermissions("liangyuan:company:list")
    public R getCompanyList(CompanyDto companyDto) {
        List<CompanyEntity> companyList = companyService.getCompanyList(companyDto);
        return R.resultData(companyList);
    }

    /**
     * 导出客户列表查询
     */
    @GetMapping("/exportList")
    @ApiOperation("导出客户列表查询")
//    @RequiresPermissions("liangyuan:company:list")
    public R getCompanyExportList(@RequestParam Map<String, Object> params) {
        List<CompanyEntity> companyList = companyService.getCompanyExportList(params);
        return R.ok("查询成功", companyList);
    }

    /**
     * 获取“我”的客户列表
     */
    @GetMapping("/getMyList")
    @ApiOperation("获取“我”的客户列表")
//    @RequiresPermissions("liangyuan:company:list")
    public R getMyCompany() {
        Long userId = ShiroUtils.getUserId();
        if (userId != null) {
            List<CompanyVo> companyList = companyService.getMyCompany(userId);
            return R.ok("查询成功", companyList);
        }
        return R.error();
    }


    /**
     * 获取客户详情
     */
    @ApiOperation("获取客户详情")
    @GetMapping("/getDetail/{id}")
//    @RequiresPermissions("liangyuan:company:getDetail")
    public R getDetail(@PathVariable("id") Long id) {
        CompanyVo companyVo = companyService.getDetail(id);

        return companyVo != null ? R.ok("查询成功", companyVo) : R.error();
    }

    /**
     * 修改客户基本信息
     */
    @ApiOperation("修改客户基本信息")
    @OperateLog(title = "修改客户基本信息")
    @PutMapping("/updateCompany")
//    @RequiresPermissions("liangyuan:company:updateCompany")
    public R updateCompany(@RequestBody CompanyBasicVo companyBasicVo) {
        Boolean b = companyService.updateCompany(companyBasicVo);

        return b ? R.ok("修改成功") : R.error("修改失败");
    }

    /**
     * 修改联系人信息
     */
    @ApiOperation("修改联系人信息")
    @OperateLog(title = "修改联系人信息")
    @PutMapping("/updateContact")
//    @RequiresPermissions("liangyuan:company:updateContact")
    public R updateContact(@RequestBody CompanyContactVo companyContactVo) {
        Boolean b = companyService.updateContact(companyContactVo);

        return b ? R.ok("修改成功") : R.error("修改失败");
    }

    /**
     * 领取客户资源
     */
    @ApiOperation("/领取客户资源")
    @PutMapping("/getCompany")
//    @RequiresPermissions("liangyuan:company:updateCompany")
    public R getCompany(@RequestBody AssignmentVo assignmentVo) {
        Boolean b = companyService.getCompany(assignmentVo);
        return b ? R.ok("领取成功") : R.error("领取失败,被选中客户已被领取");
    }


    /**
     * 释放客户资源
     */
    @ApiOperation("释放客户资源")
    @PutMapping("/releaseCompany")
//    @RequiresPermissions("liangyuan:company:updateCompany")
    public R releaseCompany(@RequestBody AssignmentVo assignmentVo) {

        Boolean b = companyService.releaseCompany(assignmentVo);
        return b ? R.ok("释放成功") : R.error("释放失败");
    }

    /**
     * 分配客户资源
     */
    @ApiOperation("分配客户资源")
    @PutMapping("/allotCompany")
//    @RequiresPermissions("liangyuan:company:updateCompany")
    public R allotCompany(@RequestBody AssignmentVo assignmentVo) {
        Boolean b = companyService.allotCompany(assignmentVo);
        return b ? R.ok("分配成功") : R.error("分配失败");
    }


}
