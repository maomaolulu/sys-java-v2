package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/6/6 17:51
 */
@Data
public class CompanyDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业名称
     */
    private String company;
    /**
     * 统一社会信用代码
     */
    private String code;
    /**
     * 客户归属
     */
    private String belong;
    /**
     * 客户归属人id
     */
    private Integer belongId;
    /**
     * 客户类型（0潜在，1新，2老，3忠诚）
     */
    private Integer type;
    /**
     * 客户属性
     */
    private Integer state;
    /**
     * 企业规模
     */
    private String companyScale;
}
