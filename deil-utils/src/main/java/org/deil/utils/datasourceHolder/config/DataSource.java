package org.deil.utils.datasourceHolder.config;

import org.deil.utils.datasourceHolder.DataSourceEnum;

import java.lang.annotation.*;

/**
 * @PURPOSE 
 * @DATE 2022/11/28
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceEnum value() default DataSourceEnum.MYSQL;
}
