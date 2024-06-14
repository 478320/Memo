package com.huayu.auth.service.impl;

import com.huayu.api.dto.LoginUser;
import com.huayu.api.domain.User;
import com.huayu.api.exception.BusinessException;
import com.huayu.api.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.huayu.api.utils.Code.BUSINESS_ERR;

/**
 * userDetail自定义实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户信息
        User user = userMapper.selectUser(username);
        //如果没有查询到用户就抛出异常
        if(Objects.isNull(user)){
            throw new BusinessException(BUSINESS_ERR,"用户名或者密码错误");
        }

        return new LoginUser(user);
    }
}
