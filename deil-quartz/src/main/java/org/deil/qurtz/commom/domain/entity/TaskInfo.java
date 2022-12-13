package org.deil.qurtz.commom.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @PURPOSE 任务信息
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Data
public class TaskInfo implements Serializable {

    private Integer id;

    private String cron;

    private String jobName;

    private String status;

    private Date createTime;

    private Date updateTime;

}
