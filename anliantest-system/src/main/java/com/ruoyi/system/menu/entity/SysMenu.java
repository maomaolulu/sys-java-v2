package com.ruoyi.system.menu.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 * 
 * @author zh
 * @date 2023-04-27
 */
@TableName("sys_menu")
@Data
public class SysMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@TableId
	private Long menuId;
	/**
	 * 父菜单ID，一级菜单为0
	 */
	private Long parentId;
	/**
	 * 父菜单名称
	 */
	@TableField(exist = false)
	private String parentName;
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 菜单URL(路由地址)
	 */
	private String url;
	/**
	 * 权限标识
	 */
	private String perms;
	/**
	 * 类型 1：菜单   2：按钮   3：职能
	 */
	private Integer type;
//	/**
//	 * 权限验证码(职能相关 )
//	 */
//	private String authCode;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 系统名称 sys；pj；zj；
	 */
	private String moduleName;
	/**
	 * 系统名称 sys 运营系统 ；pj  检评系统；zj  评价系统；
	 */
	private String moduleType;

	/**
	 * 显示状态（0显示 1隐藏）
	 */
	private String visible;

//	/**
//	 * 菜单状态（0显示 1隐藏）
//	 */
//	private String status;

	@TableField(exist = false)
	private List<SysMenu> children ;



	

}
