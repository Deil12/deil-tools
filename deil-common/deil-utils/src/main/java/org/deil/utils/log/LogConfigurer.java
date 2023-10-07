package org.deil.utils.log;

import org.apache.commons.lang3.StringUtils;
import org.deil.utils.pojo.properties.LogProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

@EnableWebMvc
@Configuration
public class LogConfigurer implements WebMvcConfigurer {
    private Logger log = LoggerFactory.getLogger(LogConfigurer.class);

    private LogProperties properties;

    @Resource
    public void setProperties(LogProperties properties) {
        this.properties = properties;
    }

    //@Bean
    //public HandlerInterceptor logIdHodlerInterceptorInterceptor() {
    //    LogIdInterceptor interceptor = new LogIdInterceptor();
    //    interceptor.setProperties(properties);
    //    return interceptor;
    //}

    //@Bean
    //public LogTraceInterceptor logInterceptor() {
    //    LogTraceInterceptor interceptor = new LogTraceInterceptor();
    //    interceptor.setProperties(properties);
    //    return new LogTraceInterceptor();
    //}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            private static final String LOG_ID = "logId";
            private static final String REQUEST_TIME = "requestTime";

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String tid = UUID.randomUUID().toString().replace("-", "");
                //可以考虑让客户端传入链路ID，但需保证一定的复杂度唯一性；如果没使用默认UUID自动生成
                if (!StringUtils.isEmpty(request.getHeader(LOG_ID))) {
                    tid = request.getHeader(LOG_ID);
                }
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                if (properties.isEnabled() &&
                        (hasTargetAnnotation(handlerMethod.getBeanType(), /*LogTrace*/Log.class)
                                || hasTargetAnnotation(handlerMethod.getMethod(), /*LogTrace*/Log.class))) {
                    MDC.put(LOG_ID, "[" + tid + "]");
                }

                request.setAttribute(LOG_ID, tid);
                request.setAttribute(REQUEST_TIME, System.currentTimeMillis());
                if (ObjectUtils.isEmpty(request.getHeader("Accept-Language"))) {
                    //设置当前的请求的语言类型，并写入到cookie中去
                    LocaleContextHolder.resetLocaleContext();
                    LocaleContextHolder.setLocale(Locale.CHINA);
                }
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler,
                                        @Nullable Exception ex) {
                MDC.remove(LOG_ID);
            }
        })
        //具体制定哪些需要拦截，哪些不拦截
        //.addPathPatterns("/**")
        //.excludePathPatterns("/testxx.html")
        ;
    }

    /**
     * 是否存在注解
     *
     * @param method            将要访问的接口方法对象
     * @param annotationClasses 注解字节码（一个成功即可，多个注解在里面存在的关系是 或 ）
     * @return 是否存在注解
     */
    @SafeVarargs
    private final boolean hasTargetAnnotation(Method method, Class<? extends Annotation>... annotationClasses) {
        for (Class<? extends Annotation> annotationClz : annotationClasses) {
            if (method.isAnnotationPresent(annotationClz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在注解
     *
     * @param clz               将要访问的接口类
     * @param annotationClasses 注解字节码（一个成功即可，多个注解在里面存在的关系是 或 ）
     * @return 是否存在注解
     */
    @SafeVarargs
    private final boolean hasTargetAnnotation(Class<?> clz, Class<? extends Annotation>... annotationClasses) {
        for (Class<? extends Annotation> annotationClz : annotationClasses) {
            if (clz.isAnnotationPresent(annotationClz)) {
                return true;
            }
        }
        return false;
    }

}
