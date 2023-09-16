package org.deil.utils.log.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @PURPOSE web日志方面
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Slf4j
@Aspect
public class WebLogAspect extends AbstractLogAspect {

    @Pointcut(
            "@target(org.springframework.web.bind.annotation.RestController) && (" +
                    "@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
                    "&& !@annotation(org.deil.utils.log.annotation.IgnoreLog)" +
                    ")"
    )
    protected void webLog() {

    }

    @Around("webLog()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return doAround(proceedingJoinPoint);
    }

}
