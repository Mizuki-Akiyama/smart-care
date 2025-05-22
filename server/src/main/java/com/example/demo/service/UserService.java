package com.example.demo.service;


import com.example.demo.mapper.UserMapper;
import com.example.demo.model.AuthUser;
import com.example.demo.util.JWTUtil;
import com.example.demo.model.User;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public void register(User user){
        String secret = user.getPassword();
        user.setPassword(DigestUtils.sha256Hex(secret));
        userMapper.register(user);
    }

    public AuthUser login(String userId, String password){
        String secret = DigestUtils.sha256Hex(password);
        User user = userMapper.login(userId,secret);
        if(Objects.isNull(user)){
            return null;
        }

        AuthUser authUser = new AuthUser();
        authUser.setUserId(user.getUserId());
        authUser.setPassword(secret);

        authUser.setToken(JWTUtil.createToken(userId,secret));

        return authUser;

    }

    public User getUserById(String id){
        return userMapper.getUserById(id);
    }

    public void updateUser(User user){
        System.out.println(user);
        userMapper.updateUser(user);}
}
