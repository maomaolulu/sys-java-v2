package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhanghao
 * @email
 * @date 2023-06-09
 * @desc : 车间点位
 */
@Data
@TableName("ly_point")
public class Point implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long  id ;
	/** 项目 */
	private Long projectId ;
	/** 排单id */
	private Long planId ;
	/** 车间岗位id */
	private Long workshopId ;
	/** 点位 */
	private String point ;
	/** 点位编号数字 */
	private Integer codeNumber ;
	/** 点位编号 */
	private String spotCode ;
	/** 逻辑删 */
	private Integer delFlag ;
	/** 创建人id */
	private Long createById ;
	/** 创建人名称 */
	private String createBy ;
	/** 创建时间 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime ;
	/** 更新人 */
	private String updateBy ;
	/** 更新时间 */
	private Date updateTime ;

}