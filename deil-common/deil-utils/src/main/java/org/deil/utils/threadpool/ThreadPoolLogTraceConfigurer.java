package org.deil.utils.threadpool;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.deil.utils.pojo.properties.ThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * threadpool配置类
 *
 * @DATE 2023/04/17
 * @CODE Deil
 * @SINCE 1.0.0
 * @see AsyncConfigurer
 */
//@Configuration
@EnableAsync
//@ComponentScan
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolLogTraceConfigurer implements /*AsyncConfigurer*/LogTraceConfigurer {
    private Logger log = LoggerFactory.getLogger(ThreadPoolLogTraceConfigurer.class);

    private final ThreadPoolProperties threadPoolProperties;

    public ThreadPoolLogTraceConfigurer(
            ThreadPoolProperties threadPoolProperties
    ) {
        this.threadPoolProperties = threadPoolProperties;
    }

    @Bean
    @Override
    /*public Executor getAsyncExecutor() {*/
    public Executor getLogTraceExecutor() {
        ThreadPoolTaskLogTraceExecutor executor = new ThreadPoolTaskLogTraceExecutor();
        //核心线程数Runtime.getRuntime().availableProcessors()：线程池创建时候初始化的线程数
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        //最大线程数500：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        //缓冲队列500：用来缓冲执行任务的队列
        executor.setQueueCapacity(threadPoolProperties.getMaxPoolSize());
        //线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());
        //允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());

        executor.setAllowCoreThreadTimeOut(threadPoolProperties.isAllowtCoreThreadTimeOut());
        executor.setWaitForTasksToCompleteOnShutdown(threadPoolProperties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationSeconds());
        executor.setRejectedExecutionHandler(threadPoolProperties.getRejectedExecutionHandler()[threadPoolProperties.getPolicys()]);
        if (threadPoolProperties.isEnabled()) {
            executor.initialize();
            log.info("[\033[0;32m{}线程池初始化\033[0m]", threadPoolProperties.getThreadNamePrefix());
        }
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            String msg = StringUtils.EMPTY;
            if (ArrayUtils.isNotEmpty(objects) && objects.length > 0) {
                msg = StringUtils.join(msg, "[details]:");
                for (int i = 0; i < objects.length; i++) {
                    msg = StringUtils.join(msg, objects[i]);
                }
            }
            if (Objects.nonNull(throwable)) {
                msg = StringUtils.join(msg, throwable.getMessage());
            }
            log.error("{}线程异常:{} >> {}", threadPoolProperties.getThreadNamePrefix(), method.getDeclaringClass(), msg);
        };
    }
}
