package com.ruoyi.system.log;

import com.ruoyi.system.log.listen.SysOperateLogListener;
import com.ruoyi.system.log.service.SysOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author maoly
 * @date 2023/4/26 日志自动配置
 */
@EnableAsync
@RequiredArgsConstructor
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
public class LogAutoConfiguration {

	@Bean
	public SysOperateLogListener sysLogListener(SysOperateLogService sysOperateLogService) {
		return new SysOperateLogListener(sysOperateLogService);
	}

}
