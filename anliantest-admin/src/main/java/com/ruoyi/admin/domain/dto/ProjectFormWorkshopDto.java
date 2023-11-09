package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhanghao
 * @date 2023-06-15
 * @desc : 项目车间岗位点位表格列表
 */
@Data
public class ProjectFormWorkshopDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 车间岗位id */
    private Long id ;
    /** 父级id */
    private Long pid ;

    /** 建筑 */
    private String building ;
    /** 车间 */
    private String workshop ;
    /** 岗位 */
    private String post ;
    /** 点位 */
    private String point ;
    /** 点位编码 */
    private String spotCode ;


 }
