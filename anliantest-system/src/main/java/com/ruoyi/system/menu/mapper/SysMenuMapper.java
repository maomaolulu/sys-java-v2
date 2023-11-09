package com.ruoyi.system.menu.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.system.menu.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单数据处理层
 *
 * @author hjy
 * @date 2023/3/31 9:19
 */
@Mapper
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    @Select(" SELECT * FROM sys_menu me\n" +
            "left join sys_role_menu rm on me.menu_id=rm.menu_id\n" +
//            "where rm.role_id in (1) and me.type=1 and  me.visible=0 ")
            " ${ew.customSqlSegment} ")
    List<SysMenu> SysMenuByUser(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
