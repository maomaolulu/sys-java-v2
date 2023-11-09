package com.ruoyi.system.log.event;

import com.ruoyi.system.log.entity.SysOperateLog;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 * @author maoly
 */
public class SysOperateLogEvent extends ApplicationEvent {

    public SysOperateLogEvent(SysOperateLog source)
    {
        super(source);
    }
}