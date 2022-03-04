package com.deepblue.inaction_01_rabbitmq.rabbitmq_03.return_listener;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * User: YANG
 * Date: 2019/7/5-0:27
 * Description: No Description
 *
 * 本程序注意两点:
 *      1.
 *       //正确的routing key 到queue中
		 channel.basicPublish(exchangeName, routingKey, true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		 //错误的routing key 或 消息不能通过这个routingKey 到 queue中
		 channel.basicPublish(exchangeName, "error_routing_key", true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		 解释一下上面的参数 channel.basicPublish(String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body)
		 exchange   : 交换机名称
		 routingKey : 路由键
		 mandatory  : 强制性的意思,默认为false; 取值true的时候, 可以使用channel.addReturnListener() 监听没有发送到queue中message; false的时候不可以添加
		 props      : 参数
		 body       : 消息
 *
 *      2.channel.addReturnListener(new ReturnListener() {
 *
 */
public class RabbitMQReturnProducer {

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
		String exchangeName = "direct_return_exchange";
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
		String queueName = "direct_return_queue";
		channel.queueDeclare(queueName, true, false, false, null);

		//6.将交换器和队列通过  路由键  绑定
		String routingKey = "return_routing_key";
		channel.queueBind(queueName, exchangeName, routingKey);

		//7.发送10条消息
		for(int i = 0; i < 10; i++){
			String message = "return-message -" + i;
			//正确的routing key 到queue中
			channel.basicPublish(exchangeName, routingKey, true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			//错误的routing key 或 消息不能通过这个routingKey 到 queue中
			channel.basicPublish(exchangeName, "error_routing_key", true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

			/**
			 * 解释一下上面的参数 channel.basicPublish(String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body)
			 * exchange   : 交换机名称
			 * routingKey : 路由键
			 * mandatory  : 强制性的意思,默认为false; 取值true的时候, 可以使用channel.addReturnListener() 监听没有发送到queue中message; false的时候不可以添加
			 * props      : 参数
			 * body       : 消息
			 */
		}

		//8.添加一个ReturnListener 来监听 没有发送到queue中的消息!
		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
				System.out.println("replyCode -------------:" + replyCode);
				System.out.println("replyText -------------:" + replyText);
				System.out.println("exchange  -------------:" + exchange);
				System.out.println("routingKey ------------:" + routingKey);
				System.out.println("properties ------------:" + properties.toString());
				System.out.println("body ------------------:" + new String(body));
				System.out.println("----------------------------------------------------------------------------------");
			}
		});
		

		//8.关闭资源与连接,这里注释掉,因为有回调的监听
//		channel.close();
//		connection.close();


	}
}
