package com.deepblue.rabbitmq.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface SendMessageChannel {

    @Output("my_message_channel")
    public SubscribableChannel sendMsg();
}
