package com.ruoyi.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gy
 * @date 2023-06-08 17:56
 */
@Data
public class InvoiceDto implements Serializable {
    /**
     *  id
     */
    private Long id;
    /**
     *  项目ID
     */
    private Long projectId;
    /**
     *  合同ID
     */
    private Long contractId;
    /**
     *  开票时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date invoiceTime;
    /**
     *  开票类型
     */
    private Integer invoiceType;
    /**
     *  发票号码
     */
    private String invoiceNumber;
    /**
     *  开票金额
     */
    private BigDecimal invoiceMoney;
    /**
     *  备注
     */
    private String remark;
}
