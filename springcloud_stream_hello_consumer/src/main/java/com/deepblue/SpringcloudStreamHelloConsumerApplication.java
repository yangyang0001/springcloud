package com.deepblue;

import com.deepblue.rabbitmq.consumer.ReceMessageChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableEurekaClient
@SpringBootApplication
@EnableBinding(value = {ReceMessageChannel.class})
public class SpringcloudStreamHelloConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudStreamHelloConsumerApplication.class, args);
    }

}
