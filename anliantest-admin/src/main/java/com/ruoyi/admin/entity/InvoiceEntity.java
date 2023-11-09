package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gy
 * @date 2023-06-07 17:54
 */
@Data
@TableName("ly_invoice_record")
@Accessors(chain = true)
public class InvoiceEntity extends BaseEntity implements Serializable {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
