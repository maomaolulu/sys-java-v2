package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/6/2 10:21
 */
@Data
public class ProjectDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 业务员id
     */
    private Long salesmenid;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 合同签订日期起始查询时间
     */
    private String startSignDate;
    /**
     * 合同签订日期终止查询时间
     */
    private String endSignDate;
    /**
     * 项目状态(0:未启动，1：已启动，2：已完成，3：已结束 4：终止 5：挂起/暂停)
     */
    private Integer status;
    /**
     * 合同签订状态（0未签订，1已签订）
     */
    private Integer contractSignStatus;
}
