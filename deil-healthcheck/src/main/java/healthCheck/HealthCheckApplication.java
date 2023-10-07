package healthCheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @PURPOSE 健康检查应用程序
 * @DATE 2022/11/30
 * @CODE Deil
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
//@EnableRetry
public class HealthCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthCheckApplication.class, args);
    }

}
