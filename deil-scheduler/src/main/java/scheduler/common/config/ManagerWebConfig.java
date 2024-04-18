package scheduler.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Configuration
public class ManagerWebConfig extends WebMvcConfigurationSupport/* implements WebMvcConfigurer*/ {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/");
        // registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 视图映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry相当于viewcontroller的注册中心，想让哪些请求跳到哪些页面，在这里注册就行了
        // urlPath：请求地址；等同于requestMapping的地址
        // viewName：视图名；controller中return的页面的名。

        // registry.addViewController("/index.html").setViewName("index");
        // registry.addViewController("/track.html").setViewName("track");
        // registry.addViewController("/sa-login.html").setViewName("sa-login");
    }

}
