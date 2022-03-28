package com.deepblue.rabbitmq.consumer;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * User: YANG
 * Date: 2019/7/23-13:00
 * Description: No Description
 */
@Component
public class ReceMessageListener {

	@StreamListener("my_message_channel")
	public void listener(String message) {
		System.out.println("ReceMessageListener receive message is :" + message);
	}

}
