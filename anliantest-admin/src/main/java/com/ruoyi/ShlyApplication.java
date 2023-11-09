package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description
 * @Date 2023/5/4 17:30
 * @Author maoly
 **/
@SpringBootApplication
@EnableScheduling
public class ShlyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ShlyApplication.class, args);

        System.out.println("##########上海量远服务启动成功#################");

    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShlyApplication.class);
    }
}