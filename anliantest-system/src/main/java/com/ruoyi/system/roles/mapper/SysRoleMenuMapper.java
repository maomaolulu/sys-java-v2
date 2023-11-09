package com.ruoyi.system.roles.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.roles.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 权限与菜单数据处理层
 *
 * @author hjy
 * @date 2023/3/31 13:45
 */
@Mapper
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

}
