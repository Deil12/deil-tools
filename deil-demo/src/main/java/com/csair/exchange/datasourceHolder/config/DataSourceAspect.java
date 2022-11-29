package com.csair.exchange.datasourceHolder.config;

import com.csair.exchange.datasourceHolder.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @PURPOSE 
 * @DATE 2022/11/28
 * @COPYRIGHT © csair.com 
 */
@Component
@Slf4j
@Aspect
@Order(-1)
public class DataSourceAspect {

    /**
     * 设置切面范围
     */
    @Pointcut("@within(com.csair.exchange.datasourceHolder.config.DataSource) || " +
            "@annotation(com.csair.exchange.datasourceHolder.config.DataSource)")
    public void pointCut(){}

    /**
     * 添加数据源上下文
     * @param dataSource
     */
    @Before("pointCut() && @annotation(dataSource)")
    public void doBefore(DataSource dataSource){
        DataSourceContextHolder.setDataSource(dataSource.value().getValue());
    }

    /**
     * 清除数据源上下文
     */
    @After("pointCut()")
    public void doAfter(){
        DataSourceContextHolder.clear();
    }

}
