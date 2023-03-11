package org.deil.qurtz.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.deil.qurtz.commom.EnumTaskEnable;
import org.deil.qurtz.commom.domain.entity.TaskInfo;
import org.deil.qurtz.commom.domain.vo.TaskInfoReq;
import org.deil.qurtz.service.TaskInfoService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @PURPOSE 石英经理
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Slf4j
@Component
public class QuartzManager {

    @Resource
    private /*final*/ Scheduler scheduler;
    private final SpringJobFactory springJobFactory;
    private final TaskInfoService taskInfoService;
    public QuartzManager(
        SpringJobFactory springJobFactory,
        TaskInfoService taskInfoService
    ) {
        this.springJobFactory = springJobFactory;
        this.taskInfoService = taskInfoService;
    }

    @PostConstruct
    public void start() {
        //启动所有任务
        try {
            scheduler.setJobFactory(springJobFactory);
            // scheduler.clear();
            List<TaskInfo> tasks = taskInfoService.selectTasks();
            for (TaskInfo taskInfo : tasks) {
                if (EnumTaskEnable.START.getCode().equals(taskInfo.getStatus()) && !StringUtils.isEmpty(taskInfo.getCron())) {
                    TaskInfoReq data=new TaskInfoReq();
                    BeanUtils.copyProperties(taskInfo,data);
                    taskInfoService.addJob(data);
                }
            }
            log.info("定时任务启动完成");
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("定时任务初始化失败");
        }
    }

}
