//package org.deil.gateway.config;
//
//import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import reactor.core.publisher.Mono;
//
//import java.util.Objects;
//
///**
// * 使用redis进行限流配置
// */
//@Configuration
//public class RedisRateLimiterConfig {
//    /**
//     * 使用ip地址进行限流
//     */
//    @Bean
//    public KeyResolver ipKeyResolver(){
//        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName());
//    }
//}
