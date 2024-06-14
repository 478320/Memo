package com.huayu.task.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huayu.api.domain.User;
import com.huayu.api.dto.LoginUser;
import com.huayu.api.dto.Result;
import com.huayu.api.exception.BusinessException;
import com.huayu.ratelimit.annotation.RateLimit;
import com.huayu.task.domain.Task;
import com.huayu.task.mapper.TaskMapper;
import com.huayu.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.huayu.api.utils.Code.BUSINESS_ERR;

/**
 * 代办业务层实现类
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Result saveTask(Task task, HttpServletRequest request) {
        LoginUser loginUser = JSONUtil.toBean(request.getHeader("user-info"), LoginUser.class);
        User user = loginUser.getUser();
        Long userId = user.getId();
        //设置代办的创建者
        task.setUserId(userId);
        task.setCompleted(false);
        try {
            taskMapper.saveTask(task);
        } catch (Exception e) {
            throw new BusinessException(BUSINESS_ERR, "代办字数过多，请分批添加");
        }
        return Result.ok("新增代办成功");
    }


    @Override
    public Result listTask(Integer status, HttpServletRequest request,Integer current) {
        LoginUser loginUser = JSONUtil.toBean(request.getHeader("user-info"), LoginUser.class);
        User user = loginUser.getUser();
        Long userId = user.getId();
        //从数据库查询代办信息
        List<Task> taskList = taskMapper.selectTaskList(Boolean.valueOf(status != 0),userId,current*5);
        //数据库有信息则携带对应的信息数量返回
        return Result.ok(taskList,taskList.size());
    }

    @Override
    public Result listTaskLike(String context, HttpServletRequest request,Integer current) {
        LoginUser loginUser = JSONUtil.toBean(request.getHeader("user-info"), LoginUser.class);
        User user = loginUser.getUser();
        Long userId = user.getId();
        //从数据库查询代办信息
        List<Task> taskList = taskMapper.selectTaskListLike("*"+context+"*",userId,current*5);
        //数据库有信息则携带对应的信息数量返回
        return Result.ok(taskList,taskList.size());
    }

    @Override
    public Result updateStatus(HttpServletRequest request, Task task) {
        LoginUser loginUser = JSONUtil.toBean(request.getHeader("user-info"), LoginUser.class);
        User user = loginUser.getUser();
        Long userId = user.getId();
        if (task.getUserId().equals(userId)){
            taskMapper.updateStatus(task);
        }else {
            return Result.fail("你没有权限更改此条代办");
        }
        return Result.ok("更改成功");
    }

    @Override
    public Result removeTaskById(Long id, HttpServletRequest request) {
        LoginUser loginUser = JSONUtil.toBean(request.getHeader("user-info"), LoginUser.class);
        User user = loginUser.getUser();
        Long userId = user.getId();
        Task task = taskMapper.selectTaskById(id);
        if (!task.getUserId().equals(userId)) {
           return Result.fail("你没有权限进行此操作");
        }
        taskMapper.deleteTaskById(id);
        return Result.ok("删除成功");
    }

    @Override
    public Result removeAllTaskComplete(HttpServletRequest request) {
        LoginUser loginUser = JSONUtil.toBean(request.getHeader("user-info"), LoginUser.class);
        User user = loginUser.getUser();
        Long userId = user.getId();
        taskMapper.deleteTaskCompletedByUserId(userId);
        return Result.ok("删除成功");
    }

    @Override
    public Result removeAllTask(HttpServletRequest request) {
        LoginUser loginUser = JSONUtil.toBean(request.getHeader("user-info"), LoginUser.class);
        User user = loginUser.getUser();
        Long userId = user.getId();
        taskMapper.deleteTaskByUserId(userId);
        return Result.ok("删除成功");
    }
}
