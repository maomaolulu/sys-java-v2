package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/6/8 18:03
 */
@Data
public class ProjectReceiptDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 项目编号(模糊查询)
     */
    private String identifier;

    /**
     * 企业名称(模糊查询)
     */
    private String company;
}
