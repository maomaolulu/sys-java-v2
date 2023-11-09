package com.ruoyi.admin.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.admin.domain.dto.ImageStatueDto;
import com.ruoyi.admin.entity.ProcessFlowEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-13 10:03
 */
@Data
public class ProcessFlowVo implements Serializable {

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
     * 工艺流程类型(0:子工艺1:工艺)
     */
    private Integer flowType;

    /**
     * 子工艺
     */
    private List<ProcessFlowVo> sons;

    /**
     * 需要永久保存的图片
     */
    private List<ImageStatueDto> imageList = new ArrayList<>();
}
