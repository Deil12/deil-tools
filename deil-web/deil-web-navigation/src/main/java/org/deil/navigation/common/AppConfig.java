package org.deil.navigation.common;

import org.deil.login.common.utils.SpringContextAwareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import javax.annotation.PreDestroy;

@Configuration
public class AppConfig implements ApplicationListener<ContextRefreshedEvent> {
    private Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("\033[42;30m---------------------- 页 面 服 务 初 始 化 [{}] ----------------------\033[0m", SpringContextAwareUtil.getActiveProfile());
    }

    @PreDestroy
    public void destroy() {
        log.info("\033[42;31m---------------------- 页 面 服 务 注 销 ----------------------\033[0m");
    }

    /**
     * 网关跨域配置
     *
     * @return {@link CorsWebFilter }
     * @since 1.0.0
     */
    // @Bean
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

    // @Bean
    // @Order(-101)
    // LoginServletFilter getSaServletFilterForQuickLogin() {
    //     return new LoginServletFilter()
    //             .addInclude("/**")// 拦截路由
    //             .addExclude("/favicon.ico", "/saLogin", "/doLogin", "/sa-res/**")// 排除掉登录相关接口，不需要鉴权的
    //             .setAuth(obj ->
    //                     LoginRouter
    //                             .match(LoginConfigManager.getConfig().getInclude().split(","))
    //                             .notMatch(LoginConfigManager.getConfig().getExclude().split(","))
    //                             .check(r -> {
    //                                 // 未登录时直接转发到login.html页面
    //                                 if (LoginQuickManager.getConfig().isEnabled() && !StpUtil.isLogin()) {
    //                                     // log.info("\033[0;32;4m切换登陆...\033[0m");
    //                                     LoginHolder.getRequest().forward("/saLogin");
    //                                     LoginRouter.back();
    //                                 }
    //                             })
    //             )// 认证函数: 每次请求执行
    //             .setError(e -> {
    //                 e.printStackTrace();
    //                 return e.getMessage();
    //             });// 异常处理函数：每次认证函数发生异常时执行此函数
    // }

}
