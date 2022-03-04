package com.deepblue.inaction_01_rabbitmq.rabbitmq_03.ack_noack_requeue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * User: YANG
 * Date: 2019/7/5-14:20
 * Description: No Description
 *
 * 特别注意在时机生产中不用这种方式进行处理,往往不做Nack 和 重回队列的方式,而是进行手动的补偿!
 */
public class RequeueConsumer {

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

				//当属性的num = 0时,进行消息Nack 并且重回队列!
				if((Integer)properties.getHeaders().get("num") == 0){
					//Nack 并且重回队列,放在队列的尾部
					channel.basicNack(envelope.getDeliveryTag(), false, true);
				} else {
					//消息消费确认,必须加上,否则会重复消费,如果配置了这一句,下面的basicConsume()不用加true了
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};

		//5.进行消费
		String queueName = "direct_requeue_queue";
		//这一句配置true:也是进行消息消费确认的,上面的channel.basicAck(envelope.getDeliveryTag(), false),如果没有注释这里省略true就OK
//		channel.basicConsume(queueName, true, consumer);
		channel.basicConsume(queueName, consumer);

	}

}
