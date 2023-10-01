package org.deil.utils.log;

import lombok.CustomLog;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

@CustomLog
@SuppressWarnings("NullableProblems")
public class LogContextDecorator implements TaskDecorator {
    /**
     * {@inheritDoc}
     */
    @Override
    public Runnable decorate(@NonNull Runnable runnable) {
        String parentLogId = LogIdHolder.getLogId();
        //log.info("父线程LogId：{}", parentLogId);
        return () -> {
            try {
                LogIdHolder.setLogId(parentLogId);
                //log.info("子线程已同步父线程LogId");
                runnable.run();
            } finally {
                //log.info("子线程已成功清除LogId");
                LogIdHolder.removeLogId();
            }
        };
    }
}
