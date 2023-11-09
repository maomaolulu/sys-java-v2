package com.ruoyi.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.user.entity.PersonnelEntity;
import com.ruoyi.system.user.vo.PersonnelVo;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 14:54
 */
public interface PersonnelService extends IService<PersonnelEntity> {


    /**
     * 新增人事档案
     * @param personnelEntity
     */
    void addFile(PersonnelEntity personnelEntity);

    /**
     * 批量新增人事档案
     * @param personnelVo
     */
    void addFileBatch(PersonnelVo personnelVo);

    /**
     * 人事档案列表(不分页)
     * @return
     */
    List<PersonnelEntity> getFileList(Long userId);

    /**
     * 修改人事档案
     * @param personnelVo
     */
    void updateFile(PersonnelVo personnelVo);

    /**
     * 删除人事档案
     * @param userId
     */
    void deleteFile(Long userId);


}
