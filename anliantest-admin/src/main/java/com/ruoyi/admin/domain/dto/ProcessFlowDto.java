package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-12 14:36
 */
@Data
public class ProcessFlowDto implements Serializable {

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

    private List<ImageStatueDto> imageList;
}
