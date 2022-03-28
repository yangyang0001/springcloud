package com.deepblue.controller;

import com.deepblue.rabbitmq.producer.SendMessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class SendMessageController {

	@Autowired
	private SendMessageChannel sendMessageChannel;

	@RequestMapping("/sendMessage")
	public String sendMessage(String message) {
		Message msg = MessageBuilder.withPayload(message.getBytes()).build();
		sendMessageChannel.sendMsg().send(msg);
		return "success";
	}

}
