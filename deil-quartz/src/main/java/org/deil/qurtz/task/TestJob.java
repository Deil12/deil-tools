package org.deil.qurtz.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @PURPOSE 董ao工作
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Slf4j
@Component
public class TestJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("测试job");
    }
}
