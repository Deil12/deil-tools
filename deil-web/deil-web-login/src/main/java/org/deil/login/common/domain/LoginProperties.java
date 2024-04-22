package org.deil.login.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginProperties {

    public static final String ACCOUNT = "name";
    public static final String PASSWORD = "pwd";
    public static final String MESSAGE = "msg";
    public static final String CONFIG = "cfg";

    /** 是否开启全局登录校验，如果为 false，则不再拦截请求出现登录页 */
    private Boolean enabled = true;

    /** 用户名 */
    private String name = "";

    /** 密码 */
    private String pwd = "";

    /** 是否自动生成一个账号和密码，此配置项为 true 后，name、pwd 字段将失效 */
    private Boolean auto = false;

    /** 登录页面的标题 */
    private String title = "Hey There";

    /** 是否显示底部版权信息 */
    private Boolean copr = true;

    /** 配置拦截的路径，逗号分隔 */
    private String include = "/**";

    /** 配置拦截的路径，逗号分隔 */
    private String exclude = "/favicon.ico, /static/**, /login, /doLogin";

    private String ip = "";

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCopr() {
        return copr;
    }

    public void setCopr(Boolean copr) {
        this.copr = copr;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

}
