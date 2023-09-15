package healthCheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.PreDestroy;

/**
 * @PURPOSE 应用程序配置类
 * @DATE 2022/11/30
 * @CODE Deil
 * @see ApplicationListener
 */
@Slf4j
@Configuration
public class AppConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("\033[42;30m---------------------- F5 检 测 初 始 化 ----------------------\033[0m");
    }

    @PreDestroy
    public void destroy() {
        log.info("\033[42;31m---------------------- F5 检 测 注 销 ----------------------\033[0m");
    }

}
