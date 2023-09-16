package org.deil.utils.utils.annotation;

import java.lang.annotation.*;

/**
 * 用于标记数据库表名，支持拓展
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {

    /**
     * 表名称
     */
    String value() default "";

    /**
     * 主键类型
     */
    KeyType keyType() default KeyType.AUTO;




    /**
     * 主键类型
     */
    enum KeyType {

        /**
         * 自增主键
         */
        AUTO,

        /**
         * 单个字段主键
         */
        SINGLE,

        /**
         * 多字段主键
         */
        MULTI,

        /**
         * 无主键
         */
        NONE

    }

}
