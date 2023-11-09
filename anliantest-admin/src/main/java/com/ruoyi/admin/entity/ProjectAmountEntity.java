package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ZhuYiCheng
 * @date 2023/4/7 11:10
 */
@Data
@TableName("ly_project_amount")
public class ProjectAmountEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 合同ID
     */
    private Long contractId;
    /**
     * 项目金额(元)
     */
    private BigDecimal totalMoney;
    /**
     * 已收款金额(元)
     */
    private BigDecimal receiptMoney;
    /**
     * 未结算金额(元)
     */
    private BigDecimal nosettlementMoney;
    /**
     * 佣金金额(元)(业务费)
     */
    private BigDecimal commission;
    /**
     * 佣金比例,佣金/总金额
     */
    private BigDecimal commissionRatio;
    /**
     * 佣金未结算金额(元)
     */
    private BigDecimal commissionOutstanding;
    /**
     * 评审费(元)
     */
    private BigDecimal evaluationFee;
    /**
     * 未结算评审费(元)
     */
    private BigDecimal evaluationOutstanding;
    /**
     * 分包费(元)
     */
    private BigDecimal subprojectFee;
    /**
     * 未结算分包费(元)
     */
    private BigDecimal subprojectOutstanding;
    /**
     * 服务费用(元)
     */
    private BigDecimal serviceCharge;
    /**
     * 未结算服务费用(元)
     */
    private BigDecimal serviceChargeOutstanding;
    /**
     * 其他支出(元)
     */
    private BigDecimal otherExpenses;
    /**
     * 未结算的其他支出(元)
     */
    private BigDecimal otherExpensesOutstanding;
    /**
     * 已开票金额(元)
     */
    private BigDecimal invoiceMoney;
    /**
     * 项目净值(元)
     */
    private BigDecimal netvalue;
    /**
     * 虚拟税费(元)
     */
    private BigDecimal virtualTax;
    /**
     * 已收款比例
     */
    private BigDecimal receiptMoneyRate;
    /**
     * 已开票比例
     */
    private BigDecimal invoiceMoneyRate;
}
