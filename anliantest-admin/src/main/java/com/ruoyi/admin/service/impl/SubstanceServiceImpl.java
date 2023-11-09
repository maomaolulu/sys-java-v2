package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.vo.SubstanceVo;
import com.ruoyi.admin.entity.SubstanceDetectionEntity;
import com.ruoyi.admin.entity.SubstanceEntity;
import com.ruoyi.admin.mapper.SubstanceMapper;
import com.ruoyi.admin.service.SubStanceDetectionService;
import com.ruoyi.admin.service.SubstanceService;
import com.ruoyi.common.datasource.DynamicDataSourceContextHolder;
import com.ruoyi.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhuYiCheng
 * @date 2023/4/24 10:52
 */
@Service
public class SubstanceServiceImpl extends ServiceImpl<SubstanceMapper, SubstanceEntity> implements SubstanceService {

    @Autowired
    private SubstanceMapper substanceMapper;
    @Autowired
    private SubStanceDetectionService subStanceDetectionService;

    /**
     * 获取检测项目列表
     */
    @Override
    public List<SubstanceVo> getSubstanceList() {

        List<SubstanceVo> substanceVoList = substanceMapper.getSubstanceList();

        BigDecimal b100 = new BigDecimal(100);
        BigDecimal b400 = new BigDecimal(400);
        for (SubstanceVo substanceVo : substanceVoList) {
            if(substanceVo.getQualification()==null){
                substanceVo.setQualification(2);
                substanceVo.setLabSource("上海量远");
            }
            if (substanceVo.getSType() == 1 || substanceVo.getSType() == 2) {
                substanceVo.setUnitPrice(b400);
            }else {
                substanceVo.setUnitPrice(b100);
            }
        }
        return substanceVoList;
    }

    @Override
    public List<SubstanceEntity> getAllAndList(){
        List<SubstanceEntity> list1 = this.list();
        List<SubstanceDetectionEntity> list2 = subStanceDetectionService.list();
        Map<Long,List<SubstanceDetectionEntity>> map = list2.stream().collect(Collectors.groupingBy(SubstanceDetectionEntity::getSubstanceId));
        list1.forEach(substanceEntity -> {
            substanceEntity.setSubstanceDetectionEntities(map.get(substanceEntity.getId()));
        });
        return list1;
    }
}
