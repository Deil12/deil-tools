package org.deil.utils.datasource.aspect.annotation;

import org.deil.utils.datasource.domain.DataSourceEnum;
import java.lang.annotation.*;

/**
 * @PURPOSE
 * 自定义的注解
 * 可以在 mapper 或者 service 中使用
 * 可以用于方法，也可以用于类
 *
 * 本注解用于切换数据库 例 @DataSource("dataSourceMsql")
 * @DATE 2022/11/24
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceEnum value() default DataSourceEnum.dataSourceMysql;
}