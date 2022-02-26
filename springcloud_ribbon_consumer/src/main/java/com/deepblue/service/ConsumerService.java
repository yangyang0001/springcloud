package com.deepblue.service;

import com.deepblue.entity.User;

public interface ConsumerService {

    public String helloConsumer();

    public User getUserById(Long userId);
}
