package com.ruoyi.admin.domain.vo;

import lombok.Data;

import java.util.List;
/**
 * @author ZhuYiCheng
 * @date 2023/3/29 13:45
 */
@Data
public class AssignmentVo {

    private List<Long> ids;

    private Long userId;

    private String username;
}