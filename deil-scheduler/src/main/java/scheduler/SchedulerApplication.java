package scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;

/**
 * @PURPOSE 健康检查应用程序
 * @DATE 2022/11/30
 * @CODE Deil
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
//@EnableRetry
public class SchedulerApplication implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }

    //region 监听
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("\033[42;30m---------------------- 定 时 服 务 群 组 初 始 化 ----------------------\033[0m");
    }

    @PreDestroy
    public void destroy() {
        log.info("\033[42;31m---------------------- 定 时 服 务 群 组 注 销 ----------------------\033[0m");
    }
    //endregion

}
