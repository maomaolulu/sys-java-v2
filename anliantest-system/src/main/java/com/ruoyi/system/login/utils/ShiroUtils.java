package com.ruoyi.system.login.utils;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.system.user.entity.UserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;

import java.util.List;

import static org.apache.shiro.SecurityUtils.getSubject;

/**
 * shiro工具类
 */
public class ShiroUtils {
    /**
     * 获取用户信息
     * @return
     */
    public static UserEntity getUserEntity() {
        return (UserEntity) getSubject().getPrincipal();
    }

    /**
     * 获取用户姓名id
     */
    public static Long getUserId() {
        return getUserEntity().getUserId();
    }

    /**
     * 获取用户所属部门
     */
    public static Long getDeptId() {
        return getUserEntity().getDeptId();
    }

    /**
     * 获取用户姓名
     */
    public static String getUserName() {
        return getUserEntity().getUsername();
    }

    /**
     * 获取用户邮箱
     */
    public static String getEmail() {
        return getUserEntity().getEmail();
    }

    /**
     * 获取当前用户隶属公司
     */
    public static String getCompanyName() {
        return getUserEntity().getSubjection();
    }

//    /**
//     * 获取当前用户登录的token
//     */
//    public static String getToken() {
//        return getUserEntity().getToken();
//    }


    /**
     * 生成随机盐
     */
    public static String randomSalt() {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(3).toHex();
    }

    /**
     * 是否为管理员
     *
     * @return 结果
     */
    public static boolean isAdmin() {
        UserEntity userEntity = getUserEntity();
        List<Long> roleIdList = userEntity.getRoleIdList();
        if(CollectionUtils.isNotEmpty(roleIdList) && roleIdList.contains(Constants.ROLE_ADMIN)){
            return true;
        }
        return false;
    }

}
