package com.huayu.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huayu.api.dto.Result;
import com.huayu.task.domain.Task;

import java.util.List;

/**
 * 代办表现层接口
 */
public interface TaskMapper extends BaseMapper<Task> {
    void saveTask(Task task);

    void updateStatus(Task task);

    Task selectTaskById(Long id);

    void deleteTaskById(Long id);

    void deleteTaskCompletedByUserId(Long id);

    void deleteTaskByUserId(Long id);

    List<Task> selectTaskList(Boolean status,Long userId,Integer current);

    List<Task> selectTaskListLike(String context,Long userId,Integer current);
}
