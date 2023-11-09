package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程画布
 * @author gy
 * @date 2023-06-12 13:55
 */
@Data
@TableName("ly_process_canvas")
@Accessors(chain = true)
public class ProcessCanvasEntity implements Serializable {

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
