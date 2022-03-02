package com.deepblue.service;

import com.deepblue.entity.User;

public interface RibbonConsumerService {

    public String helloConsumer();

    public User getUserById(Long userId);
}
