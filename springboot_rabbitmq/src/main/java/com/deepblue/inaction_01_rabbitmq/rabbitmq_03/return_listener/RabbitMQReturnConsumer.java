package com.deepblue.inaction_01_rabbitmq.rabbitmq_03.return_listener;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * User: YANG
 * Date: 2019/7/5-0:27
 * Description: No Description
 */
public class RabbitMQReturnConsumer {

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

		//3.创建消费消息的信道
		Channel channel = connection.createChannel();

		//设置客户端接收	未被ack的	消息个数
		channel.basicQos(64);

		//4.创建消费者
		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				System.out.println("message-------------:" + new String(body));
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//消息消费确认,必须加上,否则会重复消费,如果配置了这一句,下面的basicConsume()不用加true了
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};

		//5.进行消费
		String queueName = "direct_return_queue";
		//这一句配置true:也是进行消息消费确认的,上面的channel.basicAck(envelope.getDeliveryTag(), false),如果没有注释这里省略true就OK
//		channel.basicConsume(queueName, true, consumer);
		channel.basicConsume(queueName, consumer);

	}
}
