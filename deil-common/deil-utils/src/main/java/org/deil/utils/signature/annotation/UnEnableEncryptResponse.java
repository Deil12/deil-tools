package org.deil.utils.signature.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 取消响应加签（接口响应默认加密）
 *
 * @DATE 2023/03/11
 * @CODE Deil
 */
@Deprecated
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnEnableEncryptResponse {
}
