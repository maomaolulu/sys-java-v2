package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhuYiCheng
 * @date 2023/4/23 18:13
 */
@Data
@TableName("ly_quotation_workshop")
public class WorkshopEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 报价基本信息id
     */
    private Long quotationId;
    /**
     * 车间id
     */
    private Long workshopId;
    /**
     * 车间
     */
    private String workshop;
    /**
     * 岗位
     */
    private String post;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 逻辑删除
     */
    private Integer delFlag;
    /**
     * 创建人id
     */
    private Long createById;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;

}
