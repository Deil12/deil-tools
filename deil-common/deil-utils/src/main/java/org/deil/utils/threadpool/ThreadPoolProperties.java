package org.deil.utils.threadpool;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@ConfigurationProperties(prefix = "deil.threadpool")
public class ThreadPoolProperties {

    private boolean enabled = Boolean.FALSE;

    private int corePoolSize = Runtime.getRuntime().availableProcessors();

    private int maxPoolSize = 500;

    private int queueCapacity = 0;

    private String threadNamePrefix = "DeilUtils-";

    private int keepAliveSeconds = 60;

    private boolean allowtCoreThreadTimeOut = Boolean.FALSE;

    private boolean waitForTasksToCompleteOnShutdown = Boolean.FALSE;

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

    public ThreadPoolProperties() {
    }

    public ThreadPoolProperties(boolean enabled, int corePoolSize, int maxPoolSize, int queueCapacity, String threadNamePrefix, int keepAliveSeconds, boolean allowtCoreThreadTimeOut, boolean waitForTasksToCompleteOnShutdown, int awaitTerminationSeconds, int policys) {
        this.enabled = enabled;
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.queueCapacity = queueCapacity;
        this.threadNamePrefix = threadNamePrefix;
        this.keepAliveSeconds = keepAliveSeconds;
        this.allowtCoreThreadTimeOut = allowtCoreThreadTimeOut;
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
        this.awaitTerminationSeconds = awaitTerminationSeconds;
        this.policys = policys;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public boolean isAllowtCoreThreadTimeOut() {
        return allowtCoreThreadTimeOut;
    }

    public void setAllowtCoreThreadTimeOut(boolean allowtCoreThreadTimeOut) {
        this.allowtCoreThreadTimeOut = allowtCoreThreadTimeOut;
    }

    public boolean isWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    public int getAwaitTerminationSeconds() {
        return awaitTerminationSeconds;
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }

    public int getPolicys() {
        return policys;
    }

    public void setPolicys(int policys) {
        this.policys = policys;
    }

    public RejectedExecutionHandler[] getRejectedExecutionHandler() {
        return rejectedExecutionHandler;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler[] rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }
}
