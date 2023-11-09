package com.ruoyi.system.login.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.annotation.OperateLogRelease;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.RRException;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.login.form.SysLoginForm;
import com.ruoyi.system.login.service.SysCaptchaService;
import com.ruoyi.system.login.service.SysUserTokenService;
import com.ruoyi.system.login.utils.AlRedisUntil;
import com.ruoyi.system.login.utils.ShiroUtils;
import com.ruoyi.system.user.entity.UserEntity;
import com.ruoyi.system.user.mapper.UserMapper;
import com.ruoyi.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Api(tags = "用户登录相关")
@RestController
public class SysLoginController {
    @Autowired
    private AlRedisUntil alRedisUntil;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysCaptchaService sysCaptchaService;

    /**
     * 验证码
     */
    @ApiOperation("验证码")
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取数字验证码
        BufferedImage image = sysCaptchaService.getMathCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     */
    @PostMapping("/sys/login")
    @OperateLogRelease(title = "登录")
    public Map<String, Object> login2(@RequestBody SysLoginForm form) {
        boolean captcha = sysCaptchaService.verifyCode(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            throw new RRException("验证码不正确");
        }



        //用户信息
        UserEntity user = userService.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getEmail, form.getUsername()));
        if (user == null) {
            throw new RRException("账号不存在");
        } else if (user.getStatus() != 0) {
            if (user.getStatus() == 1) {
                throw new RRException("账号已被锁定,请联系管理员");
            } else {
                throw new RRException("已离职，禁止登录");
            }


        } else if (!user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            throw new RRException("密码错误");

        }

        //用户角色信息列表
        List<Long> roleIdList = userMapper.getRoleIdList(user.getUserId());
        user.setRoleIdList(roleIdList);

        //生成token，并保存到数据库  type: _wx _pc
        R r = sysUserTokenService.createToken(user, form.getType());
        return r;
    }


    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    public R logout( String type) {
        String token = (String) alRedisUntil.get(Constants.ACCESS_USERID + ShiroUtils.getUserId() + type);
        if (StrUtil.isNotBlank(token)) {
            // TODO: 2023-05-05 记录日志
            alRedisUntil.del(Constants.ACCESS_USERID + ShiroUtils.getUserId() + type);
            alRedisUntil.del(Constants.ACCESS_TOKEN + token );
        }
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return R.ok();
    }


}
