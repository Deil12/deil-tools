package org.deil.qurtz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.deil.qurtz.commom.Result;
import org.deil.qurtz.commom.domain.vo.TaskInfoReq;
import org.deil.qurtz.service.TaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PURPOSE 任务 controller 层
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Api(tags = "定时任务管理接口")
@RestController
@RequestMapping(value = "/task", produces = "application/json;charset=UTF-8")
public class TaskController {

    @Autowired
    private TaskInfoService taskInfoService;

    /**定时器列表*/
    @ApiOperation("查询列表")
    @PostMapping("/list")
    public Result list(@RequestBody TaskInfoReq reqVo) {
        return taskInfoService.selectTaskListByPage(reqVo);
    }

    /**增加任务*/
    @ApiOperation("任务新增")
    @PostMapping("/add")
    public Result add(@RequestBody TaskInfoReq taskInfoReq) {
        return taskInfoService.addJob(taskInfoReq);
    }

    /**删除任务*/
    @ApiOperation("任务删除")
    @PostMapping("/del")
    public Result delete(@RequestBody TaskInfoReq reqVo) {
        return taskInfoService.delete(reqVo);
    }

    /**定时器修改*/
    @ApiOperation("修改")
    @PostMapping("/edit")
    public Result edit(@RequestBody TaskInfoReq reqVo) {
        return taskInfoService.updateJob(reqVo);
    }

    /**暂停任务*/
    @ApiOperation("任务暂停")
    @PostMapping("/pause")
    public Result pause(Integer taskId) {
        return taskInfoService.pauseJob(taskId);
    }

    /**恢复任务*/
    @ApiOperation("任务恢复")
    @PostMapping("/resume")
    public Result resume(Integer taskId) {
        return taskInfoService.resumeJob(taskId);
    }

}
