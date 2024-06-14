package com.huayu.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huayu.api.domain.User;
import com.huayu.api.dto.Result;


/**
 * user服务类
 */
public interface IUserService extends IService<User> {

    Result register(User user);

}
