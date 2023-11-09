package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.entity.IndustryEntity;
import com.ruoyi.admin.mapper.IndustryMapper;
import com.ruoyi.admin.service.IndustryService;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("industryService")
public class IndustryServiceImpl extends ServiceImpl<IndustryMapper, IndustryEntity> implements IndustryService {


    /**
     * 获取行业类别列表
     * @param joint
     * @return
     */
    @Override
    public List<IndustryEntity> listJoint(String joint) {
        ArrayList<IndustryEntity> list = new ArrayList<>();
        if (StringUtils.isBlank(joint)){
//        if (joint=="" || joint==null){
            List<IndustryEntity> industryEntities = this.list(new QueryWrapper<IndustryEntity>()
                    .like("joint", "C")
                    .last("limit 50")
            );
            list.addAll(industryEntities);
        }else {
            List<IndustryEntity> list1 = this.list(new QueryWrapper<IndustryEntity>()
                    .like("joint", joint)
            );
            list.addAll(list1);
        }
        return list;
    }
}
