package healthCheck;

import java.util.concurrent.ScheduledFuture;

/**
 * @PURPOSE ScheduledFuture包装类
 * @DATE 2022/11/30
 * @CODE Deil
 */
public final class ScheduledFutureWrapper {

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
