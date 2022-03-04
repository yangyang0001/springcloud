package com.deepblue.inaction_01_rabbitmq.rabbitmq_02.exchange_03_fanout;

import com.rabbitmq.client.*;

/**
 * User: YANG
 * Date: 2019/7/4-17:11
 * Description: No Description
 * 注意:
 *      Fanout 不会根据任何的routing key来处理,会发送消息到任何与之绑定的 queue 中
 *      这里fanout exchange 绑定了两个队列, MY_FANOUT_QUEUE_01,MY_FANOUT_QUEUE_02
 *
 */
public class FanoutExchangeProducer {

	public static final String EXCHANGE_NAME = "MY_FANOUT_EXCHANGE";
	public static final String QUEUE_NAME_01 = "MY_FANOUT_QUEUE_01";
	public static final String QUEUE_NAME_02 = "MY_FANOUT_QUEUE_02";

	public static void main(String[] args) throws Exception {

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

		//6.Fanout Exchange 不需要routing key,但是不能为 null 否则报错
		channel.queueBind(QUEUE_NAME_01, EXCHANGE_NAME, "");
		channel.queueBind(QUEUE_NAME_02, EXCHANGE_NAME, "");

		//7.发送10条消息
		for (int i = 0; i < 10; i++) {
			String message = "fanout-exchange-message -" + i;
				channel.basicPublish(EXCHANGE_NAME, "" , MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}

		//8.关闭资源
		channel.close();
		connection.close();
	}
}
