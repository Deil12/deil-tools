package healthCheck;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 具体执行逻辑
 *
 * @author Boom
 */
@Slf4j
public class SchedulingRunnable implements Runnable {
    private final HealthCheckConfig healthCheckConfig;

    public SchedulingRunnable(HealthCheckConfig healthCheckConfig) {
        this.healthCheckConfig = healthCheckConfig;
    }

    @Override
    public void run() {
        //执行任务
        log.info("执行健康检查：{}", healthCheckConfig.getServerName());
        String scriptLocation = healthCheckConfig.getScriptLocation();
        try {
            HttpRequest.get(healthCheckConfig.getCheckUrl())
                    .execute().body();
            log.info("健康检查成功");
        } catch (Exception e) {
            log.info("健康检查失败，开始重启脚本");
            try {
                Runtime.getRuntime().exec(scriptLocation);
                log.info("执行重启脚本成功");
            } catch (Exception ex) {
                log.info("执行重启脚本失败", ex);
            }
        }
    }
}
