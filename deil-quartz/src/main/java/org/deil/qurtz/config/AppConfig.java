//package org.deil.qurtz.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.core.env.Environment;
//
//import javax.annotation.PreDestroy;
//
///**
// * @PURPOSE 应用程序配置类
// * @DATE 2022/11/27
// * @COPYRIGHT © Deil
// */
//@Configuration
//public class AppConfig implements ApplicationListener<ContextRefreshedEvent> {
//    private Logger log = LoggerFactory.getLogger(AppConfig.class);
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        log.info("---------------------- 系 统 初 始 化 ----------------------");
//    }
//
//    @PreDestroy
//    public void destroy() {
//        log.info("---------------------- 系 统 注 销 ----------------------");
//    }
//
//}
