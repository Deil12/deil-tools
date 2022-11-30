package healthCheck;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

/**
 * @author Boom
 */
@Component
public class CronTaskRegistrar implements DisposableBean {

    @Autowired
    private TaskScheduler taskScheduler;


    public void scheduleCronTask(CronTask cronTask) {
        ScheduledFutureWrapper scheduledTask = new ScheduledFutureWrapper();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
    }


    @Override
    public void destroy() throws Exception {
    }
}
