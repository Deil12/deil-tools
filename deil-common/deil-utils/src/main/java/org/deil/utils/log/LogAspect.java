package org.deil.utils.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    private final ObjectMapper objectMapper;

    public LogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around(value = "@annotation(org.deil.utils.log.annotation.Log)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long requestTime = 0;
        String logId = "";
        ServletRequestAttributes requestAtt = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        Object o;
        if (requestAtt != null) {
            request = requestAtt.getRequest();
        }

        if (request != null) {
            logId = String.valueOf(request.getAttribute("logId"));
            requestTime = (long) request.getAttribute("requestTime");

            ArrayNode parameterNode = objectMapper.createArrayNode();
            request.getParameterMap().forEach((s, strings) -> parameterNode.addObject().put(s, Arrays.toString(strings)));

            ArrayNode headers = objectMapper.createArrayNode();
            headers.addObject()
                    .put("ContentType", request.getContentType())
                    .put("Accept-Language", request.getHeader("Accept-Language"));

            log.info("\033[0;32;4m" + String.valueOf(objectMapper.createObjectNode()
                            .put("Event", "[START]")
                            .put("LogId", logId)
                            .put("RequestTime", requestTime)
                            .put("RequestPath", request.getRequestURI())
                            .put("QueryString", request.getQueryString())
                            .putPOJO("ParameterMap", parameterNode)
                            .putPOJO("headers", headers)
                            .put("Args", Arrays.toString(joinPoint.getArgs()))
                            .put("ClientAddr", getIpAddress(request) + ":" + request.getRemotePort())
                            .put("ServerAddr", request.getLocalAddr() + ":" + request.getServerPort())
                            .put("PackagePath", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName())
                            .toString() + "\033[0m")
            );
        } else {
            logId = UUID.randomUUID().toString().replace("-", "");
            requestTime = System.currentTimeMillis();
            log.info("\033[0;32;4m" + String.valueOf(objectMapper.createObjectNode()
                    .put("Event", "[START]")
                    .put("LogId", logId)
                    .put("Args", Arrays.toString(joinPoint.getArgs()))
                    .put("PackagePath", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName())
                    .toString() + "\033[0m")
            );
        }
        o = joinPoint.proceed();
        log.info(objectMapper.createObjectNode()
                .put("Event", "[END]")
                .put("LogId", logId)
                .put("UseTime", (System.currentTimeMillis() - requestTime) + " ms")
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
                    val = objectMapper.writeValueAsString(retVal);
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

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
