package org.deil.utils.log.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.deil.utils.single.IPUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

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
