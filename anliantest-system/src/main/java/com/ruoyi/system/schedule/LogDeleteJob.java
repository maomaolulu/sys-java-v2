package com.ruoyi.system.schedule;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.log.service.SysOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description 日志删除定时器
 * @Date 2023/5/16 13:06
 * @Author maoly
 **/
@Component
@Slf4j
public class LogDeleteJob {

    @Autowired
    private SysOperateLogService sysOperateLogService;

    /**
     * 每月1号 01:30执行，删除6个月前的日志记录
     */
    /*@Scheduled(cron ="0 30 01 1 * ?")
    public void task(){
        String preDate = DateUtils.getFixDate(Constants.PRI_NOW_DATA_INT);
        int count = sysOperateLogService.deleteByCreateTime(preDate);
        log.info("删除日志记录结束，删除条数:{}",count);
    }*/
}
