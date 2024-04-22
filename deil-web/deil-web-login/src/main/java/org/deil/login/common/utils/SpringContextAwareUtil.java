package org.deil.login.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Locale;

@Component
public class SpringContextAwareUtil implements ApplicationContextAware {
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    /**
     * 国际化使用
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    /**
     * 获取当前环境
     * @return
     */
    public static String getActiveProfile() {
        return !ObjectUtils.isEmpty(context.getEnvironment().getActiveProfiles()) ? context.getEnvironment().getActiveProfiles()[0] : context.getEnvironment().getDefaultProfiles()[0];

    }
}
