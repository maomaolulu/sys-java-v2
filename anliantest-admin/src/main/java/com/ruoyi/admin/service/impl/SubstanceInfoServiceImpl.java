package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.vo.SubstanceInfoVo;
import com.ruoyi.admin.entity.SubstanceInfoEntity;
import com.ruoyi.admin.entity.SubstanceTestMethodEntity;
import com.ruoyi.admin.mapper.SubstanceInfoMapper;
import com.ruoyi.admin.service.SubstanceInfoService;
import com.ruoyi.admin.service.SubstanceTestMethodService;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.utils.pageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description
 * @Date 2023/6/6 9:15
 * @Author maoly
 **/
@Service
public class SubstanceInfoServiceImpl extends ServiceImpl<SubstanceInfoMapper, SubstanceInfoEntity> implements SubstanceInfoService {

    @Autowired
    private SubstanceTestMethodService substanceTestMethodService;

    /**
     * 新增危害因素
     *
     * @param substanceInfo
     * @return
     */
    @Override
    public Boolean addSubstanceInfo(SubstanceInfoEntity substanceInfo) {

        return this.save(substanceInfo);
    }

    /**
     * 获取危害因素列表
     *
     * @param substanceInfoVo
     * @return
     */
    @Override
    public List<SubstanceInfoEntity> getSubstanceInfoList(SubstanceInfoVo substanceInfoVo) {
        String substanceNameOrOtherName = substanceInfoVo.getSubstanceNameOrOtherName();
        String casCode = substanceInfoVo.getCasCode();
        Integer substanceType = substanceInfoVo.getSubstanceType();

        pageUtil.startPage();
        List<SubstanceInfoEntity> list = this.list(new QueryWrapper<SubstanceInfoEntity>()
//                .and(wrapper -> wrapper.like(substanceNameOrOtherName != null, "substance_name", substanceNameOrOtherName)
//                        .or().like(substanceNameOrOtherName != null, "substance_other_name", substanceNameOrOtherName))
                .like(substanceNameOrOtherName != null, "substance_name", substanceNameOrOtherName)
                .or().like(substanceNameOrOtherName != null, "substance_other_name", substanceNameOrOtherName)
                .like(casCode != null, "cas_code", casCode)
                .eq(substanceType != null, "substance_type", substanceType)
                .eq("delete_flag",DeleteFlag.NO.ordinal())
                .orderByDesc("update_time"));
        return list;
    }


    /**
     * 获取危害因素详情
     *
     * @param id
     * @return
     */
    @Override
    public SubstanceInfoEntity getSubstanceInfo(Long id) {

        return this.getById(id);
    }

    /**
     * 修改危害因素信息
     *
     * @param substanceInfo
     * @return
     */
    @Override
    public Boolean updateSubstanceInfo(SubstanceInfoEntity substanceInfo) {

        return this.updateById(substanceInfo);
    }

    /**
     * 逻辑删除危害因素信息
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSubstanceInfo(Long id) {

        // TODO 逻辑删除物质检测方法,若关联资质则不可删除
        //逻辑删除危害因素相关的检测方法
        SubstanceTestMethodEntity method = new SubstanceTestMethodEntity();
        method.setDeleteFlag(DeleteFlag.YES.ordinal());
        substanceTestMethodService.update(method,new QueryWrapper<SubstanceTestMethodEntity>().eq("substance_test_law_id",id));

        return this.update(new UpdateWrapper<SubstanceInfoEntity>()
                .eq("id", id)
                .set("delete_flag", DeleteFlag.YES.ordinal()));
    }
}
