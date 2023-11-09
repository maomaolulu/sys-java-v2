package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.ProjectReceiptDto;
import com.ruoyi.admin.entity.InvoiceEntity;
import com.ruoyi.admin.entity.ProjectEntity;
import com.ruoyi.admin.entity.ReceiptRecordEntity;
import com.ruoyi.admin.mapper.ReceiptRecordMapper;
import com.ruoyi.admin.service.InvoiceService;
import com.ruoyi.admin.service.ProjectService;
import com.ruoyi.admin.service.ReceiptRecordService;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.enums.Numbers;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.login.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/6/8 15:35
 */
@Service
public class ReceiptRecordServiceImpl extends ServiceImpl<ReceiptRecordMapper, ReceiptRecordEntity> implements ReceiptRecordService {
    @Lazy
    @Autowired
    private ProjectService projectService;
    @Lazy
    @Autowired
    private InvoiceService invoiceService;
    private final String success = "操作成功！";

    /**
     * 新增回款记录
     *
     * @param receiptRecordEntity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addRecord(ReceiptRecordEntity receiptRecordEntity) throws Exception {

        Date nowDate = DateUtils.getNowDate();
        String userName = ShiroUtils.getUserName();
        receiptRecordEntity.setCreateTime(nowDate);
        receiptRecordEntity.setCreateBy(userName);
        receiptRecordEntity.setUpdateTime(nowDate);
        receiptRecordEntity.setUpdateBy(userName);
        //保存汇款记录
        boolean b = this.save(receiptRecordEntity);
        //修改项目金额表信息
        String result = this.compareReceiptAndInvoice(receiptRecordEntity.getProjectId());
        if (success.equals(result)) {
            return result;
        } else {
            throw new Exception(result);
        }
    }


    /**
     * 获取回款记录列表
     *
     * @param receiptDto
     * @return
     */
    @Override
    public List<ReceiptRecordEntity> getList(ProjectReceiptDto receiptDto) {

        return baseMapper.getList(new QueryWrapper<ReceiptRecordEntity>()
                .eq(receiptDto.getProjectId() != null, "r.project_id", receiptDto.getProjectId())
                .eq(receiptDto.getContractId() != null, "r.contract_id", receiptDto.getContractId())
                .eq("r.del_flag", DeleteFlag.NO.ordinal()));
    }


    /**
     * 修改回款记录
     *
     * @param receiptRecordEntity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateRecord(ReceiptRecordEntity receiptRecordEntity) throws Exception {
        Date nowDate = DateUtils.getNowDate();
        String userName = ShiroUtils.getUserName();
        receiptRecordEntity.setUpdateTime(nowDate);
        receiptRecordEntity.setUpdateBy(userName);
        //保存汇款记录
        boolean b = this.updateById(receiptRecordEntity);
        //修改项目金额表信息
        String result = this.compareReceiptAndInvoice(receiptRecordEntity.getProjectId());
        if (success.equals(result)) {
            return result;
        } else {
            throw new Exception(result);
        }
    }


    /**
     * 逻辑删除回款记录
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteRecord(Long id) throws Exception {
        ReceiptRecordEntity receiptRecordEntity = this.getById(id);

        Date nowDate = DateUtils.getNowDate();
        String userName = ShiroUtils.getUserName();
        receiptRecordEntity.setUpdateTime(nowDate);
        receiptRecordEntity.setUpdateBy(userName);
        receiptRecordEntity.setDelFlag(DeleteFlag.YES.ordinal());

        boolean b = this.updateById(receiptRecordEntity);
        //检查此操作是否不符合规范
        String result = this.compareReceiptAndInvoice(receiptRecordEntity.getProjectId());
        if (success.equals(result)) {
            return result;
        } else {
            throw new Exception(result);
        }
    }

    /**
     * 操作是否符合规范
     *
     * @param projectId
     * @return
     */
    private String compareReceiptAndInvoice(Long projectId) {
        BigDecimal totalMoney = projectService.getOne(new QueryWrapper<ProjectEntity>().eq("id", projectId)).getTotalMoney();
        int zero = 0;
        List<ReceiptRecordEntity> receipts = this.list(new QueryWrapper<ReceiptRecordEntity>().eq("project_id", projectId).eq("del_flag", zero));
        List<InvoiceEntity> invoices = invoiceService.list(new QueryWrapper<InvoiceEntity>().eq("project_id", projectId));
        BigDecimal totalReceipt = BigDecimal.ZERO;
        BigDecimal totalInvoice = BigDecimal.ZERO;
        if (receipts.size() > 0) {
            totalReceipt = receipts.stream().map(ReceiptRecordEntity::getReceiptMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (invoices.size() > 0) {
            totalInvoice = invoices.stream().map(InvoiceEntity::getInvoiceMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        //如果项目总回款额=项目总额，项目状态流转为已结束
        if(totalReceipt.compareTo(totalMoney)== zero){
            boolean b = projectService.update(new UpdateWrapper<ProjectEntity>().eq("id", projectId).set("status", Numbers.THREE.ordinal()));
        }

        if (totalReceipt.compareTo(totalMoney) > zero) {
            return "操作失败，已回款额不可大于项目总额!";
        }
        if (totalInvoice.compareTo(totalReceipt) > zero) {
            return "操作失败，已回款额不可小于已开票额!";
        }
        return success;
    }



}
