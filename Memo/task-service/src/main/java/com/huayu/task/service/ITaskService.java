package com.huayu.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huayu.api.dto.Result;
import com.huayu.task.domain.Task;

import javax.servlet.http.HttpServletRequest;

/**
 * 代办业务层接口
 */
public interface ITaskService extends IService<Task> {
    Result saveTask(Task task, HttpServletRequest request);

    Result listTask(Integer status, HttpServletRequest request,Integer current);

    Result updateStatus(HttpServletRequest request, Task task);

    Result removeTaskById(Long id, HttpServletRequest request);

    Result removeAllTaskComplete(HttpServletRequest request);

    Result removeAllTask(HttpServletRequest request);

    Result listTaskLike(String context, HttpServletRequest request,Integer current);
}
