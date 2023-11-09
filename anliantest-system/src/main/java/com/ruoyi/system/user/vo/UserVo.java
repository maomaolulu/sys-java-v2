package com.ruoyi.system.user.vo;

import com.ruoyi.system.user.entity.ArchiveEntity;
import com.ruoyi.system.user.entity.UserEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/5/17 18:51
 */
@Data
public class UserVo {

    /**
     * 用户基本信息
     */
    private UserEntity userEntity;
    /**
     * 档案信息列表
     */
    private List<ArchiveEntity> archiveEntityList;
}
