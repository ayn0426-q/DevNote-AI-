package com.qq.service;

import com.qq.pojo.User;

public interface UserService {
    User findByName( String username);


    void register(String username, String password);



    void update(User user);

    void updateAvater(String avatarUrl);

    void updatePwd(String newPwd);
}
