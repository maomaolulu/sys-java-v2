package com.ruoyi.system.roles.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author ruoyi
 */
@Data
@TableName("sys_user_role")
public class SysUserRole {

    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;



}
