package healthCheck;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @PURPOSE cron任务注册
 * @DATE 2022/11/30
 * @CODE Deil
 * @see DisposableBean
 */
@Component
public class CronTaskRegistrar implements DisposableBean {

    @Resource
    private TaskScheduler taskScheduler;

    public void scheduleCronTask(CronTask cronTask) {
        ScheduledFutureWrapper scheduledTask = new ScheduledFutureWrapper();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
    }

    @Override
    public void destroy() throws Exception {
    }

}
