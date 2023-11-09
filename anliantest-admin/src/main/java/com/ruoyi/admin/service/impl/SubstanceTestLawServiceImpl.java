package com.ruoyi.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.SubstanceTestLawDto;
import com.ruoyi.admin.entity.SubstanceTestLawEntity;
import com.ruoyi.admin.entity.SubstanceTestMethodEntity;
import com.ruoyi.admin.mapper.SubstanceTestLawMapper;
import com.ruoyi.admin.service.SubstanceTestLawService;
import com.ruoyi.admin.service.SubstanceTestMethodService;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.enums.Numbers;
import com.ruoyi.common.utils.ObjectUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.login.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
@Service
public class SubstanceTestLawServiceImpl extends ServiceImpl<SubstanceTestLawMapper, SubstanceTestLawEntity> implements SubstanceTestLawService {

    @Autowired
    private SubstanceTestMethodService substanceTestMethodService;

    @Override
    public List<SubstanceTestLawEntity> listByCondition(SubstanceTestLawDto substanceTestLawdto){
        return this.list(getSelectWrapper(substanceTestLawdto));
    }

    @Override
    public Boolean saveByCondition(SubstanceTestLawDto substanceTestLawDto){
        SubstanceTestLawEntity entity = ObjectUtils.transformObj(substanceTestLawDto,SubstanceTestLawEntity.class);
        entity.setCreateBy(ShiroUtils.getUserName());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        setStatusByNowtime(entity);
        return this.save(entity);
    }

    @Override
    public Boolean updateByCondition(SubstanceTestLawDto substanceTestLawDto){
        SubstanceTestLawEntity entity = ObjectUtils.transformObj(substanceTestLawDto,SubstanceTestLawEntity.class);
        entity.setUpdateBy(ShiroUtils.getUserName());
        entity.setUpdateTime(new Date());
        setStatusByNowtime(entity);
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByCondition(Long id){
        SubstanceTestLawEntity entity = this.getById(id);
        entity.setDeleteFlag(DeleteFlag.YES.ordinal());
        if (ObjectUtil.isNotNull(entity.getTestCategory())){
            // TODO 逻辑删除物质检测方法,若关联资质则不可删除
            SubstanceTestMethodEntity method = new SubstanceTestMethodEntity();
            method.setDeleteFlag(DeleteFlag.YES.ordinal());
            substanceTestMethodService.update(method,new QueryWrapper<SubstanceTestMethodEntity>().eq("substance_test_law_id",id));
        }
        return updateById(entity);
    }

    private QueryWrapper<SubstanceTestLawEntity> getSelectWrapper(SubstanceTestLawDto substanceTestLawdto){
        return new QueryWrapper<SubstanceTestLawEntity>()
                .eq(ObjectUtil.isNotNull(substanceTestLawdto.getId()),"id",substanceTestLawdto.getId())
                .eq(StringUtils.isNotBlank(substanceTestLawdto.getTestStandards()),"test_standards",substanceTestLawdto.getTestStandards())
                .eq(StringUtils.isNotBlank(substanceTestLawdto.getTestStandardsName()),"test_standards_name",substanceTestLawdto.getTestStandardsName())
                .eq(StringUtils.isNotBlank(substanceTestLawdto.getLegalEffect()),"legal_effect",substanceTestLawdto.getLegalEffect())
                .eq("delete_flag",Numbers.ZERO.ordinal())
                .eq(ObjectUtil.isNotNull(substanceTestLawdto.getStatus()),"status",substanceTestLawdto.getStatus())
                .isNotNull(substanceTestLawdto.getStandCategory() == Numbers.ZERO.ordinal(),"test_category")
                .isNull(substanceTestLawdto.getStandCategory() == Numbers.FIRST.ordinal(),"test_category")
                .orderByDesc("update_time");
    }

    private void setStatusByNowtime(SubstanceTestLawEntity entity){
        Date now = new Date();
        Date startDate = entity.getImplementationDate();
        Date endDate = entity.getAbolitionDate();
        if (ObjectUtil.isNotNull(startDate)){
            if (now.getTime() < startDate.getTime()){
                entity.setStatus(Numbers.TWO.ordinal());
            }else {
                entity.setStatus(Numbers.FIRST.ordinal());
                if (ObjectUtil.isNotNull(endDate)){
                    if (now.getTime() > endDate.getTime()){
                        entity.setStatus(Numbers.ZERO.ordinal());
                    }
                }
            }
        }

    }
}
