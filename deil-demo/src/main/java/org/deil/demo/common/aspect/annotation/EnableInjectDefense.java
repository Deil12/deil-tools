package org.deil.demo.common.aspect.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableInjectDefense {

    String value() default "";

}
