package com.deepblue.inaction_01_rabbitmq.rabbitmq_01.book;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitProducer {
	
	public static final String EXCHANGE_NAME = "exchange_demo";
	public static final String ROUTING_KEY = "routingkey_demo";
	public static final String QUEUE_NAME = "queue_demo";
	public static final String IP_ADDRESS = "192.168.188.19";
	public static final int PORT = 5672;	//RabbitMQ默认的服务器端口为:5672
	
	public static void main(String[] args) throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		
		ConnectionFactory factory = new ConnectionFactory();
		//Connection的第一种链接方式:
		String username = "root";
		String password = "123456";
		factory.setHost(IP_ADDRESS);
		factory.setPort(PORT);
		factory.setUsername(username);
		factory.setPassword(password);
		
		//Connection的第二种链接方式:称之为URI链接方式,链接格式为:amqp://username:password@ipaddress:port
//		String uriString = "amqp://root:root123@192.168.120.246:5672";
//		factory.setUri(uriString);
		
		//创建链接
		Connection connection = factory.newConnection();
		
		//创建通信通道,channel是线程不安全的,每个线程创建自己的channel(信道)
		Channel channel = connection.createChannel();
		
		
		//创建一个type="direct",持久化的,非自动删除的交换器
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, null);
		
		/**
		 * 解释一下上面的参数:
		 * 交换器使用之前都是先声明的channel.exchangeDeclare(exchange, type, durable, autoDelete, arguments)
		 * exchange:	交换器的名称
		 * type:		交换器的类型,常用的四种类型:fanout,direct,topic,headers
		 * durable:		是否是持久化的,true的含义为	当前的交换器可以持久化到硬盘中,服务器重启的时候不丢失相关的信息
		 * autoDelete:	是否是自动删除的,设置为true的时候: 当没有Queue绑定在当前的Exchange的时候,自动删除该Exchange
		 * arguments:	一些机构化参数
		 */
		
		//创建一个持久化的,非排他的、非自动删除的队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		//将交换器和队列通过  路由键  绑定
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

		for (int i = 0; i < 1000; i++) {
			//发送一条消息
			String message = "Hello World " + i;
			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}

		//关闭资源
		channel.close();
		connection.close();
	}

}
