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
 * @date 2023-06-08
 * @desc : 项目车间岗位
 */
@Data
@TableName("ly_project_workshop")
public class ProjectWorkshop implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id ;
	/** 项目id */
	private Long projectId ;
	/** 任务id（派单id） */
	private Long planId ;
	/** 父级id */
	private Long pid ;
	/** 评价：0:建筑，1：车间 ，2：岗位   */
	private String type ;
	/** 名称 */
	private String name ;

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