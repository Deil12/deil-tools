package org.deil.qurtz.config;

import lombok.extern.slf4j.Slf4j;
import org.deil.qurtz.commom.domain.entity.TaskInfo;
import org.deil.qurtz.commom.domain.vo.TaskInfoReq;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @PURPOSE 任务管理器
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Slf4j
@Component
public class TaskManager {

    @Resource
    private /*final*/ Scheduler scheduler;
    @Resource
    private /*final*/ SpringContextUtil springContextUtil;
    //public TaskManager(
    //        Scheduler scheduler,
    //        SpringContextUtils springContextUtils
    //) {
    //    this.scheduler = scheduler;
    //    this.springContextUtils = springContextUtils;
    //}

    public static final String JOB_DEFAULT_GROUP_NAME = "JOB_DEFAULT_GROUP_NAME";
    public static final String TRIGGER_DEFAULT_GROUP_NAME = "TRIGGER_DEFAULT_GROUP_NAME";

    /**
     * 添加任务
     */
    public boolean addJob(TaskInfoReq taskInfoReq) {
        boolean flag = true;
        if (!CronExpression.isValidExpression(taskInfoReq.getCron())) {
            log.error("定时任务表达式有误：{}", taskInfoReq.getCron());
            return false;
        }
        try {
            String className = springContextUtil.getBean(taskInfoReq.getJobName()).getClass().getName();
            JobDetail jobDetail = JobBuilder.newJob().withIdentity(new JobKey(taskInfoReq.getJobName(), JOB_DEFAULT_GROUP_NAME))
                    .ofType((Class<Job>) Class.forName(className))
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(taskInfoReq.getCron()))
                    .withIdentity(new TriggerKey(taskInfoReq.getJobName(), TRIGGER_DEFAULT_GROUP_NAME))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (Exception e) {
            log.error("添加定时任务异常：{}", e.getMessage(), e);
            flag = false;
        }
        return flag;
    }

    /**
     * @param taskInfo 任务信息
     * @return boolean
     * @TIME 2022/12/13 :
     * 更新任务
     */
    public boolean updateJob(TaskInfo taskInfo) {
        boolean flag = true;
        try {
            JobKey jobKey = new JobKey(taskInfo.getJobName(), JOB_DEFAULT_GROUP_NAME);
            TriggerKey triggerKey = new TriggerKey(taskInfo.getJobName(), TRIGGER_DEFAULT_GROUP_NAME);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
                Trigger newTrigger = TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .withSchedule(CronScheduleBuilder.cronSchedule(taskInfo.getCron()))
                        .withIdentity(triggerKey)
                        .build();
                scheduler.rescheduleJob(triggerKey, newTrigger);
            } else {
                log.info("更新任务失败，任务不存在，任务名称：{}，表达式：{}", taskInfo.getJobName(), taskInfo.getCron());
            }
            log.info("更新任务成功，任务名称：{}，表达式：{}", taskInfo.getJobName(), taskInfo.getCron());
        } catch (SchedulerException e) {
            log.error("更新定时任务失败:{}", e.getMessage(), e);
            flag = false;
        }
        return flag;
    }

    /**
     * 暂停任务
     */
    public boolean pauseJob(TaskInfo taskInfo) {
        try {
            scheduler.pauseJob(JobKey.jobKey(taskInfo.getJobName(), JOB_DEFAULT_GROUP_NAME));
            log.info("任务暂停成功：{}", taskInfo.getId());
            return true;
        } catch (SchedulerException e) {
            log.error("暂停定时任务失败:{}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 恢复任务
     */
    public boolean resumeJob(TaskInfo taskInfo) {
        try {
            scheduler.resumeJob(JobKey.jobKey(taskInfo.getJobName(), JOB_DEFAULT_GROUP_NAME));
            log.info("任务恢复成功：{}", taskInfo.getId());
            return true;
        } catch (SchedulerException e) {
            log.error("恢复定时任务失败:{}", e.getMessage(), e);
            return false;
        }
    }

}
