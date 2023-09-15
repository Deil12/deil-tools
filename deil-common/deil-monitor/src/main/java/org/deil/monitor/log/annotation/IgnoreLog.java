package org.deil.monitor.log.annotation;

import java.lang.annotation.*;

/**
 * @PURPOSE 忽略日志
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreLog {

}
