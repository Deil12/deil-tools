package scheduler.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import scheduler.common.domain.LoginProperties;
import scheduler.controller.SchedulerController;

@Configuration
@Import({ SchedulerController.class, SaQuickRegister.class})
public class SaQuickInject {

    /**
     * 注入 quick-login 配置
     *
     * @param loginProperties 配置对象
     */
    @Autowired(required = false)
    public void setSaQuickConfig(LoginProperties loginProperties) {
        SaQuickManager.setConfig(loginProperties);
    }

}
