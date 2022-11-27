package org.deil.utils.log;

import lombok.extern.slf4j.Slf4j;
import org.deil.utils.log.aspect.MethodLogAspect;
import org.deil.utils.log.aspect.WebLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @PURPOSE 日志配置类
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Slf4j
@ComponentScan("org.deil")
public class LogConfig {

    public LogConfig() {
        log.info("启动自定义日志配置");
    }

    /**
     * @return {@link MethodLogAspect }
     * @time Time() : 方法日志切面
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean(MethodLogAspect.class)
    @ConditionalOnExpression("${log.methodlog.enable:false}")
    public MethodLogAspect methodLogAspect() {
        log.info("注入方法日志切面");
        return new MethodLogAspect();
    }

    /**
     * @return {@link WebLogAspect }
     * @time Time() : 开放接口日志切面
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean(WebLogAspect.class)
    @ConditionalOnExpression("${log.weblog.enable:false}")
    public WebLogAspect webLogAspect() {
        log.info("注入开放接口日志切面");
        return new WebLogAspect();
    }

}
