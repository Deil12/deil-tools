package org.deil.qurtz.service;

import org.deil.qurtz.commom.Result;
import org.deil.qurtz.commom.domain.entity.TaskInfo;
import org.deil.qurtz.commom.domain.vo.TaskInfoReq;

import java.util.List;

/**
 * @PURPOSE 任务信息 service 层
 * @DATE 2022/12/13
 * @CODE Deil
 */
public interface TaskInfoService {

    /**获取任务列表分页*/
    Result selectTaskListByPage(TaskInfoReq taskInfoReq);

    /**获取所有任务*/
    List<TaskInfo> selectTasks();

    /**添加定时任务*/
    Result addJob(TaskInfoReq taskInfoReq);

    /**删除任务*/
    Result delete(TaskInfoReq reqVo);

    /**更新任务*/
    Result updateJob(TaskInfoReq taskInfoReq);

    /**暂停任务*/
    Result pauseJob(Integer taskId);

    /**恢复任务*/
    Result resumeJob(Integer taskId);

}
