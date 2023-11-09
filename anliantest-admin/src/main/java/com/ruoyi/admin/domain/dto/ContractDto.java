package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/6/2 13:52
 */
@Data
public class ContractDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 业务员id
     */
    private Long salesmenid;
    /**
     * 委托单位名称
     */
    private String entrustCompany;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 合同编号
     */
    private String identifier;
    /**
     * 合同签订状态(0未回，1已回)
     */
    private Integer contractStatus;
    /**
     * 合同评审状态（0待评审,1评审中,2评审通过,3评审未通过,4撤销审核）
     */
    private Integer reviewStatus;
    /**
     * 履约状态（0待履约,1履约中,2履约完成,3合同终止）
     */
    private Integer performStatus;
    /**
     * 合同签订日期起始查询时间
     */
    private String startSignDate;
    /**
     * 合同签订日期终止查询时间
     */
    private String endSignDate;
}
