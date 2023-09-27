package org.deil.gatewaynode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(GatewayProperties.class)
public class GatewayAutoConfigure {
    private Logger log = LoggerFactory.getLogger(GatewayAutoConfigure.class);

    @Bean
    public GatewayInterceptorConfigurer gatewayInterceptorConfigurer() {
        log.info("\033[0;32m请求拦截器初始化\033[0m");
        return new GatewayInterceptorConfigurer();
    }

    /*@Bean
    public XSSReflectAspect xssReflectAspect(filter.CargoSecurityProperties cargoSecurityProperties) {
        return new XSSReflectAspect(cargoSecurityProperties);
    }*/

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new XssFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        log.info("\033[0;32mXSS 过滤器初始化\033[0m");
        return filterRegistrationBean;
    }
}