package com.deepblue.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
@RestController
public class SendController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/rabbit-hello-send-message", method = RequestMethod.GET)
    public String sendMessage() {
        String context = "Hello " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        rabbitTemplate.convertAndSend("test.topic.exchange002", "spring.abc.zhangsan", context);
        return "success";
    }
}
