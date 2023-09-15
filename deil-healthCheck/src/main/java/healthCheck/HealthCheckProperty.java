package healthCheck;

import lombok.Data;

/**
 * @PURPOSE 检查配置
 * @DATE 2022/11/30
 * @CODE Deil
 */
@Data
public class HealthCheckProperty {

    private boolean enabled = true;

    /**
     * 服务名称
     */
    private String serverName = "摸鱼服务";

    /**
     * cron表达式（默认每分钟）
     */
    private String cron = "0 0/1 * * * ?";

    /**
     * 执行脚本的位置（提前给执行权限）
     */
    private String scriptLocation = "./restartApp.sh";

    /**
     * 健康检查的url
     */
    private String checkUrl = "http://localhost";

}
