package org.deil.utils.log;

/**
 * 请求线程 logId 持有者
 */
public class LogIdHolder {

    private static final ThreadLocal<String> LOG_CONTEXT = new ThreadLocal<>();

    public static void setLogId(String logId) {
        LOG_CONTEXT.set(logId);
    }


    public static void removeLogId() {
        LOG_CONTEXT.remove();
    }


    public static String getLogId() {
        return LOG_CONTEXT.get();
    }

}