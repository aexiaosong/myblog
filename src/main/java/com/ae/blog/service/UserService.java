package com.ae.blog.service;

import com.ae.blog.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
