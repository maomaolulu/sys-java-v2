package com.ruoyi.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/5/5 18:01
 */
@Data
public class QuotationListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 报价单编号
     */
    private String code;
    /**
     * 客户id
     */
    private Long companyId;
    /**
     * 客户名称
     */
    private String company;
    /**
     * 报价单状态列表
     */
    private String status;
    /**
     * 业务员
     */
    private Long salesmenId;
    /**
     * 报价单类型列表
     */
    private String quotationType;
}
