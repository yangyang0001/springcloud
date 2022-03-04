package com.deepblue.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 */
@Configuration
public class RabbitConfig {

    /*
     * 通过Bean的方式创建 Exchange, Queue, Binding关系
     * 一旦启动就会自动创建如下的Exchange, Queue, Binding
     * @return
     */
    @Bean
    public DirectExchange exchange001() {
        return new DirectExchange("test.direct.exchange001", true ,false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("test.direct.queue001", true, false, false, null);
    }

    @Bean
    public Binding binding001() {
        return new Binding("test.direct.queue001", Binding.DestinationType.QUEUE, "test.direct.exchange001", "direct_routing_key", null);
    }



    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("test.topic.exchange002", true, false);
    }

    @Bean
    public Queue queue002_1() {
        return new Queue("test.topic.queue002_1", true, false, false, null);
    }

    @Bean
    public Queue queue002_2() {
        return new Queue("test.topic.queue002_2", true, false, false, null);
    }

    @Bean
    public Binding binding002_1() {
        return BindingBuilder.bind(new Queue("test.topic.queue002_1", true, false, false, null))
                .to(new TopicExchange("test.topic.exchange002", true, false))
                .with("spring.#");
    }

    @Bean
    public Binding binding002_2() {
        return BindingBuilder.bind(new Queue("test.topic.queue002_2", true, false, false, null))
                .to(new TopicExchange("test.topic.exchange002", true, false))
                .with("mq.#");
    }


}
