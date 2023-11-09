package com.ruoyi.system.menu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.menu.entity.SysMenu;
import com.ruoyi.system.menu.mapper.SysMenuMapper;
import com.ruoyi.system.menu.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
	@Override
	public List<SysMenu> queryListParentId(Long parentId) {
		List<SysMenu> list = this.list(new LambdaQueryWrapper<SysMenu>()
				.eq(SysMenu::getParentId, parentId)
		);

		return list;
	}
	@Override
	public void delete(Long menuId){
		//删除菜单
		this.removeById(menuId);
		//删除菜单与角色关联
//		sysRoleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
	}
}
