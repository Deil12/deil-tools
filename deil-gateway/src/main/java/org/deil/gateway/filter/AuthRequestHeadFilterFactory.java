package org.deil.gateway.filter;

import org.deil.gateway.common.AuthAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
@ConfigurationProperties(prefix = "deil.auth")
public class AuthRequestHeadFilterFactory extends AbstractGatewayFilterFactory<Object> {
    /**
     * 请求限定时间
     */
    private Long validtime;

    /**
     * 账号列表
     */
    private List<AuthAccount> account;

    /**
     * 不需要进行校验的路径，需要区分大小写
     */
    private List<String> ignoreApi;

    private final ConcurrentHashMap<String, ConcurrentHashMap<String,Boolean>> concurrentHashMap = new ConcurrentHashMap<>();


    @Override
    public GatewayFilter apply(Object config) {
        return new AuthRequestHeadFilter(validtime, account, ignoreApi, concurrentHashMap);
    }
}
