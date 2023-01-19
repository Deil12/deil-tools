package org.deil.utils.log.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.deil.utils.single.IPUtil;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

/**
 * @PURPOSE 日志方面
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Slf4j
@Order(1)
@Aspect
@Component
public class LogAspect {

    @Resource
    private ObjectMapper mapper;

    @Around(value = "@annotation(org.deil.utils.log.annotation.Log)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long requestTimestamp = 0;
        String logId = "";
        ServletRequestAttributes requestAtt = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        Object o;
        if (requestAtt != null) {
            request = requestAtt.getRequest();
        }

        if (request != null) {
            logId = String.valueOf(request.getAttribute("logId"));
            requestTimestamp = (long) request.getAttribute("requestTimestamp");

            ArrayNode parameterNode = mapper.createArrayNode();
            request.getParameterMap().forEach((s, strings) -> parameterNode.addObject().put(s, Arrays.toString(strings)));

            ArrayNode headers = mapper.createArrayNode();
            headers.addObject()
                    .put("ContentType", request.getContentType())
                    .put("Accept-Language", request.getHeader("Accept-Language"));

            log.info(String.valueOf(mapper.createObjectNode()
                            .put("Event", "[START]")
                            .put("LogId", logId)
                            .put("requestTimestamp", requestTimestamp)
                            .put("RequestPath", request.getRequestURI())
                            .put("QueryString", request.getQueryString())
                            .putPOJO("ParameterMap", parameterNode)
                            .putPOJO("headers", headers)
                            .put("Args", Arrays.toString(joinPoint.getArgs()))
                            .put("ClientAddr", IPUtil.getIpAddress(request) + ":" + request.getRemotePort())
                            .put("ServerAddr", request.getLocalAddr() + ":" + request.getServerPort())
                            .put("PackagePath", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName())
                            .toString()
                    )
            );
        } else {
            logId = UUID.randomUUID().toString();
            requestTimestamp = System.currentTimeMillis();
            log.info(String.valueOf(mapper.createObjectNode()
                    .put("Event", "[START]")
                    .put("LogId", logId)
                    .put("Args", Arrays.toString(joinPoint.getArgs()))
                    .put("PackagePath", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName())
                    .toString())
            );
        }
        o = joinPoint.proceed();
        log.info(mapper.createObjectNode()
                .put("Event", "[END]")
                .put("LogId", logId)
                .put("UseTime", (System.currentTimeMillis() - requestTimestamp) + " ms")
                .put("PackagePath", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName())
                .toString());

        return o;
    }

    @AfterReturning(value = "@annotation(org.deil.utils.log.annotation.Log)", returning = "retVal")
    public void returnValue(JoinPoint joinPoint, Object retVal) {
        try {
            String val;
            if (retVal != null) {
                if (retVal instanceof ResponseEntity) {
                    val = mapper.writeValueAsString(retVal);
                } else {
                    val = retVal.toString();
                }
                log.info("[返回值] {}.{}:{}",
                        joinPoint.getSignature().getDeclaringType().getSimpleName(),
                        joinPoint.getSignature().getName(),
                        val.length() > 500 ? val.substring(0, 500) + " [truncated] ... " + val.substring(val.length() - 1) : val);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

    }
}
