package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.system.file.domain.SysAttachment;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/31 10:46
 */
@Data
@TableName("ly_contract")
public class ContractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;

    /**
     * 合同编号
     */
    private String identifier;

    /**
     * 受检企业信息表ID
     */

    private Long companyId;

    /**
     * 受检企业名称
     */
    private String company;

    /**
     * 委托单位，企业信息表ID
     */
    private Long entrustCompanyId;

    /**
     * 委托单位名称
     */
    private String entrustCompany;

    /**
     * 委托单位详细地址
     */
    private String entrustOfficeAddress;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String area;

    /**
     * 受检详细地址
     */
    private String officeAddress;

    /**
     * 法定代表人
     */
    private String legalName;

    /**
     * 固话
     */
    private String telephone;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 委托类型
     */
    private String entrustType;

    /**
     * 合同类型
     */
    private String type;

    /**
     * 合同模版ID
     */
    private Long contractTemplateId;

    /**
     * 合同状态（0录入，1签订）
     */
    private Integer status;

    /**
     * 合同签订状态(0未签订，1已签订)
     */
    private Integer contractStatus;

    /**
     *  合同评审状态（0待评审,1评审中,2评审通过,3评审未通过,4撤销审核）
     */
    private Integer reviewStatus;

    /**
     * 履约状态（0待履约,1履约中,2履约完成）
     * 履约状态（0待履约,1履约中,2履约完成,3合同终止）
     */
    private Integer performStatus;

    /**
     * 协议签订状态(0未签订，1已签订)
     */
    private Integer dealStatus;


    /**
     * 终止原因
     */
    private String terminateReason;

    /**
     * 终止时间
     */
    private Date terminateTime;

    /**
     * 合同隶属公司
     */
    private String companyOrder;

    /**
     * 杭州隶属(业务来源)
     */
    private String businessSource;

    /**
     * 关联项目数量
     */
    private Integer projectQuantity;

    /**
     * 业务员ID
     */
    private Integer salesmenid;

    /**
     * 业务员
     */
    private String salesmen;

    /**
     * 合同金额(元)
     */
    private BigDecimal totalMoney;

    /**
     * 佣金(元)
     */
    private BigDecimal commission;

    /**
     * 评审费(元)
     */
    private BigDecimal evaluationFee;

    /**
     * 分包费(元)
     */
    private BigDecimal subcontractFee;

    /**
     * 服务费用(元)
     */
    private BigDecimal serviceCharge;

    /**
     * 其他支出(元)
     */
    private BigDecimal otherExpenses;

    /**
     * 合同净值(元)
     */
    private BigDecimal netvalue;

    /**
     * 委托日期
     */
    private Date commissionDate;

    /**
     * 合同签订日期
     */
    private Date signDate;

    /**
     * 加急状态(0正常，1较急、2加急)
     */
    private Integer urgent;

    /**
     * 新老业务(0新业务，1续签业务)
     */
    private Integer old;

    /**
     * 新老业务(0新业务，1续签业务)备注
     */
    private String remarks;

    /**
     * 录入人ID
     */
    private Integer userid;

    /**
     * 录入人姓名
     */
    private String username;

    /**
     * 数据入库时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;
    /**
     * 逻辑删除（默认为0,1删除）
     */
    private int delFlag;
    /**
     * 合同对应附件列表
     */
    @TableField(exist = false)
    private List<SysAttachment> sysAttachmentList;
    /**
     * 回款进度
     */
    @TableField(exist = false)
    private BigDecimal receiptMoneyRate;
    /**
     * 开票进度
     */
    @TableField(exist = false)
    private BigDecimal invoiceMoneyRate;

}
