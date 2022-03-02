package com.deepblue.fallback;

import com.deepblue.entity.User;
import com.deepblue.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class UserServiceFallback implements UserService {

    public User getUserByUserId(@RequestParam("userId") Long userId) {
        return new User(userId, "", "");
    }

    public User getUserByNameAndPass(@RequestParam("username") String username, @RequestParam("password") String password) {
        return new User(0L, username, password);
    }
}
