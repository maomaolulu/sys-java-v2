package com.ruoyi.admin.controller;

import com.ruoyi.admin.domain.vo.SubstanceInfoVo;
import com.ruoyi.admin.entity.SubstanceInfoEntity;
import com.ruoyi.admin.service.SubstanceInfoService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/6/25 13:34
 */
@RestController
@Api("危害因素")
@RequestMapping("/liangyuan/substanceInfo")
public class SubstanceInfoController {

    @Autowired
    private SubstanceInfoService substanceInfoService;


    /**
     * 新增危害因素
     *
     * @return
     */
    @PostMapping("/addSubstance")
    @ApiOperation("新增危害因素")
    @OperateLog(title = "新增危害因素")
    public R addSubstanceInfo(@RequestBody SubstanceInfoEntity substanceInfo) {
//        , BindingResult bindingResult
//        @Valid
        //        if(bindingResult.hasErrors()){
//            return R.error(bindingResult.getFieldError().getDefaultMessage());
//        }

        Boolean b = substanceInfoService.addSubstanceInfo(substanceInfo);
        return b ? R.ok("新增成功") : R.error("新增失败");
    }

    /**
     * 获取危害因素列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获取危害因素列表")
    public R getSubstanceInfoList(SubstanceInfoVo substanceInfoVo) {

        List<SubstanceInfoEntity> infoEntityList = substanceInfoService.getSubstanceInfoList(substanceInfoVo);
        return R.resultData(infoEntityList);
    }

    /**
     * 获取危害因素详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetail")
    @ApiOperation("获取危害因素详情")
    public R getSubstanceInfo(Long id) {

        SubstanceInfoEntity substanceInfo = substanceInfoService.getSubstanceInfo(id);
        return R.ok("查询成功", substanceInfo);
    }


    /**
     * 修改危害因素信息
     *
     * @return
     */
    @PutMapping("/updateSubstance")
    @ApiOperation("修改危害因素信息")
    @OperateLog(title = "修改危害因素信息")
    public R updateSubstanceInfo(@RequestBody SubstanceInfoEntity substanceInfo) {

        Boolean b = substanceInfoService.updateSubstanceInfo(substanceInfo);
        return b ? R.ok("修改成功") : R.error("修改失败");
    }

    /**
     * 逻辑删除危害因素信息
     *
     * @return
     */
    @PutMapping("/deleteSubstance")
    @ApiOperation("逻辑删除危害因素信息")
    @OperateLog(title = "逻辑删除危害因素信息")
    public R deleteSubstanceInfo(Long id) {

        Boolean b = substanceInfoService.deleteSubstanceInfo(id);
        return b ? R.ok("删除成功") : R.error("删除失败");
    }

}
