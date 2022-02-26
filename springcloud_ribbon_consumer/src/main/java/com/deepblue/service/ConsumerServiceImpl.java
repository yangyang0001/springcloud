package com.deepblue.service;

import com.deepblue.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@Service
public class ConsumerServiceImpl implements ConsumerService{

    @Autowired
    private RestTemplate restTemplate;


    @Override
    @HystrixCommand(fallbackMethod = "helloFallback")
    @RequestMapping(value = "/ribbon-consumer", method = RequestMethod.GET)
    public String helloConsumer() {
        System.out.println("hello consumer method before invoke ...");
        return restTemplate.getForObject("http://springcloud-hello-service/sayHello", String.class);
    }

    public String helloFallback() {
        return "error";
    }

    @Override
    @RequestMapping("/getuser-byid")
    public User getUserById(Long userId) {
        System.out.println("get user by userId method before invoke ...");
        return restTemplate.getForObject("http://springcloud-hello-service/getUserById?userId={1}", User.class, userId);
    }


}
