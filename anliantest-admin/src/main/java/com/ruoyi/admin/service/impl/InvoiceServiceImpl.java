package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.InvoiceDto;
import com.ruoyi.admin.domain.vo.InvoiceVo;
import com.ruoyi.admin.entity.InvoiceEntity;
import com.ruoyi.admin.entity.ProjectEntity;
import com.ruoyi.admin.entity.ReceiptRecordEntity;
import com.ruoyi.admin.mapper.InvoiceMapper;
import com.ruoyi.admin.service.InvoiceService;
import com.ruoyi.admin.service.ProjectService;
import com.ruoyi.admin.service.ReceiptRecordService;
import com.ruoyi.common.utils.ObjectUtils;
import com.ruoyi.system.login.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-08 9:21
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, InvoiceEntity> implements InvoiceService {

    @Lazy
    @Autowired
    private ReceiptRecordService receiptService;
    @Lazy
    @Autowired
    private ProjectService projectService;
    private final int one = 1;
    private final String success = "ok";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAndCaculate(InvoiceDto invoiceDto) throws Exception {
        InvoiceEntity invoiceEntity = ObjectUtils.transformObj(invoiceDto, InvoiceEntity.class);
        invoiceEntity.setCreateBy(ShiroUtils.getUserName());
        invoiceEntity.setCreateTime(new Date());
        this.save(invoiceEntity);
        String result = compareReceiptAndInvoice(invoiceDto.getProjectId());
        if (success.equals(result)){
            return result;
        }else {
            throw new Exception(result);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAndCaculate(InvoiceDto invoiceDto) throws Exception {
        InvoiceEntity invoiceEntity = ObjectUtils.transformObj(invoiceDto, InvoiceEntity.class);
        invoiceEntity.setUpdateBy(ShiroUtils.getUserName());
        invoiceEntity.setUpdateTime(new Date());
        this.updateById(invoiceEntity);
        String result = compareReceiptAndInvoice(invoiceDto.getProjectId());
        if (success.equals(result)){
            return result;
        }else {
            throw new Exception(result);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String removeAndCaculate(Long id) throws Exception{
        InvoiceEntity invoiceEntity = this.getOne(new QueryWrapper<InvoiceEntity>().eq("id", id));
        invoiceEntity.setUpdateBy(ShiroUtils.getUserName());
        invoiceEntity.setUpdateTime(new Date());
        invoiceEntity.setDeleteFlag(one);
        this.updateById(invoiceEntity);
        String result = compareReceiptAndInvoice(invoiceEntity.getProjectId());
        if (success.equals(result)){
            return result;
        }else {
            throw new Exception(result);
        }
    }

    @Override
    public List<InvoiceVo> seleteInvoiceWithIdentifier(Long contractId){
        return baseMapper.seleteInvoiceWithIdentifier(contractId);
    }

    private String compareReceiptAndInvoice(Long projectId){
        BigDecimal totalMoney = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", projectId)).getTotalMoney();
        int zero = 0;
        List<ReceiptRecordEntity> receipts = receiptService.list(new QueryWrapper<ReceiptRecordEntity>().eq("project_id", projectId).eq("del_flag", zero));
        List<InvoiceEntity> invoices = this.list(new QueryWrapper<InvoiceEntity>().eq("project_id", projectId).eq("delete_flag",zero));
        BigDecimal totalReceipt = BigDecimal.ZERO;
        BigDecimal totalInvoice = BigDecimal.ZERO;
        if (receipts.size()>0){
            totalReceipt = receipts.stream().map(ReceiptRecordEntity::getReceiptMoney).reduce(BigDecimal.ZERO,BigDecimal::add);
        }
        if (invoices.size()>0){
            totalInvoice = invoices.stream().map(InvoiceEntity::getInvoiceMoney).reduce(BigDecimal.ZERO,BigDecimal::add);
        }
        if (totalReceipt.compareTo(totalMoney) > zero){
            return "总回款金额高于项目总金额!";
        }
        if (totalInvoice.compareTo(totalReceipt) > zero){
            return "总开票金额高于回款总金额!";
        }
        return success;
    }
}
