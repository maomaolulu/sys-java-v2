package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhanghao
 * @email
 * @date 2023-06-19
 * @desc : 项目排单表
 */
@Data
@TableName("ly_project_plan")
@Accessors(chain = true) //开启链式编程写法
public class ProjectPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	/** 主键ID */
	private Long id ;
	/** 项目id */
	private Long projectId ;
	/** 排单次数 */
	private Integer planNumber ;
	/** 状态 0：未派单 1：待确认 2：已确认   */
	private Integer planStatus ;
	/** 排单日期 */
	private Date planDate ;
	/** 调查日期 */
	private Date planSurveyDate ;
	/** 报告调查日期(报告上展示) */
	private Date reportSurveyDate ;
	/** 报告调查人 */
	private String reportSurveyUser ;
	/** 报告调查人id */
	private Long reportSurveyUserId ;
	/** 调查复核人 */
	private String checkUser ;
	/** 调查复核人id */
	private Long checkUserId ;
	/** 创建人id */
	private Long createById ;
	/** 创建人名称 */
	private String createBy ;
	/** 创建时间(下发日期/申请日期)) */
	private Date createTime ;
	/** 更新人 */
	private String updateBy ;
	/** 更新时间 */
	private Date updateTime ;

}