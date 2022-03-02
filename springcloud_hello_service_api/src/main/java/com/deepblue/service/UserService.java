package com.deepblue.service;

import com.deepblue.entity.User;
import com.deepblue.fallback.UserServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "springcloud-hello-service", fallback = UserServiceFallback.class)
public interface UserService {

    @RequestMapping(value = "/hello-service-getuser", method = RequestMethod.GET)
    public User getUserByUserId(@RequestParam("userId") Long userId);

    @RequestMapping(value = "/hello-service-getuser-pass", method = RequestMethod.GET)
    public User getUserByNameAndPass(@RequestParam("username") String username, @RequestParam("password") String password);
}
