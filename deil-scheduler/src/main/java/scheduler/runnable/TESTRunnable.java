package scheduler.runnable;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import scheduler.common.domain.BaseTaskProperty;

import java.time.LocalDateTime;

@Slf4j
public class TESTRunnable implements Runnable {

    private final TESTProperty testProperty;

    public TESTRunnable(TESTProperty testProperty) {
        this.testProperty = testProperty;
    }

    @Override
    public void run() {
        if (testProperty.isEnabled()) {
            log.info("\033[42;30m{},{}\033[0m", testProperty.getServerName(), LocalDateTime.now());
        }
    }
}

@Data
class TESTProperty extends BaseTaskProperty {

    /**
     * 执行脚本的位置（提前给执行权限）
     */
    private String scriptLocation = "./restartApp.sh";

    /**
     * 健康检查的url
     */
    private String checkUrl = "http://localhost";

}
