package org.deil.utils.thread;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
@Configuration
@EnableAsync
@ComponentScan("**.service")
@EnableConfigurationProperties(ThreadPoolProperties.class)
//@ConditionalOnProperty(prefix = "deil.threadpool", name = "enable", havingValue = "true", matchIfMissing = false)
public class ThreadPoolConfig implements AsyncConfigurer {
    private Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    private final ThreadPoolProperties threadPoolProperties;
    public ThreadPoolConfig(
            ThreadPoolProperties threadPoolProperties
    ) {
        this.threadPoolProperties = threadPoolProperties;
    }

    @Bean("asyncTaskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getMaxPoolSize());
        executor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(threadPoolProperties.isAllowtCoreThreadTimeOut());
        executor.setWaitForTasksToCompleteOnShutdown(threadPoolProperties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationSeconds());
        executor.setRejectedExecutionHandler(threadPoolProperties.getRejectedExecutionHandler()[threadPoolProperties.getPolicys()]);
        //executor.initialize();
        log.info("\033[0;32mThreadPoolTaskExecutor 线程池初始化\033[0m");
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
            log.error("Async-CustomsMoveService 线程异常:{} >> {}", method.getDeclaringClass(), msg);
        };
    }

}
