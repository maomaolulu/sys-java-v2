package com.ruoyi.system.roles.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.roles.entity.SysUserRole;
import com.ruoyi.system.roles.mapper.SysUserRoleMapper;
import com.ruoyi.system.roles.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联
 *
 * @author zh
 * @date 2023/4/28
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {


    public void ces(){
        this.list();
    }
}
