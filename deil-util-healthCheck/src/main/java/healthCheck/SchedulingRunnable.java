package healthCheck;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @PURPOSE 具体执行逻辑
 * @DATE 2022/11/30
 * @CODE Deil
 * @see Runnable
 */
@Slf4j
public class SchedulingRunnable implements Runnable {

    private final HealthCheckProperty healthCheckProperty;

    public SchedulingRunnable(HealthCheckProperty healthCheckProperty) {
        this.healthCheckProperty = healthCheckProperty;
    }

    @Override
    public void run() {
        //执行任务
        log.info("执行健康检查：{}", healthCheckProperty.getServerName());
        String scriptLocation = healthCheckProperty.getScriptLocation();
        try {
            HttpRequest.get(healthCheckProperty.getCheckUrl())
                    .execute()
                    .body();
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
