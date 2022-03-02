package com.deepblue.controller;

import com.deepblue.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class HelloController implements HelloService{

    @Override
    public String sayHello() {
        System.out.println("hello service impl say hello method invoke!");
        return "Hello Word";
    }

    @Override
    public String sayHelloPerson(String username) {
        System.out.println("hello service impl say hello person method invoke!");
        return "Hello " + username;
    }

}
