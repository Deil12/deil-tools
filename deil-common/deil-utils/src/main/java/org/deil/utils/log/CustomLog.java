package org.deil.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLog {

    private final Logger logger;

    private CustomLog(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    private CustomLog(String topicName) {
        this.logger = LoggerFactory.getLogger(topicName);
    }

    public static CustomLog getLogger(Class<?> clazz) {
        return new CustomLog(clazz);
    }

    public static CustomLog getLogger(String topicName) {
        return new CustomLog(topicName);
    }

    public void info(String format, Object... args) {
        logger.info("logId: [" + LogIdHolder.getLogId() + "] - " + format, args);
    }

    public void warn(String format, Object... args) {
        logger.warn("logId: [" + LogIdHolder.getLogId() + "] - " + format, args);
    }

    public void error(String format, Object... args) {
        logger.error("logId: [" + LogIdHolder.getLogId() + "] - " + format, args);
    }

    public void debug(String format, Object... args) {
        logger.debug("logId: [" + LogIdHolder.getLogId() + "] - " + format, args);
    }

}
