package com.ruoyi.system.login.oauth;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-20
 */
public class OAuthToken implements AuthenticationToken {
    private String token;

    public OAuthToken(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
