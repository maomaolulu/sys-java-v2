package com.ruoyi.system.menu.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.constant.Constant;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.login.utils.ShiroUtils;
import com.ruoyi.system.menu.entity.SysMenu;
import com.ruoyi.system.menu.service.SysMenuService;
import com.ruoyi.system.roles.service.SysRoleMenuService;
import com.ruoyi.system.roles.service.SysRoleService;
import com.ruoyi.system.roles.util.MenuTree;
import com.ruoyi.system.user.entity.UserEntity;
import com.ruoyi.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 系统菜单
 * 
 * @author zh
 * @date 2023-04-27
 */
@RestController
@Api(tags="系统菜单")
@RequestMapping("/sys/menu")
public class    SysMenuController  {
	private final SysMenuService sysMenuService;
	private final SysRoleService sysRoleService;
	private final UserService userService;
	private final SysRoleMenuService sysRoleMenuService;

	@Autowired
	public SysMenuController(SysMenuService sysMenuService, SysRoleService sysRoleService, UserService userService, SysRoleMenuService sysRoleMenuService) {
		this.sysMenuService = sysMenuService;
		this.sysRoleService = sysRoleService;
		this.userService = userService;
		this.sysRoleMenuService = sysRoleMenuService;
	}


	/**
	 * 导航菜单
	 */
	@GetMapping("/list")
	@ApiOperation("导航菜单")
	public R list( SysMenu sysMenu){
		List<SysMenu> list = sysMenuService.list(
				new LambdaQueryWrapper<SysMenu>()
						.like(StrUtil.isNotBlank(sysMenu.getName()),SysMenu::getName,sysMenu.getName())
						.like(StrUtil.isNotBlank(sysMenu.getVisible()),SysMenu::getVisible,sysMenu.getVisible())
						.like(StrUtil.isNotBlank(sysMenu.getPerms()),SysMenu::getPerms,sysMenu.getPerms())
						.eq(sysMenu.getType()!=null,SysMenu::getType,sysMenu.getType())
						.eq(SysMenu::getModuleName,sysMenu.getModuleName())
		);
		MenuTree menuTree = new MenuTree(list);
		List<SysMenu> sysMenus = menuTree.builTree();

		return R.data(sysMenus);

	}


	/**
	 * 保存
	 */
	@PostMapping("/save")
	@ApiOperation("保存菜单")
	@OperateLog(title = "保存菜单")
	public R save(@RequestBody SysMenu menu){
		//数据校验
		R r = verifyForm(menu);
		if(r!=null){
			return r;
		}

		sysMenuService.save(menu);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperation("修改菜单")
	@OperateLog(title = "修改菜单")
	public R update(@RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);

		sysMenuService.updateById(menu);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete/{menuId}")
	@ApiOperation("单条删除菜单")
	@OperateLog(title = "单条删除菜单")
	public R delete(@PathVariable("menuId") long menuId){
//		if(menuId <= 31){
//			return R.error("系统菜单，不能删除");
//		}

		//判断是否有子菜单或按钮
		List<SysMenu> menuList = sysMenuService.queryListParentId(menuId);


		if(menuList.size() > 0){
			return R.error("请先删除子菜单或按钮");
		}

		sysMenuService.delete(menuId);

		return R.ok();
	}
	/**
	 * 验证参数是否正确
	 */
	private R verifyForm(SysMenu menu){

		if(StrUtil.isBlank(menu.getName())){
			return R.error("菜单名称不能为空");
		}

		if(menu.getParentId() == null){
			return R.error("上级菜单不能为空");
		}

		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				return R.error("菜单URL不能为空");
			}
		}

		//上级菜单类型
		int parentType = 0;
		if(menu.getParentId() != 0){
			SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
			parentType = parentMenu.getType();
		}


		//按钮
		if(menu.getType() == 2){
			if(parentType != 1){
				return R.error("上级菜单只能为菜单类型");
			}
		}
		return null;
	}


	/**
	 * 导航菜单
	 */
	@GetMapping("/info")
	@ApiOperation("用户信息")
	public R info() {
		//个人权限
		R r = sysRoleService.roleAndMenuByUser(ShiroUtils.getUserId());
		// TODO: 2023-05-09 select
		UserEntity user = userService.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUserId, ShiroUtils.getUserId()));
		user.setPassword(null);
		user.setSalt(null);
		r.put("user", user);

		return r;
	}

}
