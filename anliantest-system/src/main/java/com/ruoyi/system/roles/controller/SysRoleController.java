package com.ruoyi.system.roles.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import com.ruoyi.common.utils.pageUtil;
import com.ruoyi.system.roles.entity.SysRole;
import com.ruoyi.system.roles.service.SysRoleMenuService;
import com.ruoyi.system.roles.service.SysRoleService;
import com.ruoyi.system.roles.vo.RoleMenuVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 角色控制层
 *
 * @author zh
 * @date 2023/4/28
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController  {


    private final SysRoleService roleService;
    private final SysRoleMenuService sysRoleMenuService;


    public SysRoleController(SysRoleService roleService, SysRoleMenuService sysRoleMenuService) {
        this.roleService = roleService;

        this.sysRoleMenuService = sysRoleMenuService;
    }

    /**
     * 角色已勾选菜单及按钮
     */
    @GetMapping("haveMenu")
    public R haveMenu(Long roleId){
        if(roleId==null){
            return R.error("角色id不能为空");
        }
        R r = roleService.haveMenu(roleId);


        return r;

    }

    /**
     * 分配权限
     */
    @OperateLog(title = "分配权限")
    @PostMapping("/saveRoleMenu")
    public R saveRoleMenu (@RequestBody RoleMenuVo roleMenuVo){

        roleService.saveRoleMenu( roleMenuVo.getRoleId() , roleMenuVo.getList());



        return R.ok("分配权限成功");


    }
    /**
     * 角色列表
     */
    @GetMapping("/listPage")
    public R listPage( SysRole role) {
        pageUtil.startPage();


        List<SysRole> list = roleService.selectRoleList(role);
        return R.resultData(list);
    }
    /**
     * 角色列表不分页(用于用户新增选择)
     */
    @GetMapping("/list")
    public R list() {
        List<SysRole> list = roleService.list(new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus,0));
        return R.data(list);
    }
    /**
     * 角色详情
     */
    @GetMapping("/info")
    public R info( SysRole role) {
        //授权角色  未授权角色 菜单全部（前端根据 type 拆分类型 或者做成map返回前端 修改接收的是已勾选的数据list  和前端沟通  ） 勾选的 字段标记
        List<SysRole> list = roleService.selectRoleList(role);
        return R.resultData(list);
    }



    /**
     * 新增角色
     */
    @PostMapping("/add")
    @OperateLog(title = "新增角色")
    public R add(@Validated @RequestBody SysRole sysRole) {
        // 新增 选择菜单关联   判断角色名称  判断角色（权限）标识
        R verify = verify(sysRole);
        //验证角色名称  判断角色（权限）标识 是否重复
        if(verify!=null){
            return verify;
        }
        roleService.saveSysRole(sysRole);

        return R.ok("新增成功");

    }

    /**
     * 修改保存角色
     */
    @OperateLog(title = "修改保存角色")
    @PostMapping("/update")
    public R update(@Validated @RequestBody SysRole sysRole) {
        // 新增 选择菜单关联   判断角色名称  判断角色（权限）标识
        R verify = verify(sysRole);
        //验证角色名称  判断角色（权限）标识 是否重复
        if(verify!=null){
            return verify;
        }
        roleService.updateSysRole(sysRole);

        return R.ok("修改成功");
    }



    /**
     * 状态修改
     */
    @PostMapping("/updateStatus")
    @OperateLog(title = "updateStatus")
    public R updateStatus(@Validated @RequestBody SysRole sysRole) {

        roleService.updateById(sysRole);
        return R.ok("修改成功");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/del")
    @OperateLog(title = "删除角色")
    public R remove(@RequestBody List<Long> roleIds) {

        roleService.deleteRoleByIds(roleIds);
        return R.ok("删除成功");
    }


    /**
     * 验证角色名称及角色权限是否重复
     * @return
     */
    private R  verify(SysRole sysRole){
        //角色名
        String roleName = sysRole.getRoleName();
        //角色唯一标识
        String roleKey = sysRole.getRoleKey();
        int count = roleService.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, roleName).ne(sysRole.getRoleId()!=null,SysRole::getRoleId,sysRole.getRoleId()));
        if(count>0){
            return R.error("角色名称不能重复");
        }
        int count1 = roleService.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, roleKey).ne(sysRole.getRoleId()!=null,SysRole::getRoleId,sysRole.getRoleId()));
        if(count1>0){
            return R.error("角色标识不能重复");
        }
        return null;
    }
}
