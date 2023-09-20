package org.deil.gateway.common.config;

import org.deil.gateway.common.utils.SpringContextAwareUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimeZone;

@Slf4j
@Configuration
public class AppConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("\033[42;30m---------------------- 后 台 网 关 初 始 化 [{}] ----------------------\033[0m", SpringContextAwareUtil.getActiveProfile());
    }

    @PreDestroy
    public void destroy() {
        log.info("\033[42;31m---------------------- 后 台 网 关 注 销 ----------------------\033[0m");
    }

    /**
     * 网关跨域配置
     *
     * @return {@link CorsWebFilter }
     * @time 2023/06/12
     * @since 1.0.0
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        log.info("\033[0;32m跨域配置初始化\033[0m");
        return new CorsWebFilter(source);
    }

    /**
     * 使用redis进行IP限流
     *
     * @return {@link KeyResolver }
     * @time 2023/06/12
     * @since 1.0.0
     */
    @Bean
    public KeyResolver ipKeyResolver(){
        log.info("\033[0;32mIP限流初始化(基于Redis)\033[0m");
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName());
    }

    /**
     * XSS 过滤器
     *
     * @return {@link FilterRegistrationBean }
     * @time 2023/06/12
     * @since 1.0.0
     */
    /*//@Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new XssFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        log.info("\033[0;32mXSS 过滤器初始化\033[0m");
        return filterRegistrationBean;
    }*/

    /**
     * json 序列化
     *
     * @param builder
     * @return {@link ObjectMapper }
     * @time 2023/06/12
     * @since 1.0.0
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化
        // Include.NON_NULL 属性为NULL 不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        //json中多余的参数不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许出现单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        //设置全局的时间转化
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(smt);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));//解决时区差8小时问题
        log.info("\033[0;32mJSON 统一时间格式序列化初始化\033[0m");
        return objectMapper;
    }
}
