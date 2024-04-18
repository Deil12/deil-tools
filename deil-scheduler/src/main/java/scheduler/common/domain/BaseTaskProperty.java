package scheduler.common.domain;

import java.nio.charset.StandardCharsets;

public class BaseTaskProperty {

    private boolean enabled = true;

    private String serverName = "test";

    private String cron = "0/30 * * * * ?";

    private long interval = 1000;

    private long initialDelay = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getServerName() {
        return new String(serverName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }
}
