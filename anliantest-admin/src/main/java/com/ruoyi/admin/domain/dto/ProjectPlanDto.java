package com.ruoyi.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.admin.entity.ProjectRecord;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 排单数据
 *
 * @author zhanghao
 * @email
 * @date 2023-05-18
 */
@Data
public class ProjectPlanDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 排单id
     */
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目编号
     */
    private String identifier;

    /**
     * 项目类型
     */
    private String type;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 企业名称
     */
    private String company;

    /**
     * 企业地址
     */
    private String officeAddress;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 业务员ID
     */
    private Long salesmenid;
    /**
     * 业务员
     */
    private String salesmen;

    /**
     * 项目状态(0:未启动，1：已启动，2：已完成，3：已结束 4：终止 5：挂起/暂停)
     */
    private Integer status;

    /**
     * 项目金额(元)
     */
    private BigDecimal totalMoney;
    /**
     * 项目净值(元)
     */
    private BigDecimal netvalue;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 负责人id
     */
    private Long chargeId;
    /**
     * 负责人
     */
    private String charge;

    //--------------------排单信息----------------------
    /**
     * 排单次数
     */
    private Integer planNumber;
    /**
     * 排单状态（ 0：未派单 1：待确认 2.已确认    ）
     */
    private Integer planStatus;
    /**
     * 下发日期/申请日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 派单日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date planDate;
    /**
     * 调查日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date planSurveyDate;

    /**
     * 报告调查日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date reportSurveyDate;

    /** 报告调查人 */
    private String reportSurveyUser ;
    /** 报告调查人id */
    private Long reportSurveyUserId ;
    /** 调查复核人 */
    private String checkUser ;
    /** 调查复核人id */
    private Long checkUserId ;
    /**
     * 派单记录
     */
    private List<ProjectRecord> planRecordList;
}
