package com.deepblue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SpringcloudSleuthTrace2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudSleuthTrace2Application.class, args);
    }

}
