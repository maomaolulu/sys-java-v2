package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhanghao
 * @date 2023-06-12
 * @desc : 项目车间岗位设备
 */
@Data
public class EquipmentDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 车间岗位id */
    private Long workshopId ;
    /** 建筑id */
    private Long buildingId ;
    /** 车间id */
    private Long workId ;
    /** 项目id */
    private Long projectId ;
    /** 任务id（派单id） */
    private Long planId ;
    /** 建筑（评价） */
    private String building ;
    /** 车间 */
    private String workshop ;
    /** 岗位 */
    private String post ;

    /** 设备id  */
    private Long id ;
    /** 设备名称 */
    private String equipment ;
    /** 型号/规格 */
    private String spotCode ;
    /** 数量 */
    private Integer number ;
    /** 运行数 */
    private Integer operationNumber ;
    /** 布局 */
    private String layout ;
    /** 涉及危害(物质id逗号分割) */
    private String substanceIds ;
    /** 设备状况（0全封闭、1设备局部开口有负压、2设备局部开口无负压、3设备敞开） */
    private Integer status ;
 }
