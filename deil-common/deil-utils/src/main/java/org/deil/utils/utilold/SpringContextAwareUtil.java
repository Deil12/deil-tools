//package org.deil.utils.util;
//
//import lombok.experimental.UtilityClass;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
//import java.util.Locale;
//
///**
// * @PURPOSE
// * @DATE 2022/09/08
// */
//@Component
//@UtilityClass
//public class SpringContextAwareUtil implements ApplicationContextAware {
//
//    private static ApplicationContext context = null;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        context = applicationContext;
//    }
//    /**
//     * 国际化使用
//     * @param key
//     * @return
//     */
//    public static String getMessage(String key) {
//        return context.getMessage(key, null, Locale.getDefault());
//    }
//
//    /**
//     * 获取当前环境
//     * @return
//     */
//    public static String getActiveProfile() {
//        return context.getEnvironment().getActiveProfiles()[0];
//    }
//}
