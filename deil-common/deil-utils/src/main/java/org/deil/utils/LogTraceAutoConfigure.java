package org.deil.utils;

import org.deil.utils.pojo.properties.LogProperties;
import org.deil.utils.log.LogConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class LogTraceAutoConfigure {
    private Logger log = LoggerFactory.getLogger(LogTraceAutoConfigure.class);

    @Bean
    public LogConfigurer logConfigurer() {
        log.info("[\033[0;32m日志链路初始化\033[0m]");
        return new LogConfigurer();
    }

}