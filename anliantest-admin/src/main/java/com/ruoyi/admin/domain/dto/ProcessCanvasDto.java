package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-06-13 11:27
 */
@Data
public class ProcessCanvasDto implements Serializable {
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
     * 工艺流程id
     */
    private Long flowId;

    /**
     * 画布内容
     */
    private String content;

    /**
     * 图片地址（改为minio方式）
     */
    private String path;
}
