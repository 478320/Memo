package com.huayu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.huayu.api.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * user数据层
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    void saveUser(User user);

    User selectUser(String username);

}
