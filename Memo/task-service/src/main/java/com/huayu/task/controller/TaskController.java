package com.huayu.task.controller;


import com.huayu.api.dto.Result;
import com.huayu.ratelimit.annotation.RateLimit;
import com.huayu.task.domain.Task;
import com.huayu.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 代办表现层对象
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskService;

    /**
     * 添加代办的接口方法
     *
     * @param task 用户传入的代办信息
     */
    @PostMapping
    public Result saveTask(@RequestBody Task task, HttpServletRequest request) {
        return taskService.saveTask(task, request);
    }

    /**
     * 查询已完成/未完成代办的接口方法
     *
     * @param request 携带查询头的用户唯一token信息
     * @param status  查询的任务的状态
     */
    @RateLimit(prefixKey = "list",timeRange = 10,timeUnit = TimeUnit.SECONDS,maxCount = 3)
    @GetMapping("/{status}")
    public Result listTask(@PathVariable("status") Integer status,@RequestParam(value = "current", defaultValue = "0") Integer current,HttpServletRequest request) {
        return taskService.listTask(status, request,current);
    }

    @GetMapping("/like")
    public Result listTaskLike(@RequestParam(value = "context") String context,@RequestParam(value = "current", defaultValue = "0") Integer current,HttpServletRequest request) {
        return taskService.listTaskLike(context, request,current);
    }

    /**
     * 更新代办状态的接口方法
     *
     * @param request 携带查询头的用户唯一token信息
     * @param task    携带更改用户状态的请求体
     */
    @PutMapping()
    public Result updateStatus( HttpServletRequest request, @RequestBody Task task) {
        return taskService.updateStatus(request, task);
    }

    /**
     * 删除代办的接口方法
     *
     * @param id      要删除的代办的唯一id
     * @param request 携带查询头的用户唯一token信息
     */
    @DeleteMapping("/{id}")
    public Result removeTaskById(@PathVariable("id") Long id, HttpServletRequest request) {
        return taskService.removeTaskById(id, request);
    }

    /**
     * 删除所有已经完成的代办的方法
     *
     * @param request 携带查询头的用户唯一token信息
     */
    @DeleteMapping("/completed")
    public Result removeAllTaskCompleted(HttpServletRequest request) {
        return taskService.removeAllTaskComplete(request);
    }

    /**
     * 删除所有代办的方法
     *
     * @param request 携带查询头的用户唯一token信息
     */
    @DeleteMapping("/all")
    public Result removeAllTask(HttpServletRequest request) {
        return taskService.removeAllTask(request);
    }

}
