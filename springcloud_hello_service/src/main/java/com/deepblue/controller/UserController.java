package com.deepblue.controller;

import com.deepblue.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class UserController {

    @RequestMapping("/getUserById")
    public User getUserById(Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUsername("username");
        user.setPassword("12345678");
        return user;
    }

}
