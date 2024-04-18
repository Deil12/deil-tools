package scheduler.login;

import org.thymeleaf.util.StringUtils;
import scheduler.common.domain.LoginProperties;

public class SaQuickManager {

    /**
     * 配置文件 Bean
     */
    private static volatile LoginProperties config;
    public static void setConfig(LoginProperties config) {
        SaQuickManager.config = config;
        // 如果配置了 auto=true，则随机生成账号名密码
        if(config.getAuto()) {
            config.setName(StringUtils.randomAlphanumeric(8));
            config.setPwd(StringUtils.randomAlphanumeric(8));
        }
    }
    public static LoginProperties getConfig() {
        if (config == null) {
            synchronized (SaQuickManager.class) {
                if (config == null) {
                    setConfig(new LoginProperties());
                }
            }
        }
        return config;
    }

}
