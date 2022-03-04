package com.deepblue.inaction_01_rabbitmq.rabbitmq_02.message_property;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * User: YANG
 * Date: 2019/7/4-13:45
 * Description: No Description
 */
public class RabbitMQMessagePropertyProducer {

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


		//4.设置 message 的属性
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("aaa_key", "aaa_value");
		headers.put("bbb_key", "ccc_value");

		AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
				.expiration("30000")    //设置过期时间
				.contentEncoding("UTF-8")
				.headers(headers)
				.build();

		//5.通过channel发送数据
		for(int i = 0; i < 10; i++){
			String message = "HelloWorld-" + i;
			channel.basicPublish("", "test01", properties, message.getBytes());
		}

		//6.关闭
		channel.close();
		connection.close();

	}
}
