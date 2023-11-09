package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-05
 */
@Data
@TableName("al_substance")
@Accessors(chain = true)
public class SubstanceEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 自增主键ID */
    @TableId
    private Long id;
    /** 名称 */
    private String name;
    /** 总尘id */
    private Integer totalDustId;
    /** 英文名 */
    private String nameEn;
    /** 化学文摘号CAS No. */
    private String casNo;
    /** 最高容许浓度(mg/ m³) */
    private Float mac;
    /** 时间加权平均容许浓度(mg/ m³) */
    private Float pcTwa;
    /** 短时间接触容许浓度(mg/ m³) */
    private Float pcStel;
    /** 临界不良反应 */
    private String reaction;
    /** 是否折算(2否,1是) */
    private Integer deduction;
    /** 备注 */
    private String remaks;
    /** 物质类型(1.毒物(包括co/co2)  2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传振动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 ) */
    private Integer sType;
    /** 计算方式(1.mac 2.pc/twa 3.pc/twa+pc/stel) */
    private Integer computeMode;
    /** 采样物质所属部门(1.职卫2.环境公卫3.辐射) */
    private Integer sDept;
    /** 是否高毒(1毒物非高毒2.毒物高毒) */
    private Integer highlyToxic;
    /** 数据入库时间 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;
    /** 修改时间 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatetime;
    /** 备注说明(皮/敏/G1) */
    private String remarksNote;
    /** 标识(选择物质时区分同物质的参数) */
    private String mark;
    /** 是否根据游离二氧化硅判定(1是,2否) */
    private Integer isSilica;
    /** 市级申报code */
    private String sjsbCode;
    /** 检测项目对照数据id */
    private Integer indicatorId;
    /** 合并之后的名称  例如五苯两酯里 五苯合并之后的名称都是苯系物  乙酸乙酯和乙酸丁酯合并之后的名称都是乙酸酯类   这样最后把所有合并名称去重就得到了合并之后的名字 */
    private String mergeName;
    /** 所有物质合并之后的名称  例如五苯两酯   乙酸之类(不是五苯两酯的那两个酯类) */
    private String totalMergeName;
    /** 在合并采样的物质中的排序 */
    private Integer mergeSort;
    /** 是否可以测个体(1可以 2不可以)*/
    private Integer hasSolo;
    /** 对应的 */
    @TableField(exist = false)
    private List<SubstanceDetectionEntity> substanceDetectionEntities;
}
