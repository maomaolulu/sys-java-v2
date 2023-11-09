package com.ruoyi.admin.controller;

import com.ruoyi.admin.domain.vo.QuotationListVo;
import com.ruoyi.admin.domain.vo.QuotationVo;
import com.ruoyi.admin.entity.ContractEntity;
import com.ruoyi.admin.entity.QuotationEntity;
import com.ruoyi.admin.entity.QuotationInfoEntity;
import com.ruoyi.admin.service.ContractService;
import com.ruoyi.admin.service.QuotationService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/4/20 9:58
 */
@RestController
@Api(tags = "报价单")
@RequestMapping("/liangyuan/quotation")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private ContractService contractService;

    /**
     * 新增报价单
     */
    @PostMapping("/addQuotation")
    @OperateLog(title = "新增报价单")
    @ApiOperation("新增报价单")
//    @RequiresPermissions("liangyuan:quotation:addQuotation")
    public R addQuotation(@RequestBody QuotationVo quotationVo) throws UnsupportedEncodingException {

        Map<String, Object> map = quotationService.addQuotation(quotationVo);
        return R.data(map);
    }



    /**
     * 报价单分页列表
     */
    @GetMapping("/list")
    @ApiOperation("报价单分页列表")
//    @RequiresPermissions("liangyuan:quotation:list")
    public R getQuotationList(QuotationListVo quotationListVo) {

        List<QuotationEntity> quotationList = quotationService.getQuotationList(quotationListVo);
        return R.resultData(quotationList);
    }

    /**
     * 新增合同,项目时获取客户公司状态为报价中的报价单
     */
    @GetMapping("/listAll")
    @ApiOperation("新增合同,项目时获取客户公司状态为报价中的报价单")
//    @RequiresPermissions("liangyuan:quotation:list")
    public R getQuotationList(QuotationEntity quotationEntity) {

        List<QuotationEntity> quotationList = quotationService.getQuotationListAll(quotationEntity);
        return R.ok("查询成功", quotationList);
    }

    /**
     * 报价单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetail/{id}")
    @ApiOperation("报价单详情")
//    @RequiresPermissions("liangyuan:quotation:getDetail")
    public R getDetail(@PathVariable("id") Long id) {

        QuotationVo quotationVo = quotationService.getDetail(id);
        return R.ok("查询成功", quotationVo);
    }

    /**
     * 报价单修改
     *
     * @param quotationVo
     * @return
     */
    @PutMapping("/updateQuotation")
    @OperateLog(title = "报价单修改")
    @ApiOperation("报价单修改")
//    @RequiresPermissions("liangyuan:quotation:updateQuotation")
    public R updateQuotation(@RequestBody QuotationVo quotationVo) throws UnsupportedEncodingException {
        QuotationEntity quotationEntity = quotationVo.getQuotationEntity();
        //状态为审批中的报价单不允许编辑
        if (quotationEntity.getStatus() == 1) {
            return R.error("报价单状态为[审批中]，不允许编辑");
        }
        //如果关联了合同，关联的合同正在评审或评审通过，无法修改
        Long contractId = quotationEntity.getContractId();
        if (contractId != null) {
            ContractEntity contractEntity = contractService.getById(contractId);
            Integer reviewStatus = contractEntity.getReviewStatus();
            //0待评审,1评审中,2评审通过,3评审未通过,4撤销审核
            if (reviewStatus == 1 || reviewStatus == 2) {
                return R.error("报价单关联合同[评审中/评审通过]，不允许编辑");
            }
        }
        Map<String, Object> map = quotationService.updateQuotation(quotationVo);
        return R.data(map);
    }

    /**
     * 终止报价单
     *
     * @param idList
     * @return
     */
    @PutMapping("/terminateQuotation")
    @OperateLog(title = "终止报价单")
    @ApiOperation("终止报价单")
//    @RequiresPermissions("liangyuan:quotation:updateQuotation")
    public R terminateQuotation(@RequestBody List<Long> idList) {
//        List<Long> idList = Arrays.asList(ids);
        Boolean b = quotationService.terminateQuotation(idList);
        return b ? R.ok() : R.error();
    }

    /**
     * 导出
     */
    @GetMapping(value = "/exportQuotation")
    @OperateLog(title = "导出", isSaveRequestData = false)
    public void exportQuotation(HttpServletResponse response, Long id) throws IOException {
        quotationService.exportQuotation(response, id);
    }
    /**
     * 最终版报价导出
     */
    @GetMapping(value = "/exportFinalQuotation")
    @OperateLog(title = "最终版报价导出", isSaveRequestData = false)
    public void exportFinalQuotation(HttpServletResponse response, Long id) throws IOException {
        quotationService.exportFinalQuotation(response, id);
    }

    /**
     * 报价单状态修改
     *
     * @param quotationEntity
     * @return
     */
    @PutMapping("/updateStatus")
    @OperateLog(title = "报价单状态修改")
    @ApiOperation("报价单状态修改")
//    @RequiresPermissions("liangyuan:quotation:updateQuotation")
    public R updateStatus(@RequestBody QuotationEntity quotationEntity) {

        Boolean b = quotationService.updateStatus(quotationEntity);

        return b ? R.ok("修改成功") : R.error("修改失败");
    }

    /**
     * (逻辑)删除报价单
     *
     * @return
     */
    @PutMapping("/deleteQuotation")
    @OperateLog(title = "(逻辑)删除报价单")
    @ApiOperation("(逻辑)删除报价单")
    public R deleteQuotation(@RequestBody List<Long> idList) {
        //验证是否符合删除要求
        for (Long id : idList) {
            QuotationEntity quotationEntity = quotationService.getById(id);
            Integer status = quotationEntity.getStatus();
            if (status != 2 && status != 3 && status != 6) {
                return R.error("报价单" + id + "不是[审核失败/报价中/撤销审核]状态，无法删除");
            }
        }

        Boolean b = quotationService.deleteQuotation(idList);
        return b ? R.ok("删除报价单成功") : R.error("删除报价单失败");
    }

    /**
     * 报价单撤回审批
     *
     * @return
     */
    @OperateLog(title = "报价单撤回审批")
    @ApiOperation("报价单撤回审批")
    @PutMapping("/retractQuotation/{id}")
    public R retract(@PathVariable("id") Long id) {
        return quotationService.retract(id);
//        return b ? R.ok("撤回审批成功") : R.error("撤回审批失败");

    }

    /**
     * 导入报价详情(从文件解析)
     *
     * @return R
     */
    @ApiOperation("导入报价详情(从文件解析)")
    @PostMapping("/importFromFile")
    public R importFromFile(MultipartFile file, HttpServletRequest request){
        try{
            return R.ok("成功",quotationService.importFromFile(file, request));
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }

    /**
     * 根据报价单id获取报价单检测物质详情信息
     * @return
     */
    @ApiOperation("根据报价单id获取报价单检测物质详情信息")
    @GetMapping("/getInfoById")
    public R getInfoById(Long id){
        QuotationVo quotationVo = quotationService.getInfoById(id);
        return R.ok("info",quotationVo);
    }

}
