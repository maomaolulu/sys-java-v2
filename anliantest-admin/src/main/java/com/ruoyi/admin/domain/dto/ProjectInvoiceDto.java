package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 项目和开票Vo
 * @author gy
 * @date 2023-06-08 11:23
 */
@Data
public class ProjectInvoiceDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    private Long id;
    /**
     * 项目编号(模糊查询)
     */
    private String identifier;

    /**
     * 企业名称(模糊查询)
     */
    private String company;

}
