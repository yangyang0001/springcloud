package com.deepblue.fallback;

import com.deepblue.service.HelloService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class HelloServiceFallback implements HelloService {

    public String sayHello() {
        return "error";
    }

    public String sayHelloPerson(@RequestParam("username") String username) {
        return "error";
    }

}
