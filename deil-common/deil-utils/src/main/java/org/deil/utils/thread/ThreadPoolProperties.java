package org.deil.utils.thread;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @DATE 2023/04/08
 * @CODE Deil
 */
@Data
@Component
@ConfigurationProperties(prefix = "deil.threadpool")
public class ThreadPoolProperties {

    private boolean enable = true;

    private int corePoolSize = Runtime.getRuntime().availableProcessors();

    private int maxPoolSize = 500;

    private int queueCapacity = 0;

    private String threadNamePrefix = "Deil-";

    private int keepAliveSeconds = 60;

    private boolean allowtCoreThreadTimeOut = false;

    private boolean waitForTasksToCompleteOnShutdown = false;

    private int awaitTerminationSeconds = 10;

    /**
     *  <p>0 {@link ThreadPoolExecutor.AbortPolicy}</p>
     *  <p>1 {@link ThreadPoolExecutor.AbortPolicy}</p>
     *  <p>2 {@link ThreadPoolExecutor.AbortPolicy}</p>
     *  <p>3 {@link ThreadPoolExecutor.AbortPolicy}</p>
     */
    private int policys = 3;

    //private Class<? extends RejectedExecutionHandler> rejectedExecutionHandler;
    private RejectedExecutionHandler[] rejectedExecutionHandler = {
            new ThreadPoolExecutor.AbortPolicy(),
            new ThreadPoolExecutor.DiscardPolicy(),
            new ThreadPoolExecutor.DiscardOldestPolicy(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    };

}
