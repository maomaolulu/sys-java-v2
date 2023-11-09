package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 项目记录
 * 
 * @author zhanghao
 * @email
 * @date 2023-05-18
 */
@Data
@TableName("ly_project_record")
public class ProjectRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id ;
	/** 项目id */
	private Long projectId ;
	/**类型 0：下发 1：调查排单 */
	private Integer type ;
	/**类型 0：下发 1：调查排单 */
	private Integer subType ;
	/** 标题 */
	private String title ;
	/** 调查人id */
	private Long chargeId ;
	/** 调查人名称 */
	private String charge ;

	/** 备注 */
	private String remark ;
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
	/**
	 * 记录详情
	 */
	private String planDetails;

}
