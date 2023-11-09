package com.ruoyi.system.roles.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.R;
import com.ruoyi.common.utils.spring.SpringContextHolder;
import com.ruoyi.system.menu.entity.SysMenu;
import com.ruoyi.system.menu.service.SysMenuService;
import com.ruoyi.system.roles.entity.SysRole;
import com.ruoyi.system.roles.entity.SysRoleMenu;
import com.ruoyi.system.roles.entity.SysUserRole;
import com.ruoyi.system.roles.mapper.SysRoleMapper;
import com.ruoyi.system.roles.service.SysRoleMenuService;
import com.ruoyi.system.roles.service.SysRoleService;
import com.ruoyi.system.roles.service.SysUserRoleService;
import com.ruoyi.system.roles.util.MenuTree;
import com.ruoyi.system.roles.vo.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色-接口实现层
 *
 * @author zh
 * @date 2023/4/28
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleMenuService sysRoleMenuService;

    @Autowired
    public SysRoleServiceImpl(SysUserRoleService sysUserRoleService, SysRoleMenuService sysRoleMenuService) {
        this.sysUserRoleService = sysUserRoleService;
        this.sysRoleMenuService = sysRoleMenuService;
    }

    /**
     * 列表接口
     *
     * @param sysRole
     * @return
     */
    @Override
    public List<SysRole> selectRoleList(SysRole sysRole) {
        List<SysRole> list = this.list(new LambdaQueryWrapper<SysRole>()
                //角色名查询
                .like(StrUtil.isNotBlank(sysRole.getRoleName()), SysRole::getRoleName, sysRole.getRoleName())
                //状态查询
                .eq(sysRole.getStatus() != null, SysRole::getStatus, sysRole.getStatus())
                //类型查询
                .eq(StrUtil.isNotBlank(sysRole.getType()), SysRole::getType, sysRole.getType())
        );

        if (CollUtil.isNotEmpty(list)) {

            List<Long> roleIds = list.stream().distinct().map(SysRole::getRoleId).collect(Collectors.toList());

            //查询关联用户list
            List<SysUserVo> sysUserVos = baseMapper.selectRoleUsers(new QueryWrapper<Object>().in("ur.role_id", roleIds));

            if (CollUtil.isNotEmpty(sysUserVos)) {
                //分组塞入角色关联的用户
                Map<Long, List<SysUserVo>> sysUserVoList = sysUserVos.stream().collect(Collectors.groupingBy(SysUserVo::getRoleId));

                for (SysRole sysRole1 : list
                ) {
                    List<SysUserVo> sysUserVos1 = sysUserVoList.get(sysRole1.getRoleId());
                    if (CollUtil.isNotEmpty(sysUserVos1)) {
                        sysRole1.setSysUserVoList(sysUserVos1);
                    }
                }
            }

        }


        return list;
    }

    /**
     * 新增
     *
     * @param sysRole
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRole saveSysRole(SysRole sysRole) {
        this.save(sysRole);
        List<SysRoleMenu> sysRoleMenuList = sysRole.getSysRoleMenuList();

        if (CollUtil.isNotEmpty(sysRoleMenuList)) {
            sysRoleMenuService.saveBatch(sysRoleMenuList);

        }

        return sysRole;
    }

    /**
     * 修改
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRole updateSysRole(SysRole sysRole) {
        this.updateById(sysRole);

        List<SysRoleMenu> sysRoleMenuList = sysRole.getSysRoleMenuList();
        // TODO: 2023-04-28 后续判断是否相同在做更新
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, sysRole.getRoleId()));
        if (CollUtil.isNotEmpty(sysRoleMenuList)) {
            sysRoleMenuService.saveBatch(sysRoleMenuList);
        }
        return sysRole;
    }

    /**
     * 删除
     *
     * @param roleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleByIds(List<Long> roleIds) {
        //删除人员关联
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getRoleId, roleIds));
        //删除菜单关联
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds));
        //删除角色
        this.remove(new LambdaQueryWrapper<SysRole>().in(SysRole::getRoleId, roleIds));

    }

    @Override
    public Set<String> roleByUser(Long userId) {


        //角色 及 roles

        List<SysRole> sysRoles = rolesByUserId(userId);
        if (CollUtil.isNotEmpty(sysRoles)) {
            List<Long> rolesIds = sysRoles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
            Set<String> roleKeys = sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
            //按钮
            List<SysMenu> sysMenus = menusByRoles(rolesIds, roleKeys);
            if (CollUtil.isNotEmpty(sysMenus)) {
                List<SysMenu> collect1 = sysMenus.stream().filter(i -> i.getType() == 2).collect(Collectors.toList());

                if (CollUtil.isNotEmpty(collect1)) {
                    Set<String> collect = collect1.stream().map(SysMenu::getPerms).collect(Collectors.toSet());
                    roleKeys.addAll(collect);

                }
            }
            return roleKeys;
        } else {

            return new HashSet<String>();
        }

    }

    /**
     * 角色及菜单
     *
     * @param userId
     * @return
     */
    @Override
    public R roleAndMenuByUser(Long userId) {
        R r = new R();
        //角色 及 roles

        List<SysRole> sysRoles = rolesByUserId(userId);

        if (CollUtil.isNotEmpty(sysRoles)) {
            List<Long> rolesIds = sysRoles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
            Set<String> roleKeys = sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
            //按钮及菜单
            List<SysMenu> sysMenus = menusByRoles(rolesIds, roleKeys);

            if (CollUtil.isNotEmpty(sysMenus)) {
                //菜单权限 取有值的
                List<SysMenu> collect1 = sysMenus.stream().filter(i -> i.getPerms() != null).collect(Collectors.toList());
                //个人权限
                if (CollUtil.isNotEmpty(collect1)) {
                    Set<String> collect = collect1.stream().map(SysMenu::getPerms).collect(Collectors.toSet());
                    roleKeys.addAll(collect);
                    r.put("roles", roleKeys);
                } else {
                    r.put("roles", new HashSet<>());
                }
                //个人菜单
                List<SysMenu> collect2 = sysMenus.stream().filter(i -> i.getType() == 1).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(collect2)) {
                    //递归处理生成树状结构
                    MenuTree menuTree = new MenuTree(collect2);
                    List<SysMenu> sysMenus1 = menuTree.builTree();
                    r.put("menu", sysMenus1);
                } else {
                    r.put("menu", new ArrayList<>());
                }

            } else {
                r.put("menu", new ArrayList<>()).put("roles", roleKeys);
            }
            return r;
        } else {
            r.put("menu", new ArrayList<>()).put("roles", new HashSet<>());
            return r;
        }
    }

    ///角色 及 roles
    private List<SysRole> rolesByUserId(Long userId) {
        //角色 及 roles
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("ur.user_id", userId);
        List<SysRole> sysRoles = baseMapper.sysRolesByUser(wrapper);
        return sysRoles;

    }

    /**
     * 根据用户查询角色id
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> roleIdsByUser(Long userId) {

        List<Long> roleIds = baseMapper.roleIdsByUser(userId);
        return roleIds;

    }

    /**
     * 角色已勾选菜单及按钮
     *
     * @return
     */
    @Override
    public R haveMenu(Long roleId) {

        SysMenuService sysMenuService = SpringContextHolder.getBean(SysMenuService.class);
        //全部菜单
//        List<SysMenu> listAll = sysMenuService.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getVisible, 0));
        List<SysMenu> listAll = sysMenuService.list();
        //父级名称
        Map<Long, String> collect1 = listAll.stream().collect(Collectors.toMap(SysMenu::getMenuId, SysMenu::getName));
        for (SysMenu sysMenu:listAll
             ) {
            if(sysMenu.getParentId()==0){
                sysMenu.setParentName(sysMenu.getModuleName());

            }else {

                sysMenu.setParentName(collect1.get(sysMenu.getParentId()));

            }
        }



        //分类型菜单
        Map<String, List<SysMenu>> map = listAll.stream().collect(Collectors.groupingBy(SysMenu::getModuleType));


        //分类型菜单选择id
        Map<String, List<Long>> linkedHashMap = new LinkedHashMap<>();
        for (String key : map.keySet()) {

            List<SysMenu> sysMenus = map.get(key);
            if (CollUtil.isNotEmpty(sysMenus)) {
                //key ： 不同菜单类型
                //对应 拥有的ids
                //根据roleId 和 moduleType 查询有的menuIds (显示的)
                List<Long> longs = baseMapper.menuIdsByRoleAndmoduleType(roleId, key);
                //根据roleId 和 moduleType 查询有的menuIds (不显示的)
                List<Long> longs2 = baseMapper.menuIdsByRoleAndmoduleTypeVisible(roleId, key);
                linkedHashMap.put(key + "Ids", longs);
                linkedHashMap.put(key + "IdsVisible", longs2);


                MenuTree menuTree = new MenuTree(sysMenus);

                List<SysMenu> sysMenus1 = menuTree.builTree();

                //外层包一层
                ArrayList<SysMenu> sysMenus2 = new ArrayList<>();
                SysMenu sysMenu = new SysMenu();
                sysMenu.setChildren(sysMenus1);
                sysMenu.setVisible("0");
                sysMenu.setName(sysMenus.get(0).getModuleName());
                sysMenu.setMenuId(0L);
                sysMenus2.add(sysMenu);

                map.put(key, sysMenus2);
            }


        }
        return R.ok().put("map", map).put("mapIds", linkedHashMap);
    }

    /**
     * 分配权限（新增修改角色关联菜单）
     * @param roleId
     * @param menuIds
     */
    @Override
    @Transactional
    public void saveRoleMenu(Long roleId, List<Long> menuIds) {
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        if(CollUtil.isNotEmpty(menuIds)){
            ArrayList<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            for (Long menuId:menuIds
                 ) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenus.add(sysRoleMenu);
            }
            sysRoleMenuService.saveBatch(sysRoleMenus);

        }
    }


    private List<SysMenu> menusByRoles(List<Long> rolesIds, Set<String> roleKeys) {


        QueryWrapper<SysRole> wrapper1 = new QueryWrapper<>();

        boolean admin = roleKeys.stream().anyMatch(i -> "admin".equals(i));
        //角色是否拥有超级管理员
//        wrapper1.eq("sm.visible",0);
//        wrapper1.eq("rm.type",type);
        if (!admin) {
            wrapper1.in("rm.role_id", rolesIds);

        }
        wrapper1.orderByAsc("sm.order_num");
        List<SysMenu> menus = baseMapper.sysMenuByRoles(wrapper1);

        return menus;
    }
}
