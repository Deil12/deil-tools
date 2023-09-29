package healthCheck;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @PURPOSE 检查配置
 * @DATE 2022/11/30
 * @CODE Deil
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(factory = YamlPropertySourceFactory.class, value = { "classpath:application.yml" })
public class BaseProperties {

    private boolean enabled = Boolean.TRUE;

    private boolean toRestart = Boolean.FALSE;

    /**
     * 服务名称
     */
    private String serverName = "kiss my ass";

    /**
     * cron表达式
     */
    private String cron = "* 0/1 * * * ?";

    /**
     * 执行脚本的位置（提前给执行权限）
     */
    private String scriptLocation = "./blingbling.sh";

    /**
     * 健康检查的url
     */
    private String checkUrl = "http://localhost:8080";

}
