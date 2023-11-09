package com.ruoyi.system.login.service;


import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.login.oauth.TokenGenerator;
import com.ruoyi.system.login.utils.AlRedisUntil;
import com.ruoyi.system.user.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户Token
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Service("accessTokenService")
@Slf4j
public class SysUserTokenService  {
	//12小时后过期
	private final static int EXPIRE = 1800;
	@Autowired
	private AlRedisUntil alRedisUntil;
	private final static String ACCESS_TOKEN  = Constants.ACCESS_TOKEN;

	private final static String ACCESS_USERID = Constants.ACCESS_USERID;
	/**
	 * 生成token
	 * @param
	 * @return
	 */
	public R createToken(UserEntity user, String type){

		//_pc  _wx
		//生成一个token
		String token = TokenGenerator.generateValue();
		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		user.setPassword(null);
		user.setSalt(null);

		//踢除之前token
		expireToken(user.getUserId(),type);

		alRedisUntil.set( ACCESS_USERID+ user.getUserId()+type, token, EXPIRE);
		alRedisUntil.set(ACCESS_TOKEN + token+type, user, EXPIRE);

		R r = R.ok().put("token", token+type).put("expire", EXPIRE).put("expireTime", expireTime);

		return r;
	}


	public void expireToken(long userId,String type)
	{
		String token =(String) alRedisUntil.get(ACCESS_USERID + userId + type);
		if (StringUtils.isNotBlank(token))
		{
			alRedisUntil.del(ACCESS_USERID + userId + type);
			alRedisUntil.del(ACCESS_TOKEN + token + type);
		}
	}


////	/**
////	 * 退出，修改token值
////	 * @param userId  用户ID
////	 */
////	void logout(long userId,String type);
//
//    /**
//     * 根据用户ID删除token
//     * @param userId
//     */
//    void deleteToken(Long userId);
//
//	/**
//	 * xin redis 中创建Token及缓存信息
//	 * @param userId
//	 * @param type
//	 * @return
//	 */
//	R newCreateToken(Long userId, String type);
//
//	/**
//	 * xin退出
//	 * @param userId
//	 * @param type
//	 */
//	void logoutNew(Long userId, String type);
}
