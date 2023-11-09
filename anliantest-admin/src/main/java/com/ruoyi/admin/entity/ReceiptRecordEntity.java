package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ZhuYiCheng
 * @date 2023/6/7 17:51
 */
@Data
@TableName("ly_receipt_record")
public class ReceiptRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目编号
     */
    @TableField(exist = false)
    private String projectIdentifier;
    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 回款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiptTime;
    /**
     * 回款金额
     */
    private BigDecimal receiptMoney;
    /**
     * 结算方式（1现金、2对公账号、3承兑汇票）
     */
    private Integer paymentMethod;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 逻辑删除;0否，1是)
     */
    private Integer delFlag;
}
