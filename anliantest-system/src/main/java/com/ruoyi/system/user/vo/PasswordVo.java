package com.ruoyi.system.user.vo;

import lombok.Data;

/**
 * @author ZhuYiCheng
 * @date 2023/4/27 14:03
 */
@Data
public class PasswordVo {
    /**
     * 用户名邮箱
     */
    private String username;
    /**
     * 原密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;
}
