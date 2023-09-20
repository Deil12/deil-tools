package org.deil.demo.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 *
 * @DATE 2023/09/19
 * @CODE Deil
 */
public class TestJob extends QuartzJobBean {
    private Logger log = LoggerFactory.getLogger(TestJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("quartz1执行");
    }
}
