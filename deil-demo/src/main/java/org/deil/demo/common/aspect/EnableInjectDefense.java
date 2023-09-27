package org.deil.demo.common.aspect;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableInjectDefense {

    String value() default "";

}
