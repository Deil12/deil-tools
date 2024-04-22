package org.deil.navigation.common;

import org.deil.login.common.domain.LoginProperties;
import org.deil.login.common.login.LoginConfigManager;
import org.deil.login.common.login.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * @see org.deil.login.common.LoginWebConfig
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LoginProperties loginProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(loginProperties.getInclude().split(","))
                .excludePathPatterns(loginProperties.getExclude().replaceAll(" ", "").split(","));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 视图映射，viewcontroller的注册中心
     * @param registry registry.addViewController("@RequestMapping地址").setViewName("return页面");
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.addViewController("/").setViewName("navigation");
        // registry.addViewController("/firework.html").setViewName("firework");
        // registry.addViewController("/rain_flower.html").setViewName("rain_flower");
    }

}

