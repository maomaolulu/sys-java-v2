package com.ruoyi.admin.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ZhuYiCheng
 * @date 2023/6/13 10:32
 */
@Data
public class MoneyVo {

    /**
     * 项目总额
     */
    private BigDecimal totalMoney;
    /**
     * 已回款额
     */
    private BigDecimal receiptMoney;
    /**
     * 未回款额
     */
    private BigDecimal receiptOutstanding;

}
