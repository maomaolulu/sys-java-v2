package com.ruoyi.admin.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.admin.entity.InvoiceEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-08 11:35
 */
@Data
public class ProjectInvoiceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    private Integer id;
    /**
     * 合同id
     */
    private Integer contractId;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 企业名称
     */
    private String company;
    /**
     * 合同编号
     */
    private String contractIdentifier;
    /**
     * 合同隶属
     */
    private String companyOrder;
    /**
     * 业务来源
     */
    private String businessSource;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 业务员
     */
    private String salesmen;
    /**
     * 项目负责人
     */
    private String charge;
    /**
     * 合同签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date contractSignDate;
    /**
     * 项目金额
     */
    private BigDecimal totalMoney;
    /**
     * 已开票
     */
    private BigDecimal invoiceMoney;
    /**
     * 未开票(项目总金额-已开票金额)
     */
    private BigDecimal invoiceMoneyNotready;
    /**
     * 剩余可开票金额(已收款金额-已开票金额)
     */
    @TableField(exist = false)
    private BigDecimal invoiceMoneyready;
    /**
     * 开票进度
     */
    private BigDecimal invoiceMoneyRate;
    /**
     * 存储相关开票信息
     */
    @TableField(exist = false)
    private List<InvoiceEntity> invoiceEntityList;
}
