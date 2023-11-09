package com.ruoyi.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 项目表vo
 * 
 * @author zhanghao
 * @email
 * @date 2023-05-24
 */
@Data
public class ProjectVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	private Long id;
	/**
	 * 合同id
	 */
	private Long contractId;
	/**
	 * 报价单编号
	 */
	private String quotationCode;
	/**
	 * 合同 单编号
	 */
	private String type;
	/**
	 * identifier
	 */
	private String identifier;
	/**
	 * 受检企业名称
	 */
	private String company;
	/**
	 * 受检详细地址
	 */
	private String officeAddress;

	/**
	 * 检测项目
	 */
	private String substances;

	/**
	 * 分包项目
	 */
	private String isSubcontracts;
	/**
	 * 应测点数
	 */
	private Integer shouldPoint;
	/**
	 * 实测点数
	 */
	private Integer point;

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

}
