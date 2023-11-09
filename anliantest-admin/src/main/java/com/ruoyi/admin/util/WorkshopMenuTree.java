package com.ruoyi.admin.util;

import com.ruoyi.admin.domain.dto.ProjectWorkshopDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 树形结构
 * @author: zx
 * @date: 2021/10/25 10:02
 */
public class WorkshopMenuTree {
    private List<ProjectWorkshopDto> workshopList = new ArrayList<ProjectWorkshopDto>();
    public WorkshopMenuTree(List<ProjectWorkshopDto> workshopList) {
        this.workshopList=workshopList;
    }

    //建立树形结构
    public List<ProjectWorkshopDto> builTree(){
        List<ProjectWorkshopDto> treeWorkshops =new  ArrayList<ProjectWorkshopDto>();
        for(ProjectWorkshopDto workshopNode : getRootNode()) {
            workshopNode=buildChilTree(workshopNode);
            treeWorkshops.add(workshopNode);
        }
        return treeWorkshops;
    }

    //递归，建立子树形结构
    private ProjectWorkshopDto buildChilTree(ProjectWorkshopDto pNode){
        List<ProjectWorkshopDto> chilWorkshops =new  ArrayList<ProjectWorkshopDto>();
        for(ProjectWorkshopDto workshopNode : workshopList) {
            if(workshopNode.getPid().equals(pNode.getId())) {
                chilWorkshops.add(buildChilTree(workshopNode));
            }
        }
        pNode.setChildren(chilWorkshops);
        return pNode;
    }

    //获取根节点
    private List<ProjectWorkshopDto> getRootNode() {
        List<ProjectWorkshopDto> workshopLists =new  ArrayList<ProjectWorkshopDto>();
        for(ProjectWorkshopDto workshopNode : workshopList) {
            if(workshopNode.getPid().equals(0L)) {
                workshopLists.add(workshopNode);
            }
        }
        return workshopLists;
    }
}