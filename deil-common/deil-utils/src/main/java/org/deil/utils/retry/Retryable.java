package org.deil.utils.retry;

import java.lang.annotation.*;

/**
 * 
 *
 * @DATE 2023/09/22
 * @CODE Deil
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retryable {

    // 最大重试次数
    int retryTimes() default 3;
    // 重试间隔
    int retryInterval() default 1;

}
