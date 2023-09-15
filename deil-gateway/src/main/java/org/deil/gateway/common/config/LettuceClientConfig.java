package org.deil.gateway.common.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

/**
 * 用来替换协议版本
 * 解决lettuce6.0以上无法验证redis密码问题
 */
@Configuration
public class LettuceClientConfig implements LettuceClientConfigurationBuilderCustomizer {
    //替换新版本 lettuce6后协议版本默认ProtocolVersion.RESP3，在验证密码时会添加  default，使用ProtocolVersion.RESP2去除
    @Override
    public void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigurationBuilder) {
        clientConfigurationBuilder.clientOptions(ClientOptions.builder()
                .protocolVersion(ProtocolVersion.RESP2)
                .build());
    }
}
