package com.deepblue.service;

import com.deepblue.fallback.HelloServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "springcloud-hello-service", fallback = HelloServiceFallback.class)
public interface HelloService {

    @RequestMapping(value = "/hello-service-say-hello", method = RequestMethod.GET)
    public String sayHello();

    @RequestMapping(value = "/hello-service-say-hello-person", method = RequestMethod.GET)
    public String sayHelloPerson(@RequestParam("username") String username);

}
