package com.ruoyi.system.roles.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.system.roles.vo.SysUserVo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色实体
 *
 * @author hjy
 * @date 2023/3/31 10:58
 */
@Data
@TableName("sys_role")
public class SysRole  {
    /**
     * 角色ID
     */
    @TableId
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;

    /**
     * 角色排序
     */
    private String roleSort;


    /**
     * 角色状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private Integer delFlag;
    /**
     * 类型：运营系统 检评系统 评价系统
     */
    private String type;
    /**
     * 描述
     */
    private String description;

    /**
     * 禁止删除（0：不禁止，1：禁止）
     */
    private Integer prohibit ;

//    /**
//     * 菜单组
//     */
//    @TableField(exist = false)
//    private List<Long> menuIds;

    /**
     * 菜单组
     */
    @TableField(exist = false)
    private List<SysRoleMenu> sysRoleMenuList;

    /**
     * 用户组
     */
    @TableField(exist = false)
    private List<SysUserVo> sysUserVoList;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}