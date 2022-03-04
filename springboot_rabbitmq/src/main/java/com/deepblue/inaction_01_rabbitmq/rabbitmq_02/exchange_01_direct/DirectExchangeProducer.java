package com.deepblue.inaction_01_rabbitmq.rabbitmq_02.exchange_01_direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * User: YANG
 * Date: 2019/7/4-15:38
 * Description: No Description
 */
public class DirectExchangeProducer {

	public static final String EXCHANGE_NAME = "MY_DIRECT_EXCHANGE";
	public static final String ROUTING_KEY = "MY_DIRECT_ROUTING_KEY";
	public static final String QUEUE_NAME = "MY_DIRECT_QUEUE";

	public static void main(String[] args) throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {

		//1.创建ConnectionFactory
		ConnectionFactory factory = new ConnectionFactory();
		//Connection的第一种链接方式:
		factory.setHost("192.168.188.19");
		factory.setPort(5672);
		factory.setUsername("root");
		factory.setPassword("123456");

		//Connection的第二种链接方式:称之为URI链接方式,链接格式为:amqp://username:password@ipaddress:port
//		String uriString = "amqp://root:root123@192.168.120.246:5672";
//		factory.setUri(uriString);

		//2.创建链接
		Connection connection = factory.newConnection();

		//3.创建通信通道,channel是线程不安全的,每个线程创建自己的channel(信道)
		Channel channel = connection.createChannel();


		//4.创建一个type="direct",持久化的,非自动删除的交换器
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

		//5.创建一个持久化的,非排他的、非自动删除的队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);

		//6.将交换器和队列通过  路由键  绑定
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

		//7.发送10条消息
		for(int i = 0; i < 10; i++){
			String message = "direct-exchange-message -" + i;
			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}

		//8.关闭资源
		channel.close();
		connection.close();
	}
}
