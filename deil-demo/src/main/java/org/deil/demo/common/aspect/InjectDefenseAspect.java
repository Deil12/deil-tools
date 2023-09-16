package org.deil.demo.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Slf4j
@Aspect
@Component
@ConfigurationProperties(prefix = "deil.security")
public class InjectDefenseAspect {

    private boolean enabled = true;

    /*@Pointcut("@args(**.EnableInjectDefense)")
    public void pointCut() { }

    @Around(value = "pointCut()")*/
    @Around(value = "@annotation(org.deil.demo.common.aspect.annotation.EnableInjectDefense)")
    public Object judgeEnable(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enabled) {
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

}
