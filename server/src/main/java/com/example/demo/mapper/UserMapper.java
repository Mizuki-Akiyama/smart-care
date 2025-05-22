package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void register(User user);

    User login(String userId, String password);

    User getUserById(String id);

    void updateUser(User user);
}
