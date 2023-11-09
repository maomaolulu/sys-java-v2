package com.ruoyi.system.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/6/5 16:31
 */
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户工号
     */
    private String jobNum;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户状态
     */
    private String status;
}
