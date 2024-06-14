package com.huayu.auth.controller;


import com.huayu.api.domain.User;
import com.huayu.api.dto.Result;
import com.huayu.auth.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * user的表现层对象
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    /**
     * 登录
     *
     * @param user 登录的用户
     * @return 用户登录凭证
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        //登录
        return authService.login(user);
    }


}
