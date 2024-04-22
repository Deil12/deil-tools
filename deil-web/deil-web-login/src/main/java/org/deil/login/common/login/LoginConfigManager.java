package org.deil.login.common.login;

import org.deil.login.common.domain.LoginProperties;
import org.thymeleaf.util.StringUtils;

public class LoginConfigManager {

    /**
     * 配置文件 Bean
     */
    private static volatile LoginProperties config;

    public static void setConfig(LoginProperties config) {
        LoginConfigManager.config = config;
        // 如果配置了 auto=true，则随机生成账号名密码
        if(config.getAuto()) {
            config.setName(StringUtils.randomAlphanumeric(8));
            config.setPwd(StringUtils.randomAlphanumeric(8));
            System.out.println("\n\nname: " + config.getName() + "\npassword: " + config.getPwd() + "\n\n");
        }
    }

    public static LoginProperties getConfig() {
        if (config == null) {
            synchronized (LoginConfigManager.class) {
                if (config == null) {
                    setConfig(new LoginProperties());
                }
            }
        }
        return config;
    }

}
