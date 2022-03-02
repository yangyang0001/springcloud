package com.deepblue.controller;

import com.deepblue.entity.User;
import com.deepblue.service.RibbonConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class RibbonConsumerController {

    @Autowired
    private RibbonConsumerService ribbonConsumerService;

    @RequestMapping(value = "/ribbon-consumer-hello", method = RequestMethod.GET)
    public String helloConsumer() {
        return ribbonConsumerService.helloConsumer();
    }

    @RequestMapping(value = "/ribbon-consumer-getuser", method = RequestMethod.GET)
    public User getUserById(Long userId) {
        return ribbonConsumerService.getUserById(userId);
    }
}
