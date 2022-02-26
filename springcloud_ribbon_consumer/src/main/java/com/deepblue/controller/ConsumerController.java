package com.deepblue.controller;

import com.deepblue.entity.User;
import com.deepblue.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @RequestMapping(value = "/ribbon-consumer", method = RequestMethod.GET)
    public String helloConsumer() {
        return consumerService.helloConsumer();
    }

    @RequestMapping("/getuser-byid")
    public User getUserById(Long userId) {
        return consumerService.getUserById(userId);
    }
}
