package org.deil.utils.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *
 * @DATE 2023/09/23
 * @CODE Deil
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Upload {
    // 记录上传类型
    UploadType type() default UploadType.未知;
}
