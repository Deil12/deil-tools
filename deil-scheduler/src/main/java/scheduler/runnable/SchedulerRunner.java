package scheduler.runnable;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.CronTask;
import scheduler.common.config.BaseTaskRegistrar;

import javax.annotation.Resource;

@Data
@Configuration
@ConfigurationProperties(prefix = "task")
public class SchedulerRunner
        implements CommandLineRunner {

    private TESTProperty TEST = new TESTProperty();
    private HealthCheckProperty HEALTHCHECK = new HealthCheckProperty();

    @Resource
    private BaseTaskRegistrar baseCronRegistrar;

    @Override
    public void run(String... args) {
        baseCronRegistrar.scheduleCronTask(new CronTask(new TESTRunnable(TEST), TEST.getCron()));
        baseCronRegistrar.scheduleCronTask(new CronTask(new HealthCheckRunnable(HEALTHCHECK), HEALTHCHECK.getCron()));
    }

}
/*        implements SchedulingConfigurer {

    private TESTProperty TEST = new TESTProperty();
    private HealthCheckProperty HEALTHCHECK = new HealthCheckProperty();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(poolScheduler());

        taskRegistrar.addFixedRateTask(new IntervalTask(new HealthCheckRunnable(HEALTHCHECK), 1000, 0));
    }

    @Bean
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("poolScheduler");
        scheduler.setPoolSize(10);
        return scheduler;
    }
}*/