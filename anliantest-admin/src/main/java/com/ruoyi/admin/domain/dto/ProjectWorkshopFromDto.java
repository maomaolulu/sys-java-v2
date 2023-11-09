package com.ruoyi.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.admin.entity.EquipmentLayout;
import com.ruoyi.admin.entity.Point;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-12
 * @desc : 项目车间岗位点位
 */
@Data
public class ProjectWorkshopFromDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 车间岗位id */
    private Long id ;
    /** 项目id */
    private Long projectId ;
    /** 任务id（派单id） */
    private Long planId ;

    /** 父级id */
    private Long pid ;
    /** 评价：0:建筑，1：车间 ，2：岗位   */
    private String type ;
    /** 名称 */
    private String name ;
    /** 建筑（评价） */
    private String building ;
    /** 车间 */
    private String workshop ;
    /** 岗位 */
    private String post ;
    /** 点位 */
    private String point ;
    /** 点位编号 */
    private String spotCode ;
    /**
     * 点位
     */
    private List<Point>  pointList;

    @TableField(exist = false)
    private List<ProjectWorkshopFromDto> children ;
 }
