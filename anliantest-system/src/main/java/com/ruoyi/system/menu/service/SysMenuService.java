package com.ruoyi.system.menu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.menu.entity.SysMenu;

import java.util.List;


/**
 * 菜单管理
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
public interface SysMenuService extends IService<SysMenu> {



	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenu> queryListParentId(Long parentId);
	/**
	 * 删除
	 */
	void delete(Long menuId);


}
