package com.deepblue.rabbitmq.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 *
 */
public interface ReceMessageChannel {

    @Input("my_message_channel")
    public SubscribableChannel getMsg();
}
