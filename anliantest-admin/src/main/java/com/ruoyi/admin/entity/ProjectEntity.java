package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 项目表(包含了原任务表的字段)
 * 
 * @author LiXin
 * @email
 * @date 2022-01-10 14:43:43
 */
@Data
@TableName("ly_project")
public class ProjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目编号
	 */
	private String identifier;
	/**
	 * 合同ID
	 */
	private Long contractId;
	/**
	 * 合同编号
	 */
	private String contractIdentifier;
	/**
	 * 受检企业信息表ID
	 */
	private Long companyId;
	/**
	 * 受检企业名称
	 */
	private String company;
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
	 * 项目状态(0:未启动，1：已启动，2：已完成，3：已结束 4：终止 5：挂起/暂停)
	 */
	private Integer status;
	/**
	 * 终止原因
	 */
	private String terminateReason;
	/**
	 * 终止时间
	 */
	private Date terminateTime;
	/**
	 * 合同签订状态（0未签订，1已签订）
	 */
	private Integer contractSignStatus;
	/**
	 * 合同签订日期
	 */
	private Date contractSignDate;

	/**
	 * 项目类型
	 */
	private String type;

	/**
	 * 检测类型（定期检测，监督检测，日常检测，其他）
	 */
	private String detectionType;
	/**
	 * 所属部门ID
	 */
	private Long deptId;
	/**
	 * 负责人id
	 */
	private Long chargeId;
	/**
	 * 负责人
	 */
	private String charge;
	/**
	 * 负责人工号
	 */
	private String chargeJobNum;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 联系电话
	 */
	private String telephone;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 项目报价单id
	 */
	private Long quotationId;
	/**
	 * 报价单编号
	 */
	private String quotationCode;
	/**
	 * 委托类型
	 */
	private String entrustType;
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
	 * 项目金额(元)
	 */
	private BigDecimal totalMoney;
	/**
	 * 项目净值(元)
	 */
	private BigDecimal netvalue;
	/**
	 * 备注
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
     * 佣金金额,业务费(元)
     */
    @TableField(exist=false)
    private BigDecimal commission;
    /**
     * 佣金比例,佣金/总金额
     */
    @TableField(exist=false)
    private BigDecimal commissionRatio;
    /**
     * 评审费(元)
     */
    @TableField(exist=false)
    private BigDecimal evaluationFee;
    /**
     * 分包费(元)
     */
    @TableField(exist=false)
    private BigDecimal subprojectFee;
    /**
     * 服务费用(元)
     */
    @TableField(exist=false)
    private BigDecimal serviceCharge;
    /**
     * 其他支出(元)
     */
    @TableField(exist=false)
    private BigDecimal otherExpenses;

    /**
     * 委托日期
     */
    @TableField(exist=false)
    private Date entrustDate;
    /**
     * 签订日期
     */
    @TableField(exist=false)
    private Date signDate;
	/**
	 * 项目待回款额
	 */
	@TableField(exist = false)
	private BigDecimal receiptOutstanding;
	/**
	 * 项目待开票额
	 */
	@TableField(exist = false)
	private BigDecimal invoiceMoneyNotready;

}
