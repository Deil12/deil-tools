package org.deil.utils.pojo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "deil.utils.log")
public class LogProperties {

    private boolean enabled = Boolean.TRUE;


    public LogProperties() {
    }

    public LogProperties(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
