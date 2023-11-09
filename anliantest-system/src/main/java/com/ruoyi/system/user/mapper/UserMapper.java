package com.ruoyi.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/26 9:49
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {


    @Select("SELECT r.role_name FROM sys_role r LEFT JOIN sys_user_role ur ON r.role_id = ur.role_id \n" +
            "\tLEFT JOIN sys_user u ON ur.user_id = u.user_id WHERE u.user_id = #{userId};")
    List<String> getRoleList(@Param("userId") Long userId);

    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId};")
    List<Long> getRoleIdList(@Param("userId") Long userId);
}
