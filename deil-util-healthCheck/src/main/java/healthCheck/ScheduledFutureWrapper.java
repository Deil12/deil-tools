package healthCheck;

import java.util.concurrent.ScheduledFuture;

/**
 * ScheduledFuture包装类
 *
 * @author Boom
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
