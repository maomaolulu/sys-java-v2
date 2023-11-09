package com.ruoyi.system.roles.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.roles.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 用户角色关系处理
 *
 * @author hjy
 * @date 2023/3/31 16:08
 */
@Mapper
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
