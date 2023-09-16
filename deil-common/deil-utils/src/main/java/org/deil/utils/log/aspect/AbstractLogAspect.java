package org.deil.utils.log.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.deil.utils.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * @PURPOSE 摘要日志方面
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Slf4j
public abstract class AbstractLogAspect {

    @Autowired
    private ObjectMapper objectMapper;

    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder(getRequestLogFullInfo(proceedingJoinPoint));
        long startTime = System.currentTimeMillis();
        //执行目标方法
        Object proceed = proceedingJoinPoint.proceed();
        long costTime = System.currentTimeMillis() - startTime;
        String returnType = "";
        String returnValue = "null";
        if (returnValue != null) {
            returnType = proceed.getClass().getSimpleName();
            returnValue = objectMapper.writeValueAsString(proceed);
        }
        stringBuilder.append("====================< COMPLETE! >")
                .append(" RETURNTYPE: ").append(returnType)
                .append(", RETURNVALUE: ").append(returnValue)
                .append(", TIMECOST: ").append(costTime).append("ms");
        log.info(stringBuilder.toString());
        return proceed;
    }

    protected String getRequestLogFullInfo(JoinPoint joinPoint){
        StringBuilder stringBuilder = new StringBuilder();
        String joinPointLogInfoStr = getJoinPointLogInfoStr(joinPoint);
        if (!StringUtils.isEmpty(joinPointLogInfoStr)) {
            stringBuilder.append(joinPointLogInfoStr);
        }
        return stringBuilder.toString();
    }

    protected String getJoinPointLogInfoStr(JoinPoint joinPoint) {
        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String name = method.getName();
            String[] parameterNames = methodSignature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            StringBuilder stringBuilder;
            Log logDesc = method.getAnnotation(Log.class);
            if (logDesc != null && !StringUtils.isEmpty(logDesc.name())) {
                stringBuilder = new StringBuilder("EXECUTING METHOD NAME: " + logDesc.name());
            } else {
                stringBuilder = new StringBuilder("EXECUTING METHOD NAME: " + name);
            }
            stringBuilder.append(" [");
            if (parameterNames != null && parameterNames.length > 0) {
                //遍历拼接
                for (int i = 0; i < parameterNames.length; i++) {
                    String parameterName = parameterNames[i];
                    stringBuilder.append(parameterName).append(" = ");
                    try {
                        if (args[i] != null) {
                            stringBuilder.append(objectMapper.writeValueAsString(args[i]));
                        } else {
                            stringBuilder.append("null");
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
