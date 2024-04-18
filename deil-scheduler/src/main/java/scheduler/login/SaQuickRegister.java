package scheduler.login;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scheduler.common.domain.LoginProperties;

@Configuration
public class SaQuickRegister {

    /**
     * 使用一个比较短的前缀，尽量提高 cmd 命令台启动时指定参数的便利性
     */
    public static final String CONFIG_VERSION = "tang.ui";

    /**
     * 注册 Quick-Login 配置
     *
     * @return see note
     */
    @Bean
    @ConfigurationProperties(prefix = CONFIG_VERSION)
    LoginProperties getSaQuickConfig() {
        return new LoginProperties();
    }

}
