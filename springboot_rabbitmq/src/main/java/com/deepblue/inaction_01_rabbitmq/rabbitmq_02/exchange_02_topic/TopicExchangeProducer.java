package com.deepblue.inaction_01_rabbitmq.rabbitmq_02.exchange_02_topic;

import com.rabbitmq.client.*;

/**
 * User: YANG
 * Date: 2019/7/4-16:12
 * Description: No Description
 *
 * 总结:一个Exchange可以通过不同的routing key 来绑定不同的queue
 *
 */
public class TopicExchangeProducer {

	public static final String EXCHANGE_NAME = "MY_TOPIC_EXCHANGE";
	public static final String ROUTING_KEY_01 = "topic-.#";
	public static final String QUEUE_NAME_01 = "MY_TOPIC_QUEUE";

	public static final String ROUTING_KEY_02 = "user.#";
	public static final String QUEUE_NAME_02 = "MY_TOPIC_QUEUE_USER";

	public static void main(String[] args) throws Exception {

		//1.创建ConnectionFactory
		ConnectionFactory factory = new ConnectionFactory();
		//Connection的第一种链接方式:
//		factory.setHost("192.168.188.19");
//		factory.setPort(5672);

		//测试HAProxy 成功
//		factory.setHost("192.168.188.19");
//		factory.setPort(35672);

		//测试Keepalived 成功
		factory.setHost("192.168.188.19");
		factory.setPort(35672);

		factory.setUsername("root");
		factory.setPassword("123456");

		//Connection的第二种链接方式:称之为URI链接方式,链接格式为:amqp://username:password@ipaddress:port
//		String uriString = "amqp://root:root123@192.168.120.246:5672";
//		factory.setUri(uriString);

		//2.创建链接
		Connection connection = factory.newConnection();

		//3.创建通信通道,channel是线程不安全的,每个线程创建自己的channel(信道)
		Channel channel = connection.createChannel();

		//4.创建一个type="topic",持久化的,非自动删除的交换器
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true, false, null);

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
		channel.queueDeclare(QUEUE_NAME_01, true, false, false, null);
		channel.queueDeclare(QUEUE_NAME_02, true, false, false, null);

		//6.将交换器和队列通过  路由键  绑定
		channel.queueBind(QUEUE_NAME_01, EXCHANGE_NAME, ROUTING_KEY_01);
		channel.queueBind(QUEUE_NAME_02, EXCHANGE_NAME, ROUTING_KEY_02);

		//7.发送10条消息
		for (int i = 0; i < 10; i++) {
			if(i % 2 == 0){
				String message = "topic-exchange-message -" + i;
				channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_01, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			} else {
				String message = "user.message -" + i;
				channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_02, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			}
		}

		//8.关闭资源
		channel.close();
		connection.close();
	}
}
