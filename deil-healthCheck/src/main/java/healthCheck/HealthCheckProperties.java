package healthCheck;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @PURPOSE 健康检查属性
 * @DATE 2022/11/30
 * @CODE Deil
 * @see CommandLineRunner
 */
@Data
@Component
@ConfigurationProperties(prefix = "check")
public class HealthCheckProperties implements CommandLineRunner {

    /**
     * 配置列表
     */
    private List<PublicProperty> configList = Collections.singletonList(new PublicProperty());

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Short>> concurrentHashMap = new ConcurrentHashMap<>();

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Override
    public void run(String... args) {
        for (PublicProperty publicProperty : configList) {
            SchedulingRunnable runnable = new SchedulingRunnable(publicProperty, concurrentHashMap);
            CronTask cronTask = new CronTask(runnable, publicProperty.getCron());
            cronTaskRegistrar.scheduleCronTask(cronTask);
        }
    }

}
