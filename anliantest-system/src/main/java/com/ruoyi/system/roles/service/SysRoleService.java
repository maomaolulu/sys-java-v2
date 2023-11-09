package com.ruoyi.system.roles.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.menu.entity.SysMenu;
import com.ruoyi.system.roles.entity.SysRole;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 角色接口层
 *
 * @author zh
 * @date 2023/4/28
 */
public interface SysRoleService extends IService<SysRole> {
   /**
    * list
    * @param sysRole
    * @return
    */
   List<SysRole> selectRoleList(SysRole sysRole);

   /**
    * 新增
    */
   SysRole saveSysRole(SysRole sysRole);

   /**
    * 修改
    */
   SysRole updateSysRole(SysRole sysRole);

   /**
    * 删除
    */
   void deleteRoleByIds( List<Long> roleIds);

   /**
    * 个人权限
    */
   Set<String> roleByUser(Long userId);

   /**
    * 角色及菜单
    * @param userId
    * @return
    */
   public R roleAndMenuByUser(Long userId);

   /**
    * 根据用户查询角色id
    * @param userId
    * @return
    */
   List<Long> roleIdsByUser(Long userId);
   /**
    * 角色已勾选菜单及按钮
    * @return
    */
   R haveMenu(Long roleId);

   /**
    * 分配权限
    * @param roleId
    * @param list
    */
  void saveRoleMenu(Long roleId ,List<Long> list);
}
