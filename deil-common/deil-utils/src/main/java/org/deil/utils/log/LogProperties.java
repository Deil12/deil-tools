package org.deil.utils.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "deil.log")
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
