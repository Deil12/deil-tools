//package org.deil.demo.common;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.deil.utils.domain.properties.ThreadPoolProperties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//
///**
// * threadpool配置类
// *
// * @DATE 2023/04/17
// * @CODE Deil
// * @SINCE 1.0.0
// * @see AsyncConfigurer
// */
//@Configuration
//@EnableAsync
//public class ThreadPoolConfigLocal {
//    private Logger log = LoggerFactory.getLogger(ThreadPoolConfigLocal.class);
//
//    //@Bean("test2")
//    public Executor asyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        //核心线程数5：线程池创建时候初始化的线程数
//        executor.setCorePoolSize(5);
//        //最大线程数5：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
//        executor.setMaxPoolSize(5);
//        //缓冲队列500：用来缓冲执行任务的队列
//        executor.setQueueCapacity(500);
//        //允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
//        executor.setKeepAliveSeconds(60);
//        //线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
//        executor.setThreadNamePrefix("asyncJCccc");
//        executor.initialize();
//        log.info("\033[0;32m{}线程池初始化\033[0m", "local");
//        return executor;
//    }
//}
