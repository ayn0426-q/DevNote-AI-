package com.qq.service.impl;
import com.qq.mapper.UserMapper;
import com.qq.pojo.User;
import com.qq.service.UserService;
import com.qq.utils.Md5Util;
import com.qq.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserImpl implements UserService {
    @Autowired
   private UserMapper userMapper;
    @Override
    public User findByName(String username){
        User u=userMapper.FindUsername(username);
        return u;
    }

    @Override
    public void register(String username, String password){
        java.lang.String md5String =Md5Util.getMD5String(password);
        userMapper.add(username,md5String);
    }



    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvater(String avatarUrl) {
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer id=(Integer) map.get("id");
        userMapper.upavater(avatarUrl,id);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer id=(Integer) map.get("id");
        userMapper.updatePwd(Md5Util.getMD5String(newPwd),id);
    }


}
