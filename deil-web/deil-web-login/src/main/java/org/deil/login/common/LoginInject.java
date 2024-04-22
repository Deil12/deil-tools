package org.deil.login.common;

import org.deil.login.common.domain.LoginProperties;
import org.deil.login.common.login.LoginConfigManager;
import org.deil.login.controller.LoginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ LoginController.class, LoginRegister.class})
public class LoginInject {

    /**
     * 注入 login 配置
     *
     * @param loginProperties 配置对象
     */
    @Autowired(required = false)
    public void setLoginConfig(LoginProperties loginProperties) {
        LoginConfigManager.setConfig(loginProperties);
    }

}
