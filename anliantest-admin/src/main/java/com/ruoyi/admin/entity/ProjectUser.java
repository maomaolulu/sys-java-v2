package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zhanghao
 * @email ''
 * @date 2023-05-19
 */
@Data
@TableName("ly_project_user")
public class ProjectUser implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 排单id
	 */
	private Long planId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 人员类型 1:报告调查人，2：调查复核人
	 */
	private Integer types;


}
