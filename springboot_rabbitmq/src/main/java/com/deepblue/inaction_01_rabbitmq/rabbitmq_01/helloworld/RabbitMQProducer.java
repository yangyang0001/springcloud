package com.deepblue.inaction_01_rabbitmq.rabbitmq_01.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * User: YANG
 * Date: 2019/7/4-13:45
 * Description: No Description
 */
public class RabbitMQProducer {

	public static void main(String[] args) throws IOException, TimeoutException {

		//1.创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.188.19");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		connectionFactory.setUsername("root");
		connectionFactory.setPassword("123456");

		//2.创建Connection
		Connection connection = connectionFactory.newConnection();

		//3.创建Channel
		Channel channel = connection.createChannel();


		//4.通过channel发送数据
		for(int i = 0; i < 10; i++){
			String message = "HelloWorld-" + i;
			channel.basicPublish("", "test01", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}

		//5.关闭
		channel.close();
		connection.close();

	}
}
