package com.ruoyi.system.log.listen;

import com.ruoyi.system.log.entity.SysOperateLog;
import com.ruoyi.system.log.event.SysOperateLogEvent;
import com.ruoyi.system.log.service.SysOperateLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class SysOperateLogListener {

    private final SysOperateLogService sysOperateLogService;

    @Async
    @Order
    @EventListener(SysOperateLogEvent.class)
    public void listenOperLog(SysOperateLogEvent event) {
        SysOperateLog sysOperLog = (SysOperateLog) event.getSource();
        sysOperateLogService.save(sysOperLog);
        log.info("操作日志记录成功：{}", sysOperLog);
    }
}