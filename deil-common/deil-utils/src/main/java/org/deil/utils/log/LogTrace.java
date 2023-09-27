package org.deil.utils.log;

import java.lang.annotation.*;

/**
 * 
 *
 * @DATE 2023/09/17
 * @CODE Deil
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogTrace {

    String name() default "";

}
