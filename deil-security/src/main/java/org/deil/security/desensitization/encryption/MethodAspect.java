package org.deil.security.desensitization.encryption;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.deil.security.desensitization.encryption.annotation.EncryptMethod;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Base64;

/**
 * 方法方面
 *
 * @DATE 2023/01/02
 * @CODE Deil
 */
@Slf4j
@Aspect
@Component
public class MethodAspect {

    @Around(value = "(@annotation(org.deil.security.desensitization.encryption.annotation.EncryptMethod))")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!(joinPoint.getSignature() instanceof MethodSignature)) {
            return joinPoint.proceed();
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        EncryptMethod encryptMethod = method.getAnnotation(EncryptMethod.class);
        if (encryptMethod == null) {
            return joinPoint.proceed();
        }
        String[] parameterNames = methodSignature.getParameterNames();
        int index = getEncryptionIndex(parameterNames, encryptMethod.value());
        if (index == -1) {
            log.info("获取加密索引失败。。");
            return joinPoint.proceed();
        }
        Object[] args = joinPoint.getArgs();
        args[index] = encryption((String) args[index]);
        return joinPoint.proceed(args);
    }

    /**
     * 得到字段索引
     *
     * @param parameterNames 参数名称
     * @param encryptionKey  加密密钥
     * @return int
     * @TIME 2023/01/02
     */
    private int getEncryptionIndex(String[] parameterNames, String encryptionKey) {
        for (int i = 0; i < parameterNames.length; i++) {
            if (encryptionKey.equals(parameterNames[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 加密逻辑
     *
     * @param value 价值
     * @return {@link String }
     * @TIME 2023/01/02
     */
    private String encryption(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

}
