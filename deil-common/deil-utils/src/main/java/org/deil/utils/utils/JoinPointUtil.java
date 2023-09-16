package org.deil.utils.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

@UtilityClass
public class JoinPointUtil {

    public static <T> T getParamByName(JoinPoint joinPoint, String paramName, Class<T> clazz) {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        int index = ArrayUtils.indexOf(parameterNames, paramName);
        Object obj = args[index];
        if (obj == null) {
            return null;
        }
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }
        return (T) obj;
    }

}
