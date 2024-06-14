package com.huayu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huayu.api.domain.User;
import com.huayu.api.dto.Result;


/**
 * user服务类
 */
public interface IAuthService extends IService<User> {

    Result login(User user);


}
