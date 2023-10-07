package org.deil.utils.datasource;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(-1)
public class DataSourceAspect {

    /**
     * 设置切面范围
     */
    @Pointcut("@within(org.deil.utils.datasource.DataSource) || " +
            "@annotation(org.deil.utils.datasource.DataSource)")
    public void pointCut(){}

    /**
     * 添加数据源上下文
     * @param dataSource
     */
    @Before("pointCut() && @within(dataSource)")
    public void doBefore(DataSource dataSource){
        DataSourceContextHolder.setDataSource(dataSource.value().getValue());
    }

    /**
     * 清除数据源上下文
     */
    @After("pointCut() && @within(dataSource)")
    public void doAfter(DataSource dataSource){
        DataSourceContextHolder.clear();
    }

}
