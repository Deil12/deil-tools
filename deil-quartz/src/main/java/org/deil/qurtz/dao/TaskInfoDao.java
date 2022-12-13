package org.deil.qurtz.dao;

import org.apache.ibatis.annotations.Mapper;
import org.deil.qurtz.commom.domain.entity.TaskInfo;
import org.deil.qurtz.commom.domain.vo.TaskInfoReq;

import java.util.List;

/**
 * @PURPOSE 任务信息 dao 层
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Mapper
public interface TaskInfoDao {

    TaskInfo selectByJobName(String jobName);

    List<TaskInfo> selectAll();

    List<TaskInfo> selectTaskInfos(TaskInfoReq taskInfo);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(TaskInfo record);

    TaskInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskInfo record);

}
