package healthCheck;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 检查配置
 */
@Data
public class HealthCheckConfig {

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 执行脚本的位置
     */
    private String scriptLocation;

    /**
     * 健康检查的url
     */
    private String checkUrl;

}
