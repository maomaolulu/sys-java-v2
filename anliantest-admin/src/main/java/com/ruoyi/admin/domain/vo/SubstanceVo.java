package com.ruoyi.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ZhuYiCheng
 * @date 2023/4/23 15:01
 */
@Data
public class SubstanceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * al_substance表id
     */
    private Long id;
    /**
     * 检测项目
     */
    private String name;
    /**
     * 物质类型 (1.毒物(包括co/co2)  2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传振动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 )',
     */
    private Integer sType;
    /**
     * 有无资质(1 有,2 无)
     */
    private Integer qualification;
    /**
     * 单价(元)
     */
    private BigDecimal unitPrice;
    /**
     * 对应实验室数据来源
     */
    private String labSource;
}
