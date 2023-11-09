package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.admin.domain.dto.InvoiceDto;
import com.ruoyi.admin.entity.InvoiceEntity;
import com.ruoyi.admin.service.InvoiceService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.utils.R;
import com.ruoyi.common.utils.pageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author gy
 * @date 2023-06-08 9:22
 */
@RestController
@Api("开票管理")
@RequestMapping("/liangyuan/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * 分页查询开票信息列表
     * @param invoiceDto 开票信息查询条件
     * @return R
     */
    @GetMapping("/list")
    @OperateLog(title = "分页查询开票信息列表")
    @ApiOperation("分页查询开票信息列表")
    public R list(InvoiceDto invoiceDto){
        pageUtil.startPage();
        return R.ok("查询成功",invoiceService.list(new QueryWrapper<InvoiceEntity>()
                .eq(invoiceDto.getId()!=null,"id",invoiceDto.getId())
                .eq(invoiceDto.getContractId()!=null,"contract_id",invoiceDto.getContractId())
                .eq(invoiceDto.getProjectId()!=null,"project_id",invoiceDto.getProjectId())
                .eq("delete_flag", DeleteFlag.NO.ordinal())));
    }

    /**
     * 查询开票信息列表
     * @param invoiceDto 开票信息查询条件
     * @return R
     */
    @GetMapping("/listAll")
    @OperateLog(title = "查询开票信息列表")
    @ApiOperation("查询开票信息列表")
    public R listAll(InvoiceDto invoiceDto){
        if (invoiceDto.getContractId()!=null){
            return R.ok("查询成功", invoiceService.seleteInvoiceWithIdentifier(invoiceDto.getContractId()));
        }else {
            return R.ok("查询成功", invoiceService.list(new QueryWrapper<InvoiceEntity>()
                    .eq(invoiceDto.getId()!=null,"id",invoiceDto.getId())
                    .eq(invoiceDto.getContractId()!=null,"contract_id",invoiceDto.getContractId())
                    .eq(invoiceDto.getProjectId()!=null,"project_id",invoiceDto.getProjectId())
                    .eq("delete_flag", DeleteFlag.NO.ordinal())));
        }
    }

    /**
     * 新增开票信息
     * @param invoiceDto 新增的开票信息
     * @return R
     */
    @PostMapping("/insert")
    @OperateLog(title = "新增开票信息")
    @ApiOperation("新增开票信息")
    public R insert(@RequestBody InvoiceDto invoiceDto){
        try {
            return R.ok("新增开票信息"+invoiceService.saveAndCaculate(invoiceDto));
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }

    /**
     * 修改开票信息
     * @param invoiceDto 修改的开票信息
     * @return R
     */
    @PutMapping("/update")
    @OperateLog(title = "修改开票信息")
    @ApiOperation("修改开票信息")
    public R update(@RequestBody InvoiceDto invoiceDto){
        try {
            return R.ok("修改开票信息"+invoiceService.updateAndCaculate(invoiceDto));
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }

    /**
     * 删除开票信息
     * @param id 删除的开票信息id
     * @return R
     */
    @DeleteMapping("/delete")
    @OperateLog(title = "删除开票信息")
    @ApiOperation("删除开票信息")
    public R delete(Long id) {
        try {
            return R.ok("删除开票信息"+invoiceService.removeAndCaculate(id));
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }

}
