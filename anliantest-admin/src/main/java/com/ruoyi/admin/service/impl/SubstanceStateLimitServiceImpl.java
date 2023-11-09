package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.entity.SubstanceInfoEntity;
import com.ruoyi.admin.entity.SubstanceStateLimitEntity;
import com.ruoyi.admin.mapper.SubstanceStateLimitMapper;
import com.ruoyi.admin.service.SubstanceStateLimitService;
import com.ruoyi.common.enums.DeleteFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
@Service
public class SubstanceStateLimitServiceImpl extends ServiceImpl<SubstanceStateLimitMapper, SubstanceStateLimitEntity> implements SubstanceStateLimitService {


    @Autowired
    private SubstanceStateLimitMapper stateLimitMapper;

    /**
     * 新增危害因素限值
     *
     * @param stateLimit
     * @return
     */
    @Override
    public Boolean addLimit(SubstanceStateLimitEntity stateLimit) {

        return this.save(stateLimit);
    }


    /**
     * 获取危害因素限值
     *
     * @param substanceInfoId
     * @return
     */
    @Override
    public List<SubstanceStateLimitEntity> getListBySubstanceInfoId(Long substanceInfoId) {
        QueryWrapper<SubstanceStateLimitEntity> queryWrapper = new QueryWrapper<SubstanceStateLimitEntity>()
                .eq(substanceInfoId != null, "ss.substance_info_id", substanceInfoId)
                .eq("ss.delete_flag", DeleteFlag.NO.ordinal());

        return baseMapper.getListBySubstanceInfoId(queryWrapper);
    }


    /**
     * 修改危害因素限值
     *
     * @param stateLimitEntity
     * @return
     */
    @Override
    public Boolean updateLimit(SubstanceStateLimitEntity stateLimitEntity) {
        return this.updateById(stateLimitEntity);
    }


    /**
     * 逻辑删除危害因素限值
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteLimit(Long id) {

        return this.update(new UpdateWrapper<SubstanceStateLimitEntity>()
                .eq("id", id)
                .set("delete_flag", DeleteFlag.YES.ordinal()));
    }
}
