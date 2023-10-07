package org.deil.utils.log;

import java.lang.annotation.*;

@Deprecated
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogTrace {

    String name() default "";

}
