package org.deil.utils.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class SpringContextAwareUtil implements ApplicationContextAware {

    /**
     * Spring上下文环境
     */
    private static ApplicationContext applicationContext;

    @Override
    @SuppressWarnings("NullableProblems")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取Bean对象（不能在容器未完全构建时调用，那时 setApplicationContext() 方法还未到调用时间）
     *
     * @param name BeanName
     * @return 一个以所给名字注册的bean的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获取Bean对象（不能在容器未完全构建时调用，那时 setApplicationContext() 方法还未到调用时间）
     *
     * @param clz BeanType(类型)
     * @return 一个与所给类型相同的Bean实例
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        return (T) applicationContext.getBean(clz);
    }

    /**
     * 获取Bean对象（根据Bean名称和类型）（不能在容器未完全构建时调用，那时 setApplicationContext() 方法还未到调用时间）
     *
     * @param name BeanName
     * @param clz  BeanType(类型)
     * @return 一个与所给类型相同的Bean实例
     */
    public static <T> T getBean(String name, Class<T> clz) throws BeansException {
        return (T) applicationContext.getBean(name, clz);
    }

    /**
     * 判断一个Bean是否存在于容器，如果容器内包含一个与所给名称匹配的bean定义，则返回true，否则false
     *
     * @param name BeanName
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的Bean定义是一个singleton(单例)还是一个prototype(多例)，
     * 如果与给定名字相应的Bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name BeanName
     * @return boolean
     * @throws NoSuchBeanDefinitionException 没有Bean的定义
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * 获取Bean的类型定义
     *
     * @param name BeanName
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException 没有Bean的定义
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name BeanName
     * @return 别名列表
     * @throws NoSuchBeanDefinitionException 没有Bean的定义
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

    /**
     * 获取aop代理对象
     *
     * @param invoker 需要代理的Bean对象
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }

    /**
     * 获取当前环境
     * @return
     */
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取多个相同类型的Bean
     *
     * @param clz 类型Class
     * @param <T> 泛型
     * @return Bean列表
     */
    public static <T> Map<String, T> getMultiBean(Class<T> clz) {
        return applicationContext.getBeansOfType(clz);
    }

    /**
     * 事件发布
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    /**
     * 获取配置
     *
     * @return 当前运行环境配置
     */
    public static Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

    /**
     * 国际化使用
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return applicationContext.getMessage(key, null, Locale.getDefault());
    }
}
