package com.deepblue.controller;

import com.deepblue.entity.User;
import com.deepblue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
public class UserController implements UserService{

    @Override
    public User getUserByUserId(Long userId) {
        System.out.println("user service impl get user by userid  method invoke!");
        try {
            Thread.currentThread().sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new User(userId, "username", "12345678");
    }

    @Override
    public User getUserByNameAndPass(String username, String password) {
        System.out.println("user service impl get user by userid  method invoke!");
        try {
            Thread.currentThread().sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new User(1L, username, password);
    }

}
