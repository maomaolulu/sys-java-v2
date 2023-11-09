package com.ruoyi.admin.schedule;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.admin.entity.SubstanceTestLawEntity;
import com.ruoyi.admin.service.SubstanceTestLawService;
import com.ruoyi.common.enums.Numbers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description 检测标准/法律法规表定时刷新状态
 * @author gy
 * @date 2023-06-25 10:18
 */
@Component
@Slf4j
public class SubstanceTestLawStatusJob {

    @Autowired
    private SubstanceTestLawService service;

    /**
     * 每月1号1点定时刷新法规或检测方法状态
     */
    @Scheduled(cron ="00 00 1 1 * ?")
    public void RefreshStatus(){
        Date now = DateUtil.date() ;
        SubstanceTestLawEntity entity = new SubstanceTestLawEntity();
        // 发布
        entity.setStatus(Numbers.TWO.ordinal());
        service.update(entity,new QueryWrapper<SubstanceTestLawEntity>().lt("implementation_date",now));
        // 现行
        entity.setStatus(Numbers.FIRST.ordinal());
        service.update(entity,new QueryWrapper<SubstanceTestLawEntity>().ge("implementation_date",now).and(warpper ->warpper.lt("abolition_date",now).or().isNull("abolition_date")));
        // 废止
        entity.setStatus(Numbers.ZERO.ordinal());
        service.update(entity,new QueryWrapper<SubstanceTestLawEntity>().ge("abolition_date",now));
        log.info("刷新法规或检测方法状态定时任务执行结束");
    }
}
