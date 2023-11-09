package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.deepoove.poi.data.RowRenderData;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/20 10:00
 */
@Data
@TableName("ly_quotation")
public class QuotationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 报价单编号
     */
    private String code;
    /**
     * 客户id
     */
    private Long companyId;
    /**
     * 客户名称
     */
    private String company;
    /**
     * 合同id
     */
    private Long contractId;
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
     * 检测地址
     */
    private String address;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 报价类型
     */
    private String quotationType;
    /**
     * 报价单状态 1，审核中
     *   2， 审核失败
     *   3， 报价中
     *   4， 赢单
     *   5，丢单
     *   6，撤销审核
     */
    private Integer status;

    /**
     * 是否走审批
     */
    @TableField(exist = false)
    private Boolean isExamine;
    /**
     * 报价日期
     */
    private Date quotationDate;
    /**
     * 检测费
     */
    private BigDecimal detectFee;
    /**
     * 报告编制费
     */
    private BigDecimal reportFee;
    /**
     * 人工出车费
     */
    private BigDecimal trafficFee;
    /**
     * 总价
     */
    private BigDecimal totalMoney;
    /**
     * 业务费
     */
    private BigDecimal commission;
    /**
     * 优惠价
     */
    private BigDecimal preferentialFee;
    /**
     * 分包费
     */
    private BigDecimal subprojectFee;
    /**
     * 评审费
     */
    private BigDecimal evaluationFee;
    /**
     * 净值
     */
    private BigDecimal netvalue;
    /**
     * 服务费
     */
    private BigDecimal serviceCharge;
    /**
     * 其他费用
     */
    private BigDecimal otherExpenses;
    /**
     * 折扣（%）
     */
    private BigDecimal discount;
    /**
     * 业务员id
     */
    private Long salesmenId;
    /**
     * 业务员
     */
    private String salesmen;
    /**
     * 业务员联系电话
     */
    private String salesmenContact;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 逻辑删除（默认为0,1删除）
     */
    private int delFlag;
    /**
     * 创建人id
     */
    private Long createById;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 报价详情
     */
    @TableField(exist = false)
    private List<QuotationInfoEntity> quotationInfos;
    /**
     * 报价导出
     */
    @TableField(exist = false)
    private List<RowRenderData> quotationTable;

    /**
     * 评审原因
     */
    @TableField(exist = false)
    private String reviewReason;

}
