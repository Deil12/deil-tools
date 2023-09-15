package org.deil.monitor.log.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @PURPOSE 方法记录方面
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Slf4j
@Aspect
@Component
public class MethodLogAspect extends AbstractLogAspect{

    @Pointcut("@annotation(org.deil.utils.log.annotation.Log)")
    protected void methodLog(){

    }

    @Around("methodLog()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return doAround(proceedingJoinPoint);
    }

}
