package org.deil.utils.log;

import org.deil.utils.pojo.properties.LogProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.UUID;

/**
 * 添加拦截器
 * 自定义拦截器；在请求中添加logId值和requestTimestamp值
 * 并对头部Accept-Language进行检查
 *
 * @TIME 2023/01/19
 * @CODE Deil
 */
@Deprecated
public class LogIdInterceptor implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(LogIdInterceptor.class);

    private LogProperties properties;

    public void setProperties(LogProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("logId", UUID.randomUUID().toString().replace("-", ""));
        request.setAttribute("requestTime", System.currentTimeMillis());
        if (ObjectUtils.isEmpty(request.getHeader("Accept-Language"))) {
            //设置当前的请求的语言类型，并写入到cookie中去
            LocaleContextHolder.resetLocaleContext();
            LocaleContextHolder.setLocale(Locale.CHINA);
        }
        return true;
    }

}
