package com.ruoyi.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.user.dto.UserDto;
import com.ruoyi.system.user.entity.UserEntity;
import com.ruoyi.system.user.vo.PasswordVo;

import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/4/26 10:06
 */
public interface UserService extends IService<UserEntity> {


    /**
     * 校验工号唯一
     * @param jobNum
     * @return
     */
    Boolean checkJobNum(String jobNum);

    /**
     * 校验邮箱唯一
     * @param email
     * @return
     */
    Boolean checkEmail(String email);

    /**
     * 新增用户
     *
     * @param userEntity
     * @return
     */
    Boolean addUser(UserEntity userEntity);

    /**
     * 所有用户列表
     *
     * @return
     */
    List<UserEntity> getUserList(UserDto userDto);

    /**
     * 显示所有用户列表(不分页,用于下列选择用户,不含中介)
     * @return
     */
    List<UserEntity> getListAll();

    /**
     * 根据id获取用户
     * @param userId
     * @return
     */
    UserEntity getDetail(Long userId);

    /**
     * 修改用户信息
     * @param userEntity
     */
    Boolean updateUser(UserEntity userEntity);

    /**
     * 修改用户状态(status状态  0：禁用   1：正常)
     * @param userId
     * @param status
     */
    void updateStatus(Long userId, Integer status);

    /**
     * 修改用户登录密码
     * @param passwordVo
     * @return
     */
    Boolean updatePassword(PasswordVo passwordVo);

    /**
     * 删除用户信息(逻辑删除)
     * @param userId
     */
    void deleteUser(Long userId);



}
