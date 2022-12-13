package org.deil.qurtz.commom.domain.vo;

import lombok.Data;

/**
 * @PURPOSE 任务信息点播
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Data
public class TaskInfoReq {

    /**任务编号*/
    private Integer id;

    /**任务时间表达式*/
    private String cron;

    /**任务状态*/
    private String status;

    /**任务名称*/
    private String jobName;

    /**每页显示条数*/
    private int pageSize=10;

    /**当前页数*/
    private int pageCurrent=1;

}
