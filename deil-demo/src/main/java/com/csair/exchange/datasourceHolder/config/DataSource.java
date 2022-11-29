package com.csair.exchange.datasourceHolder.config;

import com.csair.exchange.datasourceHolder.DataSourceEnum;

import java.lang.annotation.*;

/**
 * @PURPOSE 
 * @DATE 2022/11/28
 * @COPYRIGHT © csair.com
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceEnum value() default DataSourceEnum.dataSourceMysql;
}
