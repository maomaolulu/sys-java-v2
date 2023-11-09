package com.ruoyi.system.roles.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author ruoyi
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu {
    @TableId
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;


}
