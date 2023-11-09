package com.ruoyi.system.login.service;


import com.google.code.kaptcha.Producer;
import com.ruoyi.common.exception.RRException;

import com.ruoyi.system.login.utils.AlRedisUntil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * 验证码
 *
 * @author mustang.ma@qq.com
 * @date 2018-02-10
 */
@Service("sysCaptchaService")
public class SysCaptchaService {
    @Autowired
    private Producer captchaProducerMath;
    @Autowired
    private AlRedisUntil alRedisUntil;
//    @Resource(name = "mathCaptchaProducer")
//    private Producer captchaProducerMath;



    public BufferedImage getMathCaptcha(String uuid) {
        if(StringUtils.isBlank(uuid)){
            throw new RRException("uuid不能为空");
        }
        //生成数字验证码
        String capText = captchaProducerMath.createText();
        String capStr = capText.substring(0, capText.lastIndexOf("@"));
        //code 就是算术的结果 也就是输入的验证码
        String code = capText.substring(capText.lastIndexOf("@") + 1);
        //1分钟过期
        alRedisUntil.set(uuid,code,6000);

        return captchaProducerMath.createImage(capStr);
    }

    //判断验证码
    public boolean verifyCode(String uuid, String code) {
        String s = (String) alRedisUntil.get(uuid);

        if(s!=null&&s.equals(code) ){
            return true;
        }
        return false;
    }
}
