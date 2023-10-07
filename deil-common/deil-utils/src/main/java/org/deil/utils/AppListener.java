package org.deil.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Configuration
public class AppListener implements ApplicationListener<ApplicationReadyEvent/*ContextRefreshedEvent*/> {
    private Logger log = LoggerFactory.getLogger(AppListener.class);

    //@Resource
    //private Environment env;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("\033[42;30m---------------------- {} 系 统 初 始 化 ----------------------\033[0m",
                "[ " + event.getApplicationContext().getId() + " ( " + (ObjectUtils.isEmpty(event.getApplicationContext().getEnvironment().getActiveProfiles()) ? event.getApplicationContext().getEnvironment().getDefaultProfiles()[0] : event.getApplicationContext().getEnvironment().getActiveProfiles()[0]) + " ) ]");
                //"[ " + StringUtils.defaultIfEmpty(env.getProperty("spring.application.name"), "") + " ( " + StringUtils.defaultIfEmpty(env.getProperty("spring.profiles.active"), env.getDefaultProfiles()[0]) + " ) ]");
    }

    @PreDestroy
    public void destroy() {
        log.info("\033[42;31m---------------------- 系 统 注 销 ----------------------\033[0m");
    }
}
