package org.deil.gatewaynode;

import com.alibaba.fastjson2.JSONObject;
import org.deil.utils.utilold.IPUtil;
import org.deil.utils.domain.vo.VOKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerProtectInterceptor implements HandlerInterceptor {
    private Logger log = LoggerFactory.getLogger(ServerProtectInterceptor.class);

    private GatewayProperties properties;

    public void setProperties(GatewayProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        LogIdHolder.setLogId(UUID.randomUUID().toString().replace("-", ""));
        //F5健康检测请求
        if (!IPUtil.getIpAddress(request).matches("^(10.80.119.25[1-2])|(10.69.97.73)$")) {
            log.info("\033[0;32;4m拦截器>接收[{}]请求...\033[0m", IPUtil.getIpAddress(request));
        }
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/swagger-ui.html")
                || requestURI.startsWith("/swagger-ui/index.html")
                || requestURI.startsWith("/doc.html")
                || requestURI.startsWith("/swagger-resources")
                || requestURI.startsWith("/webjars")
                || requestURI.startsWith("/v2")
                || requestURI.startsWith("/druid")
                || requestURI.startsWith("/HealthCheck/Check")
                || requestURI.startsWith("/HealthCheck/F5Check")
        ) {
            //忽略swagger地址
            return true;
        }
        if (!properties.isEnabled()) {
            return true;
        }
        String authority = request.getHeader(VOKey.REQUEST_BYGATEWAY);
        if (null != authority && authority.equals("true")) {
            return true;
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Content-type", "application/json;charset=UTF-8");

            Map<String, Object> map = new HashMap<>();
            String logId = /*request.getAttribute(VOKey.RESPONSE_LOGID).toString()*/LogIdHolder.getLogId();
            map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
            map.put("logId", logId);
            map.put("message", "请通过网关访问");
            response.getWriter().write(JSONObject.toJSONString(map));
            log.info("\033[31m拦截器>网关已开启，当前请求[{}]未经网关\033[0m", logId);
            return false;
        }
    }
}