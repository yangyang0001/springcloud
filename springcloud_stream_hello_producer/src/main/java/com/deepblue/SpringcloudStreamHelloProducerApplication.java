package com.deepblue;

import com.deepblue.rabbitmq.producer.SendMessageChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableEurekaClient
@SpringBootApplication
@EnableBinding(value = {SendMessageChannel.class})
public class SpringcloudStreamHelloProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudStreamHelloProducerApplication.class, args);
    }

}
