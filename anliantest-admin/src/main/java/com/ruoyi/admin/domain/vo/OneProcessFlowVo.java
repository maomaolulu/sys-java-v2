package com.ruoyi.admin.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.admin.domain.dto.ImageStatueDto;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-14 13:58
 */
@Data
public class OneProcessFlowVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 需要永久保存的图片
     */
    private List<ImageStatueDto> imageList = new ArrayList<>();
}
