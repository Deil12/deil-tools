package org.deil.utils.retry;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RetryAspect {

    @Pointcut("@annotation(org.deil.utils.retry.Retryable)")
    private void retryMethodCall(){}

    @Around("retryMethodCall()")
    public Object retry(ProceedingJoinPoint joinPoint) throws InterruptedException {
        // 获取重试次数和重试间隔
        Retryable retry = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(Retryable.class);
        int maxRetryTimes = retry.retryTimes();
        int retryInterval = retry.retryInterval();

        Throwable error = new RuntimeException();
        for (int retryTimes = 1; retryTimes <= maxRetryTimes; retryTimes++){
            try {
                Object result = joinPoint.proceed();
                return result;
            } catch (Throwable throwable) {
                error = throwable;
                log.warn("调用异常，开始重试，retryTimes:{}", retryTimes);
            }
            Thread.sleep(retryInterval * 1000L);
        }
        throw new RuntimeException("重试次数耗尽", error);
    }

}
