package com.ruoyi.system.roles.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenuVo {
    /** 角色id*/
    private Long roleId;
    /** 已选菜单id*/
    private List<Long> list;
}
