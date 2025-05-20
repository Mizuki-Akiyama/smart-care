package com.example.demo.controller;

import com.example.demo.model.AuthUser;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthUser user){
        String userId = user.getUserId();
        String password = user.getPassword();
        return ResponseEntity.ok(userService.login(userId,password));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok().build();
    }
}
