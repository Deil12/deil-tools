package scheduler.common.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledFuture;

/**
 * @PURPOSE cron任务注册
 * @DATE 2022/11/30
 * @CODE Deil
 * @see DisposableBean
 */
@Configuration
public class BaseTaskRegistrar implements DisposableBean {

    @Resource
    private TaskScheduler taskScheduler;

    public void scheduleCronTask(CronTask cronTask) {
        BaseFutureWrapper baseFutureWrapper = new BaseFutureWrapper();
        baseFutureWrapper.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
    }

    @Override
    public void destroy() {
        BaseFutureWrapper baseFutureWrapper = new BaseFutureWrapper();
        baseFutureWrapper.cancel();
    }

}

final class BaseFutureWrapper {

    public volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }

}
