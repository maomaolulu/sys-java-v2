package com.ruoyi.admin.domain.vo;

import com.ruoyi.admin.entity.InvoiceEntity;
import lombok.Data;

/**
 * @author gy
 * @date 2023-06-12 17:00
 */
@Data
public class InvoiceVo extends InvoiceEntity {
    /**
     *  项目编号
     */
    private String projectIdentifier;
}
