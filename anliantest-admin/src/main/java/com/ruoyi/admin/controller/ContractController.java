package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.admin.domain.dto.ContractDto;
import com.ruoyi.admin.domain.vo.AssignmentVo;
import com.ruoyi.admin.domain.vo.ContractOaVo;
import com.ruoyi.admin.domain.vo.ContractVo;
import com.ruoyi.admin.entity.ContractEntity;
import com.ruoyi.admin.mapper.ContractMapper;
import com.ruoyi.admin.service.ContractService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.common.CodeGenerateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/31 15:06
 */
@RestController
@Api(tags = "合同表")
@RequestMapping("/liangyuan/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private CodeGenerateService codeGenerateService;


    /**
     * 新增合同
     */
    @PostMapping("/addContract")
    @OperateLog(title = "新增合同")
    @ApiOperation("新增合同")
//    @RequiresPermissions("liangyuan:contract:addContract")
    public R addContract(@RequestBody ContractVo contractVo) {
        R r = contractService.addContract(contractVo);
        return r;
    }


//    /**
//     * 创建并提交评审
//     */
//    @PostMapping("/saveAndExamine")
//    @OperateLog(title = "创建并提交评审")
//    @ApiOperation("创建并提交评审")
//    public R saveAndExamine(@RequestBody ContractVo contractVo) {
//        R r = contractService.saveAndExamine(contractVo);
//        return r;
//    }


    /**
     * 合同列表查询
     */
    @GetMapping("/list")
    @ApiOperation("合同列表查询")
//    @RequiresPermissions("liangyuan:contract:list")
    public R getContractList(ContractDto contractDto) {
        List<ContractEntity> contractEntityList = contractService.getContractList(contractDto);
        return R.resultData(contractEntityList);
    }

    /**
     * 合同详情
     */
    @GetMapping("/getDetail/{id}")
    @ApiOperation("获取合同详情")
//    @RequiresPermissions("liangyuan:contract:getDetail")
    public R getDetail(@PathVariable("id") Long id) {
        ContractVo contractVo = contractService.getDetail(id);
        return R.ok("查询成功", contractVo);
    }

    /**
     * 修改合同基本信息
     */
    @PutMapping("/updateContract")
    @OperateLog(title = "修改合同基本信息")
    @ApiOperation("修改合同基本信息")
//    @RequiresPermissions("liangyuan:contract:updateContract")
    public R updateContract(@RequestBody ContractEntity contractEntity) {
        Boolean b = contractService.updateContract(contractEntity);

        return b ? R.ok("修改成功") : R.error("修改失败");
    }

    /**
     * 批量移交合同
     */
    @PutMapping("/transferContract")
    @OperateLog(title = "批量移交合同")
    @ApiOperation("批量移交合同")
//    @RequiresPermissions("liangyuan:contract:updateContract")
    public R transferContract(@RequestBody AssignmentVo assignmentVo) {
        Boolean b = contractService.transferContract(assignmentVo);
        return b ? R.ok("移交成功") : R.error("移交失败");
    }

    /**
     * 签订合同
     */
    @PutMapping("/signContract")
    @OperateLog(title = "签订合同")
    @ApiOperation("签订合同")
//    @RequiresPermissions("liangyuan:contract:updateContract")
    public R signContract(@RequestBody ContractEntity contractEntity) {
        if (contractEntity.getReviewStatus() != 2) {
            return R.error("该合同未评审通过，不允许签订合同");
        }
        Boolean b = contractService.signContract(contractEntity);
        return b ? R.ok("合同签订成功") : R.error("合同签订失败");
    }

    /**
     * 终止合同
     */
    @PutMapping("/terminateContract")
    @OperateLog(title = "终止合同")
    @ApiOperation("终止合同")
//    @RequiresPermissions("liangyuan:contract:updateContract")
    public R terminateContract(@RequestBody ContractEntity contractEntity) {
        //待履约，履约中的合同允许终止
        if (contractEntity.getPerformStatus() >= 1) {
            return R.error("终止失败，该合同非[待履约/履约中]状态，不允许终止");
        }

        Boolean b = contractService.terminateContract(contractEntity);
        return b ? R.ok("合同终止成功") : R.error("合同终止失败");
    }

    /**
     * 删除(逻辑)合同
     */
    @PutMapping("/deleteContract")
    @OperateLog(title = "删除(逻辑)合同")
    @ApiOperation("删除(逻辑)合同")
    public R deleteContract(@RequestBody List<Long> idList) {
        //验证是否符合删除要求
        List<ContractEntity> contractEntityList = contractMapper.selectBatchIds(idList);
        for (ContractEntity contractEntity : contractEntityList) {
            Integer reviewStatus = contractEntity.getReviewStatus();
//            if (reviewStatus != 0 && reviewStatus != 4) {
            if (reviewStatus == 1 || reviewStatus == 2) {
                return R.error("合同" + contractEntity.getId() + "已[提交审核/评审通过]，无法删除");
            }
        }

        Boolean b = contractService.deleteContract(idList);

        return b ? R.ok("删除成功") : R.error("删除失败");
    }

    /**
     * 合同提交评审
     */
    @PostMapping("/contractExamine")
    @OperateLog(title = "合同提交评审")
    @ApiOperation("合同提交评审")
    public R contractExamine(@RequestBody ContractEntity contractEntity) {
        R r = contractService.contractExamine(contractEntity.getId());

        return r;
    }

    /**
     * 合同状态修改
     *
     * @param contractOaVo
     * @return
     */
    @PostMapping("/updateReviewStatus")
    @ApiOperation("oa调用接口修改合同审批状态")
    public R updateStatus(@RequestBody ContractOaVo contractOaVo) {

        R r = contractService.updateReviewStatus(contractOaVo);

        return r;
    }

    /**
     * 合同撤回审批
     *
     * @return
     */
    @OperateLog(title = "合同撤回审批")
    @ApiOperation("合同撤回审批")
    @PostMapping("/retractContract/{id}")
    public R retract(@PathVariable("id") Long id) {
        R r = contractService.retract(id);
        return r;
    }
}
