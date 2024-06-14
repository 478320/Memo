package com.huayu.auth.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huayu.api.dto.LoginUser;
import com.huayu.api.utils.JwtUtil;
import com.huayu.api.domain.User;
import com.huayu.api.dto.Result;
import com.huayu.api.mapper.UserMapper;
import com.huayu.auth.service.IAuthService;
import com.huayu.api.utils.RedisConstans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * user服务层实现类
 */
@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements IAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis  userid作为key
        String loginUserStr = JSONUtil.toJsonStr(loginUser);
        stringRedisTemplate.opsForValue().set(RedisConstans.LOGIN_USER_KEY + userid, loginUserStr, 24, TimeUnit.HOURS);
        return Result.ok("登录成功",map);
    }

}
