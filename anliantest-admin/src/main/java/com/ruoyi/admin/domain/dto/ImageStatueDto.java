package com.ruoyi.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-06-14 13:38
 */
@Data
public class ImageStatueDto implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 预览路径
     */
    private String preUrl;
    /**
     * 真实路径
     */
    private String path;
}
