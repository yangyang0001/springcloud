package com.deepblue.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RefreshScope
@RestController
public class GetConfigController {

    @Value("${from}")
    private String from;

    @RequestMapping("/getFrom")
    public String getFrom() {
        return from;
    }
}
