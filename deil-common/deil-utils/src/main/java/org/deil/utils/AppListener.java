package org.deil.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import javax.annotation.PreDestroy;

/**
 * @PURPOSE 应用程序配置类
 * @DATE 2022/11/30
 * @CODE Deil
 * @see ApplicationListener
 */
@Configuration
public class AppListener implements ApplicationListener<ApplicationReadyEvent/*ContextRefreshedEvent*/> {
    private Logger log = LoggerFactory.getLogger(AppListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("\033[42;30m---------------------- {} 系 统 初 始 化 ----------------------\033[0m",
                "[ " + event.getApplicationContext().getId() + " - (" + (!ObjectUtils.isEmpty(event.getApplicationContext().getEnvironment().getActiveProfiles()) ? event.getApplicationContext().getEnvironment().getActiveProfiles()[0] : event.getApplicationContext().getEnvironment().getDefaultProfiles()[0]) + ")]");
    }

    @PreDestroy
    public void destroy() {
        log.info("\033[42;31m---------------------- 系 统 注 销 ----------------------\033[0m");
    }
}
