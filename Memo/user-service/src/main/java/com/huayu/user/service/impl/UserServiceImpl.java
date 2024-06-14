package com.huayu.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huayu.api.mapper.UserMapper;
import com.huayu.api.exception.BusinessException;
import com.huayu.api.utils.Code;
import com.huayu.user.service.IUserService;
import com.huayu.api.domain.User;
import com.huayu.api.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * user服务层实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Autowired
    private UserMapper userMapper;

    /**
     * 注册用户
     *
     * @param user 用户实体类
     * @return 创建成功与否的消息
     */
    @Override
    public Result register(User user) {
        try {
            creatUserWithPassword(user);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BusinessException(Code.BUSINESS_ERR, "创建用户失败,该用户名已经被注册,请注意使用合法的字符以及账号密码长度");
        }
        return Result.ok("创建用户成功");
    }

    private User creatUserWithPassword(User user) throws SQLIntegrityConstraintViolationException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        try {
            userMapper.saveUser(user);
        } catch (Exception e) {
            throw new SQLIntegrityConstraintViolationException();
        }
        return user;
    }

}
