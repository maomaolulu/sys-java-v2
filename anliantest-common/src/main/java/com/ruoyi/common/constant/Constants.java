package com.ruoyi.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    public final static String ACCESS_TOKEN     = "access_token:";

    public final static String ACCESS_USERID    = "access_userid:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 数字
     */
    public static final int PRI_NOW_DATA_INT = -6;

    /**
     * admin角色id
     */
    public static final Long ROLE_ADMIN = 1L;
}
