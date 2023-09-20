package org.deil.utils.threadpool;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.lang.Nullable;

import java.util.concurrent.Executor;

public interface LogTraceConfigurer {

    @Nullable
    default Executor getLogTraceExecutor(){
        return null;
    }

    @Nullable
    default AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    };

}
