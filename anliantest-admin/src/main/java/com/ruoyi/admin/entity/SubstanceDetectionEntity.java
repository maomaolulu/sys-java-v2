package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @date 2023-06-05
 */
@Data
@TableName("al_substance_detection")
@Accessors(chain = true)
public class SubstanceDetectionEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 自增主键 */
    @TableId
    private Long id;
    /** 物质 id */
    private Long substanceId;
    /** 实验室检测能力 id */
    private Long mainDataId;
    /** 资质（1：有, 2：无） */
    private Byte qualification;
    /** 物质名称（物质表信息，此表可冗余） */
    private String name;
    /** 原表中区分物质字段 */
    private String sampleTablename;
    /** 职业卫生检测标准 */
    private String standard;
    /** 职业卫生检测标准编号 */
    private String standardSerialNum;
    /** 通过认证的检测方法序号 */
    private String detectionMethodNum;
    /** 职业卫生检测标准名 */
    private String standardName;
    /** 检测方法（已更新） */
    private String detectionMethod;
    /** 物质含有多种检测标准时的标识字段（默认值 1） */
    private Integer markNum;
    /** 采样设备 */
    private String equipment;
    /** 采样流量（L/min） */
    private String flow;
    /** 采样时间（min) */
    private String testTime;
    /** 采样时间说明 */
    private String testTimeNote;
    /** 最低检出浓度 */
    private String minDetectable;
    /** 能否个体采样（0：否，1：是） */
    private Byte indSample;
    /** 采样设备（个体） */
    private String indEquipment;
    /** 采样流量（L/min)（个体） */
    private String indFlow;
    /** 采样时间（min）（个体） */
    private String indTestTime;
    /** 采样时间说明（个体） */
    private String indTestTimeNote;
    /** 最低检出浓度（个体） */
    private String indMinDetectable;
    /** 收集器 */
    private String collector;
    /** 保存/运输方式 */
    private String preserveTraffic;
    /** 样品保存要求 */
    private String preserveRequire;
    /** 样品保存期限（天） */
    private Integer shelfLife;
    /** 对应实验室数据来源 */
    private String labSource;
    /** 数据入库时间 */
    private Date createtime;
}
