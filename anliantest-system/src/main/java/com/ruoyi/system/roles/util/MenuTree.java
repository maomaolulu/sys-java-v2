package com.ruoyi.system.roles.util;

import com.ruoyi.system.menu.entity.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 树形结构
 * @author: zx
 * @date: 2021/10/25 10:02
 */
public class MenuTree {
    private List<SysMenu> menuList = new ArrayList<SysMenu>();
    public MenuTree(List<SysMenu> menuList) {
        this.menuList=menuList;
    }

    //建立树形结构
    public List<SysMenu> builTree(){
        List<SysMenu> treeMenus =new  ArrayList<SysMenu>();
        for(SysMenu menuNode : getRootNode()) {
            menuNode=buildChilTree(menuNode);
            treeMenus.add(menuNode);
        }
        return treeMenus;
    }

    //递归，建立子树形结构
    private SysMenu buildChilTree(SysMenu pNode){
        List<SysMenu> chilMenus =new  ArrayList<SysMenu>();
        for(SysMenu menuNode : menuList) {
            if(menuNode.getParentId().equals(pNode.getMenuId())) {
                chilMenus.add(buildChilTree(menuNode));
            }
        }
        pNode.setChildren(chilMenus);
        return pNode;
    }

    //获取根节点
    private List<SysMenu> getRootNode() {
        List<SysMenu> rootMenuLists =new  ArrayList<SysMenu>();
        for(SysMenu menuNode : menuList) {
            if(menuNode.getParentId().equals(0L)) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }
}