package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ZhuYiCheng
 * @date 2023/4/20 10:23
 */
@Data
@TableName("ly_quotation_info")
public class QuotationInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 报价基本信息id
     */
    private Long quotationId;
    /**
     * 车间
     */
    private String workshop;
    /**
     * 岗位
     */
    private String post;
    /**
     * 检测物质
     */
    private String substance;
    /**
     * 检测物质id
     */
    private Long substanceId;
    /**
     * 检测物质资质(1有资质,2无资质)
     */
    private Integer qualification;
    /**
     * 是否委托(0是,1否)
     */
    private Integer isEntrust;
    /**
     * 是否分包(0是,1否)
     */
    private Integer isSubcontract;
    /**
     * 应测点数
     */
    private Integer shouldPoint;
    /**
     * 实测点数
     */
    private Integer point;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 小计
     */
    private BigDecimal subtotal;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删
     */
    private Integer delFlag;
    /**
     * 创建人id
     */
    private Long createById;
    /**
     * 创建人名称
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
