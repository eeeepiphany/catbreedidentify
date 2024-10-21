package com.aida.cat.service;


import com.aida.cat.mapper.UserMapper;
import com.aida.cat.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class userServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public boolean regUser(User user) {
        return userMapper.addUser(user) > 0;
    }

    @Override
    public boolean editUser(User user) {
        return userMapper.editUser(user) > 0;
    }

    @Override
    public User loginUser(User user) {
        return userMapper.findUserByIdAndPwd(user);
    }
}

