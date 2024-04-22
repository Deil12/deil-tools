package org.deil.login.common;

import org.deil.login.common.domain.LoginProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginRegister {

    /**
     * 使用一个比较短的前缀，尽量提高 cmd 命令台启动时指定参数的便利性
     */
    public static final String CONFIG_VERSION = "deil.login";

    /**
     * 注册 Login 配置
     */
    @Bean
    @ConfigurationProperties(prefix = CONFIG_VERSION)
    LoginProperties getLoginConfig() {
        return new LoginProperties();
    }

}
