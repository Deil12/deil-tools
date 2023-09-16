package org.deil.utils.signature.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用请求解签（接口请求默认不启用）
 *
 * @DATE 2023/03/11
 * @CODE Deil
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnEnableDecryptRequest {
}
