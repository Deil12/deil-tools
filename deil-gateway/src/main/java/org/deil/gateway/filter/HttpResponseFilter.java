package org.deil.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 设置通过网关的请求头
 */
@Component
public class HttpResponseFilter implements GlobalFilter, Ordered {
    private Logger log = LoggerFactory.getLogger(HttpResponseFilter.class);
    public static String SIGNKEY_APPID = "appid";
    public static String SIGNKEY_APPSECRET = "appsecret";
    public static String SIGNKEY_NONCE = "nonce";
    public static String SIGNKEY_TIMESTAMP = "timestamp";
    public static String SIGNKEY_SIGNATURE = "signature";

    public static String REQUEST_TIME_BEGIN = "requestTimeBegin";
    public static String RESPONSE_GATEWAY = "gateway";
    public static String RESPONSE_LOGID = "logId";
    public static String RESPONSE_SETCOOKIE = "Set-Cookie";
    public static String RESPONSE_XFRAMEOPTIONS = "x-frame-options";

    public static String REQUEST_REALCLIENTIP = "real-client-ip";
    public static String REQUEST_BYGATEWAY = "by-gateway";
    public static String REQUEST_LOGID = "log-id";
    public static String REQUEST_SETCOOKIE = "Set-Cookie";
    public static String REQUEST_XFRAMEOPTIONS = "x-frame-options";
    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String logId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        //通过网关的请求全部添加请求头信息
        exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(RESPONSE_GATEWAY, "true");
        response.getHeaders().add(RESPONSE_LOGID, logId);
        response.getHeaders().add(RESPONSE_SETCOOKIE, "Secure;HttpOnly");
        response.getHeaders().add(RESPONSE_XFRAMEOPTIONS, "SAMEORIGIN");

        ServerHttpRequest request = exchange.getRequest();
        //获取请求的ip地址
        String hostString = request.getRemoteAddress().getHostString();
        //修改增加请求头
        request.mutate().header(REQUEST_REALCLIENTIP, hostString);
        request.mutate().header(REQUEST_BYGATEWAY, "true");
        request.mutate().header(REQUEST_LOGID, logId);
        request.mutate().header(REQUEST_SETCOOKIE, "Secure;HttpOnly");
        request.mutate().header(REQUEST_XFRAMEOPTIONS, "SAMEORIGIN");
        //获取请求路径
        String rawPath = request.getURI().getRawPath();

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                    if (startTime != null) {
                        log.info("LOGID >> {}, IP >> {}, URL >> {}, 耗时 >> {}ms", logId, hostString, rawPath, System.currentTimeMillis() - startTime);
                    }
                })
        );
    }
}