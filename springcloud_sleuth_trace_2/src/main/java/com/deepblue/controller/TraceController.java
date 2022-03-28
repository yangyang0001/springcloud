package com.deepblue.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class TraceController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/trace-2", method = RequestMethod.GET)
    public String trace1() {
        logger.info("===<call trace-2>===");
        return "trace";
    }


}
