package com.deepblue.inaction_01_rabbitmq.rabbitmq_03.dlx;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User: YANG
 * Date: 2019/7/5-16:19
 * Description: No Description
 *
 *      注意这里只模拟TTL过期没有消费的消息,会到死信队列中!
 *
 *      正常的direct 类型的交换机  direct_dlx_exchange
 *      正常的队列                direct_dlx_queue
 *      正常的routingKey         dlx_routing_key
 *
 *
 *      死信队列
 *          交换机:    dlx.exchange
 *          队列:      dlx.queue
 *          路由键:    dlx.routing.key
 *
 * 特别注意:
 *      在声明正常队列的时候,用arguments来绑定死信队列!
 *      代码中如下的地方特别注意!
 *      //8.4 通过arguments 绑定 死信队列! 必须设置到 声明正常队列的时候!
 *
 *
 */
public class DLXProducer {

	public static void main(String[] args) throws Exception {

		//1.创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.188.19");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("root");
		connectionFactory.setPassword("123456");
		connectionFactory.setVirtualHost("/");

		//2.创建Connection
		Connection connection = connectionFactory.newConnection();

		//3.创建发送消息的信道 并 确认消息的确认模式
		Channel channel = connection.createChannel();

		//4.创建一个type="direct",持久化的,非自动删除的交换器
		String exchangeName = "direct_dlx_exchange";
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

		/**
		 * 第8步拿到这里来使用, 因为下面添加死信队列的使用要使用到!
		 * 第8.4步注意在使用的地方的不同!
		 */
		//8.1创建死信队列所需要的 exchagne
		String dlxExchange = "dlx.exchange";

//		channel.exchangeDeclare(dlxExchange, BuiltinExchangeType.DIRECT, true, false, null);
		channel.exchangeDeclare(dlxExchange, BuiltinExchangeType.TOPIC, true, false, null);

		//8.2创建死信队列所需要的 queue
		String dlxQueue = "dlx.queue";
		channel.queueDeclare(dlxQueue, true, false, false, null);

		//8.3由一个死信队列的路由键将 上面的 死信Exchange 和 死信Queue 绑定
//		String dlxRoutingKey = "dlx.routing.key";
		String dlxRoutingKey = "#";

		channel.queueBind(dlxQueue, dlxExchange, dlxRoutingKey);



		//5.创建一个持久化的,非排他的、非自动删除的队列
		String queueName = "direct_dlx_queue";

		//8.4 通过arguments 绑定 死信队列! 必须设置到 声明正常队列的时候! 必须添加一下两个参数
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("x-dead-letter-exchange", dlxExchange);       //指定死信队列的Exchange
		/**
		 * direct类型的Exchange死信队列,此处必须指定路由键,否则死信队列无法接受到死信息!
		 * 所以这里不论什么类型的Exchange的死信队列都指明 x-dead-letter-routing-key 参数就OK了,本人亲测
		 */
		arguments.put("x-dead-letter-routing-key", dlxRoutingKey);

		//这里使用到了arguments参数来为当前的队列 绑定 死信队列
		channel.queueDeclare(queueName, true, false, false, arguments);

		//6.将交换器和队列通过  路由键  绑定
		String routingKey = "dlx_routing_key";
		channel.queueBind(queueName, exchangeName, routingKey);

		//7.发送10条消息
		for (int i = 0; i < 10; i++) {
			String message = "dlx-message -" + i;
			//TTL属性模拟 死掉的信息,然后通过死信队列的机制发送到死信队列中!
			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
					.expiration("10000").build();

			channel.basicPublish(exchangeName, routingKey, properties, message.getBytes());
		}



	}
}
