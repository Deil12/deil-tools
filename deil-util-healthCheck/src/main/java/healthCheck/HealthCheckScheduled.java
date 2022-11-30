package healthCheck;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "check")
public class HealthCheckScheduled implements CommandLineRunner {

    /**
     * 需要检查的列表
     */
    private List<HealthCheckConfig> configList;


    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;



    @Override
    public void run(String... args) throws Exception {
        for (HealthCheckConfig healthCheckConfig : configList) {
            SchedulingRunnable runnable = new SchedulingRunnable(healthCheckConfig);
            CronTask cronTask = new CronTask(runnable,healthCheckConfig.getCron());
            cronTaskRegistrar.scheduleCronTask(cronTask);
        }
    }
}
