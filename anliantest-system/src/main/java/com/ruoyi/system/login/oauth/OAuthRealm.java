package com.ruoyi.system.login.oauth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.system.login.utils.AlRedisUntil;
import com.ruoyi.system.roles.service.SysRoleService;
import com.ruoyi.system.user.entity.UserEntity;
import com.ruoyi.system.user.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 认证
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-20
 */
@Component
public class OAuthRealm extends AuthorizingRealm {

    @Autowired
    private AlRedisUntil alRedisUntil;
    //30分钟后过期
    private final static int EXPIRE = 1800 ;
    @Autowired
    private UserService userService;
    @Autowired
    private SysRoleService sysRoleService;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuthToken;
    }


    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserEntity user = (UserEntity)principals.getPrimaryPrincipal();
        Long userId = user.getUserId();
//        System.out.println("授权(验证权限时调用) OAuth2Realm.doGetAuthorizationInfo......");
        //用户权限列表
        Set<String> permsSet = sysRoleService.roleByUser(userId);
        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();

        userEntityQueryWrapper.orderByDesc("id");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        Object userStr = alRedisUntil.get(Constants.ACCESS_TOKEN + accessToken);
        //token失效
        if(userStr == null ){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        UserEntity sysUserEntity = alRedisUntil.fromJson(alRedisUntil.toJson(userStr),UserEntity.class);
        //查询用户信息
//        UserEntity user = userService.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUserId, sysUserEntity.getUserId()));
        //账号锁定
        if(sysUserEntity.getStatus() != 0){
            if(sysUserEntity.getStatus()==1){
                throw new LockedAccountException("账号已被锁定,请联系管理员");
            }else {
                throw new LockedAccountException("已离职，禁止登录");
            }

        }
        //刷新token
        alRedisUntil.expire(Constants.ACCESS_TOKEN+accessToken,EXPIRE);
        String str = "_pc";
        if(accessToken.contains("_wx")){
            accessToken="_wx";
        }
        alRedisUntil.expire(Constants.ACCESS_USERID+sysUserEntity.getUserId()+str,EXPIRE);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(sysUserEntity, accessToken, getName());
        return info;
    }
}
