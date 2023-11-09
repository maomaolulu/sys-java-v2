package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhanghao
 * @email
 * @date 2023-06-12
 * @desc : 设备布局
 */
@Data
@TableName("ly_equipment_layout")
public class EquipmentLayout implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id ;
	/** 项目id */
	private Long projectId ;
	/** 任务id（派单id） */
	private Long planId ;
	/** 车间岗位id */
	private Long workshopId ;
	/** 建筑id */
	private Long buildingId ;
	/** 车间id */
	private Long workId ;
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
	/** 备注 */
	private String remark ;
	/** 逻辑删 */
	private Integer delFlag ;
	/** 创建人id */
	private Long createById ;
	/** 创建人名称 */
	private String createBy ;
	/** 创建时间 */
	private Date createTime ;
	/** 更新人 */
	private String updateBy ;
	/** 更新时间 */
	private Date updateTime ;

}