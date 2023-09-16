package org.deil.utils.utils.annotation;

import java.lang.annotation.*;

/**
 * 用于标记数据库字段内容
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Field {

    /**
     * 数据表字段名
     */
    String value() default "";

    /**
     * 是否主键
     */
    boolean isKey() default false;

    /**
     * 默认值（只做一个判断，就是当属性值等于默认值时，不作为条件拼接）
     */
    String defaultValue() default "";

}
