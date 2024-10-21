package com.aida.cat.service;

import com.aida.cat.model.User;

import java.util.List;

public interface UserService {
    boolean regUser(User sta);
    boolean editUser(User user);
    User loginUser(User user);
}
