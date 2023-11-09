package com.ruoyi.system.login.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.system.menu.entity.SysMenu;

import lombok.Data;

import java.util.List;
@Data
public class UserVo {

    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 性别（1男，2女）
     */
    private Integer sex;

    /**
     * 工号
     */
    private String jobNum;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 隶属公司
     */
    private String subjection;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 部门
     */
    private String dept;
    /**
     * 岗位
     */
    private String post;
    /**
     * 角色
     */
    private String role;


    /**
     * 状态; 0：禁用   1：正常  2:离职
     */
    private Integer status;

    /**
     * 权限
     */
    @TableField(exist=false)
    private List<String> permissions;
    /**
     * 菜单列表
     */
    @TableField(exist=false)
    private List<SysMenu> menuList;
}
