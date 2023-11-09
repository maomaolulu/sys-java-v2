package com.ruoyi.system.login.form;

/**
 * @Description
 * @Date 2023/5/6 16:56
 * @Author maoly
 **/

import lombok.Data;

/**
 * 登录表单
 *
 * @author mustang.ma@qq.com
 * @date 2018-01-25
 */
@Data
public class SysLoginForm {

    private String username;

    private String password;

    private String captcha;

    private String uuid;

    private String type;
}

