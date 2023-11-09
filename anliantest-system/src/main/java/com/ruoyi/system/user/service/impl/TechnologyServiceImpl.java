package com.ruoyi.system.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.user.entity.TechnologyEntity;
import com.ruoyi.system.user.mapper.TechnologyMapper;
import com.ruoyi.system.user.service.TechnologyService;
import com.ruoyi.system.user.vo.TechnologyVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 16:37
 */
@Service
public class TechnologyServiceImpl extends ServiceImpl<TechnologyMapper, TechnologyEntity> implements TechnologyService {


    /**
     * 校验证书编号
     *
     * @param code
     * @return
     */
    @Override
    public Boolean checkCode(String code) {
        TechnologyEntity technologyEntity = this.getOne(new QueryWrapper<TechnologyEntity>().eq("code", code));

        return technologyEntity != null;
    }

    /**
     * 新增技术档案
     *
     * @param technologyEntity
     */
    @Override
    public void addTechnology(TechnologyEntity technologyEntity) {
        Date nowDate = DateUtils.getNowDate();
        //填充相关日期
        technologyEntity.setCollectionDate(nowDate);
        technologyEntity.setCreateTime(nowDate);
        technologyEntity.setUpdateTime(nowDate);

        this.save(technologyEntity);
    }

    /**
     * 批量新增人事档案
     *
     * @param technologyVo
     */
    @Override
    public void addTechnologyBatch(TechnologyVo technologyVo) {
        List<TechnologyEntity> technologyEntityList = technologyVo.getTechnologyEntityList();
        Date nowDate = DateUtils.getNowDate();
        for (TechnologyEntity technologyEntity : technologyEntityList) {
            //填充相关日期
            technologyEntity.setCollectionDate(nowDate);
            technologyEntity.setCreateTime(nowDate);
            technologyEntity.setUpdateTime(nowDate);
        }

        this.saveBatch(technologyEntityList);
    }


    /**
     * 技术档案列表(不分页)
     *
     * @param userId
     * @return
     */
    @Override
    public List<TechnologyEntity> getFileList(Long userId) {

        return this.list(new QueryWrapper<TechnologyEntity>().eq("user_id", userId));
    }


    /**
     * 修改技术档案
     *
     * @param technologyVo
     */
    @Override
    public void updateTechnology(TechnologyVo technologyVo) {
        List<TechnologyEntity> technologyEntityList = technologyVo.getTechnologyEntityList();
        Long userId = technologyEntityList.get(0).getUserId();
        //删除之前的
        this.remove(new QueryWrapper<TechnologyEntity>().eq("user_id", userId));

        for (TechnologyEntity technologyEntity : technologyEntityList) {
            //记录修改时间
            technologyEntity.setUpdateTime(DateUtils.getNowDate());
        }

        //加入新的
        this.saveBatch(technologyEntityList);
    }


    /**
     * 删除技术档案
     *
     * @param userId
     */
    @Override
    public void deleteTechnology(Long userId) {
        this.remove(new QueryWrapper<TechnologyEntity>().eq("user_id", userId));
    }
}
