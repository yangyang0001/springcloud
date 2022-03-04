package com.deepblue.inaction_01_rabbitmq.rabbitmq_03.limit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * User: YANG
 * Date: 2019/7/5-11:30
 * Description: No Description
 * <p>
 * 消息消费的限流操作!
 * 特别注意两点:
 *      1. channel.basicQos();
 *      2. 消息消费确认[手动确认,非自动确认],必须加上,否则会重复消费,如果配置了这一句,下面的basicConsume()不用加true了
           channel.basicAck(envelope.getDeliveryTag(), false);
 */
public class LimitConsumer {

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

		//3.创建消费消息的信道
		Channel channel = connection.createChannel();

		//设置客户端接收最大的数量,并且等待borker端返回ack后再进行接收,进行限流的消费
		channel.basicQos(5);
		//channel.basicQos(0, 5, false); 上面的一句话channel.basicQos(5)就是这句话的意思!

		//4.创建消费者
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				System.out.println("message------------:" + new String(body));
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
		String queueName = "direct_limit_queue";
		//这一句配置true:也是进行消息消费确认的,上面的channel.basicAck(envelope.getDeliveryTag(), false),如果没有注释这里省略true就OK
//		channel.basicConsume(queueName, true, consumer);
		channel.basicConsume(queueName, consumer);

	}
}
