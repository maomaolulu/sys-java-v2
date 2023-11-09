package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 工艺流程
 * @author gy
 * @date 2023-06-12 13:50
 */
@Data
@TableName("ly_process_flow")
@Accessors(chain = true)
public class ProcessFlowEntity implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 任务id（派单id）
     */
    private Long planId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 标题
     */
    private String title;

    /**
     * 小节
     */
    private String conclusion;

    /**
     * 内容
     */
    private String content;

    /**
     * 工艺流程类型(0:子工艺1:工艺)
     */
    private Integer flowType;

    /**
     * 逻辑删
     */
    private int delFlag;

    /**
     * 创建人id
     */
    private Long createById;

    /**
     * 创建人名称
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 数据入库时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
