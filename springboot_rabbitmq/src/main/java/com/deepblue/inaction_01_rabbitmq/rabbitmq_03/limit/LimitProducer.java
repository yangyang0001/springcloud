package com.deepblue.inaction_01_rabbitmq.rabbitmq_03.limit;

import com.rabbitmq.client.*;

/**
 * User: YANG
 * Date: 2019/7/5-11:31
 * Description: No Description
 */
public class LimitProducer {

	public static void main(String[] args) throws Exception {

		//1.创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.188.19");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		connectionFactory.setUsername("root");
		connectionFactory.setPassword("123456");

		//2.创建Connection
		Connection connection = connectionFactory.newConnection();

		//3.创建发送消息的信道 并 确认消息的确认模式
		Channel channel = connection.createChannel();

		//4.创建一个type="direct",持久化的,非自动删除的交换器
		String exchangeName = "direct_limit_exchange";
		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, null);
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
		String queueName = "direct_limit_queue";
		channel.queueDeclare(queueName, true, false, false, null);

		//6.将交换器和队列通过  路由键  绑定
		String routingKey = "limit_routing_key";
		channel.queueBind(queueName, exchangeName, routingKey);

		//7.发送10条消息
		for (int i = 0; i < 10; i++) {
			String message = "limit-message -" + i;
			channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}


	}
}
