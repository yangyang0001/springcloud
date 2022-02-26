package com.deepblue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class HelloController {

    @RequestMapping("/sayHello")
    public String sayHello() {
        System.out.println("springcloud-hello-service sayHello method before invoke!");
        return "HelloWorld";
    }
}
