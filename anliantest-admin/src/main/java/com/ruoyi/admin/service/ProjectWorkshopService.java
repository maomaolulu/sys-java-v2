package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.ProjectFormWorkshopDto;
import com.ruoyi.admin.domain.dto.ProjectWorkshopDto;
import com.ruoyi.admin.entity.ProjectWorkshop;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-08
 * @desc : 项目车间岗位
 */
public interface ProjectWorkshopService extends IService<ProjectWorkshop> {
    /**
     * 项目车间岗位点位列表
     *
     * @param projectWorkshopDto
     * @return
     */
    List<ProjectWorkshopDto> pointList(ProjectWorkshopDto projectWorkshopDto);
    /**
     * 项目车间岗位点位列表
     *
     * @param planId
     * @return
     */
    List<ProjectFormWorkshopDto> pointFormList(Long planId);

    /**
     * 删除项目车间岗位表格列表
     *
     * @param projectWorkshopDto
     * @return
     */
    void removeProjectWorkshop(ProjectWorkshopDto projectWorkshopDto);

//    /**
//     * 选择车间岗位
//     *
//     * @param planId
//     * @return
//     */
//    List<ProjectWorkshopDto> selectProjectWorkshop(Long planId);

    /**
     * 递归查询父级id
     *
     * @param ids
     * @return 子级及本身id
     */
    List<Long> fatherIds(List<Long> ids);

}