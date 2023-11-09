package com.ruoyi.system.login.form;

import lombok.Data;

/**
 * 密码表单
 *
 * @author mustang.ma@qq.com
 * @date 2018-01-25
 */
@Data
public class PasswordForm {
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
