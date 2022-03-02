package com.deepblue.controller;

import com.deepblue.entity.User;
import com.deepblue.service.HelloService;
import com.deepblue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class FeignConsumerController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/feign-consumer-hello", method = RequestMethod.GET)
    public String sayHello() {
        return helloService.sayHello();
    }


    @RequestMapping(value = "/feign-consumer-hello-person", method = RequestMethod.GET)
    public String sayHelloPerson(String username) {
        return helloService.sayHelloPerson(username);
    }


    @RequestMapping(value = "/feign-consumer-getuser", method = RequestMethod.GET)
    public User getUserById(Long userId) {
        return userService.getUserByUserId(userId);
    }


    @RequestMapping(value = "/feign-consumer-getuser-pass", method = RequestMethod.GET)
    public User getUserByNameAndPass(Long userId, String username, String password) {
        return userService.getUserByNameAndPass(username, password);
    }
}
