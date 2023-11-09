package com.ruoyi.system.roles.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.system.menu.entity.SysMenu;
import com.ruoyi.system.roles.entity.SysRole;
import com.ruoyi.system.roles.vo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 角色数据处理层
 *
 * @author hjy
 * @date 2023/3/31 9:19
 */
@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("SELECT ur.role_id,su.username,ur.user_id FROM `sys_user_role`  ur  left join  sys_user su on ur.user_id=su.user_id  ${ew.customSqlSegment} ")
    List<SysUserVo> selectRoleUsers(@Param(Constants.WRAPPER) QueryWrapper wrapper);
    //当前角色权限
    @Select("SELECT sr.role_key,sr.role_id FROM `sys_role`  sr " +
            "left join sys_user_role   ur ON sr.role_id=ur.role_id  " +
            "${ew.customSqlSegment}  ")
    List<SysRole> sysRolesByUser(@Param(Constants.WRAPPER) QueryWrapper wrapper);
    //根据用户id查询角色
    @Select("SELECT  sr.role_id  FROM `sys_role`  sr  " +
            "left join sys_user_role   ur ON sr.role_id=ur.role_id " +
            " where sr.`status`=0 and ur.user_id=#{userId }  "  )
    List<Long> roleIdsByUser(Long userId);

    //当前角色按钮权限 button

    //当前角色权限
    @Select("select DISTINCT sm.* from sys_menu sm " +
            "left join sys_role_menu rm on sm.menu_id=rm.menu_id " +
            "${ew.customSqlSegment}   ")
    List<SysMenu> sysMenuByRoles(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    //根据roleId 和 moduleType 查询有的menuIds(显示的)只查询菜单
    @Select("select sm.menu_id from sys_menu sm " +
            "left join sys_role_menu rm on sm.menu_id=rm.menu_id " +
            " where  sm.module_type= #{moduleType } and rm.role_id= #{ roleId } and sm.visible=0  and sm.type=1 ")
   List<Long> menuIdsByRoleAndmoduleType(@Param("roleId") Long roleId,@Param("moduleType") String moduleType);

    //根据roleId 和 moduleType 查询有的menuIds（不显示的）及  按钮
    @Select("select sm.menu_id from sys_menu sm " +
            "left join sys_role_menu rm on sm.menu_id=rm.menu_id " +
            " where  sm.module_type= #{moduleType } and rm.role_id= #{ roleId } and (sm.visible=1 || sm.type=2)  ")
   List<Long> menuIdsByRoleAndmoduleTypeVisible(@Param("roleId") Long roleId,@Param("moduleType") String moduleType);

}
