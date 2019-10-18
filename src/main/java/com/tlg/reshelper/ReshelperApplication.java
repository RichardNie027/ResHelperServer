package com.tlg.reshelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.tlg.reshelper.controller","com.tlg.reshelper.service.impl","com.nec.lib.springboot","com.tlg.reshelper.interceptor","com.tlg.reshelper.conf"}, exclude = {DataSourceAutoConfiguration.class})    //扫描controller和service
@ServletComponentScan
@EnableTransactionManagement
public class ReshelperApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ReshelperApplication.class, args);
    }

    // 继承SpringBootServletInitializer 实现configure 方便打war 外部服务器部署
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReshelperApplication.class);
    }

}
