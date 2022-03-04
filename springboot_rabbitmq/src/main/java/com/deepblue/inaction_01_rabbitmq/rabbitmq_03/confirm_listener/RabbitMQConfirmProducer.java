package com.deepblue.inaction_01_rabbitmq.rabbitmq_03.confirm_listener;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * User: YANG
 * Date: 2019/7/5-0:27
 * Description: No Description
 *
 * 本程序注意的两点:
 *      1.channel.confirmSelect();
 *      2.channel.addConfirmListener(new ConfirmListener() {
 *
 */
public class RabbitMQConfirmProducer {

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
		//下面必须添加一个监听来监听发送成功后或失败后的处理
		channel.confirmSelect();

		//4.创建一个type="direct",持久化的,非自动删除的交换器
		String exchangeName = "direct_confirm_exchange";
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
		String queueName = "direct_confirm_queue";
		channel.queueDeclare(queueName, true, false, false, null);

		//6.将交换器和队列通过  路由键  绑定
		String routingKey = "confirm_routing_key";
		channel.queueBind(queueName, exchangeName, routingKey);

		//7.发送1条消息
		for(int i = 0; i < 1; i++){
			String message = "confirm-message -" + i;
			channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}

		//添加一个监听,在这里加监听就OK了,往往一条一条的监听,但是放到for循环中会添加多个Listener是不对的! 所以这里模拟的是发一条数据 就看看 borker 有没有返回 ack
		channel.addConfirmListener(new ConfirmListener() {
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("---------------- Has Ack --------------------");
			}

			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("---------------- Has No Ack-----------------");
			}
		});

		//8.关闭资源与连接,这里注释掉,因为有回调的监听
//		channel.close();
//		connection.close();


	}
}
