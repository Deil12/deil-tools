package org.deil.utils.datasource;

import org.deil.utils.datasource.DataSourceEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceEnum value() default DataSourceEnum.MYSQL;
}
