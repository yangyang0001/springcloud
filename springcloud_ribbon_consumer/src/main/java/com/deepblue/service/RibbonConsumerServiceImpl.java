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
public class RibbonConsumerServiceImpl implements RibbonConsumerService {

    @Autowired
    private RestTemplate restTemplate;


    @Override
    @HystrixCommand(fallbackMethod = "helloFallback")
    public String helloConsumer() {
        System.out.println("ribbon consumer service hello consumer method before invoke ...");
        return restTemplate.getForObject("http://springcloud-hello-service/hello-service-say-hello", String.class);
    }

    public String helloFallback() {
        return "error";
    }

    @Override
    @HystrixCommand(fallbackMethod = "getUserFallback")
    public User getUserById(Long userId) {
        System.out.println("ribbon consumer service get user by userId method before invoke ...");
        return restTemplate.getForObject("http://springcloud-hello-service/hello-service-getuser?userId={1}", User.class, userId);
    }

    public User getUserFallback(Long userId) {
        return new User(userId, "", "");
    }


}
