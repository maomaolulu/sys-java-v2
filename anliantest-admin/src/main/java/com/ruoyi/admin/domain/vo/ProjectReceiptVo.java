package com.ruoyi.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.admin.entity.ReceiptRecordEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/6/8 15:51
 */
@Data
public class ProjectReceiptVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    private Long id;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 合同编号
     */
    private String contractIdentifier;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 项目隶属公司
     */
    private String companyOrder;
    /**
     * 杭州隶属(业务来源)
     */
    private String businessSource;
    /**
     * 业务员ID
     */
    private Long salesmenid;
    /**
     * 业务员
     */
    private String salesmen;
    /**
     * 负责人id
     */
    private Long chargeId;
    /**
     * 负责人
     */
    private String charge;
    /**
     * 合同签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date contractSignDate;
    /**
     * 项目金额(元)
     */
    private BigDecimal totalMoney;
    /**
     * 已收款金额(元)
     */
    private BigDecimal receiptMoney;
    /**
     * 未回款金额(元)
     */
    private BigDecimal receiptOutstanding;
    /**
     * 已开票金额(元)
     */
    private BigDecimal invoiceMoney;
    /**
     * 已回款比例
     */
    private BigDecimal receiptMoneyRate;
    /**
     * 项目回款记录列表
     */
    private List<ReceiptRecordEntity> receiptEntityList;
}
