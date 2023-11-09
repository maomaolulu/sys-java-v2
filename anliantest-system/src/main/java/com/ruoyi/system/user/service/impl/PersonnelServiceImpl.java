package com.ruoyi.system.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.user.entity.PersonnelEntity;
import com.ruoyi.system.user.mapper.PersonnelMapper;
import com.ruoyi.system.user.service.PersonnelService;
import com.ruoyi.system.user.vo.PersonnelVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 14:54
 */
@Service
public class PersonnelServiceImpl extends ServiceImpl<PersonnelMapper, PersonnelEntity> implements PersonnelService {


    /**
     * 新增人事档案
     *
     * @param personnelEntity
     */
    @Override
    public void addFile(PersonnelEntity personnelEntity) {
        Date nowDate = DateUtils.getNowDate();
        //填充相关日期
        personnelEntity.setCollectionDate(nowDate);
        personnelEntity.setCreateTime(nowDate);
        personnelEntity.setUpdateTime(nowDate);

        this.save(personnelEntity);
    }

    /**
     * 批量新增人事档案
     *
     * @param personnelVo
     */
    @Override
    public void addFileBatch(PersonnelVo personnelVo) {

        List<PersonnelEntity> personnelEntityList = personnelVo.getPersonnelEntityList();
        Date nowDate = DateUtils.getNowDate();
        for (PersonnelEntity personnelEntity : personnelEntityList) {
            //填充相关日期
            personnelEntity.setCollectionDate(nowDate);
            personnelEntity.setCreateTime(nowDate);
            personnelEntity.setUpdateTime(nowDate);
        }

        this.saveBatch(personnelEntityList);
    }

    /**
     * 人事档案列表(不分页)
     *
     * @return
     */
    @Override
    public List<PersonnelEntity> getFileList(Long userId) {

        return this.list(new QueryWrapper<PersonnelEntity>().eq("user_id", userId));

    }

    /**
     * 修改人事档案
     *
     * @param personnelVo
     */
    @Override
    public void updateFile(PersonnelVo personnelVo) {
        List<PersonnelEntity> personnelEntityList = personnelVo.getPersonnelEntityList();
        Long userId = personnelEntityList.get(0).getUserId();
        //删除之前的
        this.remove(new QueryWrapper<PersonnelEntity>().eq("user_id", userId));

        for (PersonnelEntity personnelEntity : personnelEntityList) {
            //记录修改时间
            personnelEntity.setUpdateTime(DateUtils.getNowDate());
        }

        //加入新的
        this.saveBatch(personnelEntityList);

    }


    /**
     * 删除人事档案
     *
     * @param userId
     */
    @Override
    public void deleteFile(Long userId) {
        this.remove(new QueryWrapper<PersonnelEntity>().eq("user_id",userId));
    }
}
