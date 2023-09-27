package org.deil.utils.log;

import java.lang.annotation.*;

/**
 * @PURPOSE 日志
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    String name() default "";

}
