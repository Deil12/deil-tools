package org.deil.utils.web;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ResultType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.deil.utils.utils.FileUtil;
import org.deil.utils.utils.IPUtil;
import org.deil.utils.utils.JoinPointUtil;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Order(1)
@Slf4j
@Aspect
@Component
public class AccessAspect {

    private final static String DEFAULT_PATHERROR = "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM/dd"));

    private final AccessProperties accessProperties;
    public AccessAspect(AccessProperties accessProperties) {
        this.accessProperties = accessProperties;
    }

    @Pointcut("execution(* *.controller..*(..))")
    public void pointCut() {
    }

    @Before(value = "pointCut()")
    public void checkAccessIP(JoinPoint point) throws Throwable {
        if (accessProperties.isEnabled()) {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String ip = IPUtil.getIpAddress(request);
            if (!isRelease(ip)) {
                Object data = JoinPointUtil.getParamByName(point, "data", Object.class);
                String logId = JoinPointUtil.getParamByName(point, "logId", String.class);
                log.error("[{}] 不在白名单中，被拦截请求:\n{}", ip, JSONObject.toJSONString(data));
                FileUtil.creatWithTimeName(
                        DEFAULT_PATHERROR+ "/REJECT_ACCESS",
                        logId + ".json",
                        new JSONObject()
                                .fluentPut("reqUrl", request.getRequestURL())
                                .fluentPut("logId", logId)
                                .fluentPut("reqJson", data)
                                .toJSONString()
                );
                throw new Exception(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
            }
        }
    }

    /**
     * 黑名单拦截, 白名单放行
     * <p>true 放行； false 拦截</p>
     *
     * @param ip
     * @return boolean
     * @time 2023/05/10
     * @since 1.0.0
     */
    private boolean isRelease(String ip) {
        return !StringUtils.hasLength(ip)
                //|| accessProperties.getBlackIPs() != null && accessProperties.getBlackIPs().contains(ip)
                || org.apache.commons.lang3.StringUtils.isEmpty(accessProperties.getWhiteIPs()) ?
                false : accessProperties.getWhiteIPs().contains(ip);
    }

}
