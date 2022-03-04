package com.deepblue.inaction_01_rabbitmq.rabbitmq_01.book;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {
	
	public static final String IP_ADDRESS = "192.168.188.19";
	public static final int PORT = 5672;
	public static final String QUEUE_NAME = "queue_demo";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		
		ConnectionFactory factory = new ConnectionFactory();
		String username = "root";
		String password = "123456";
		
		factory.setUsername(username);
		factory.setPassword(password);
		
		//创建链接,和生产者的创建链接的方式有区别注意区分
		Address[] addresses = new Address[]{new Address(IP_ADDRESS, PORT)};
		Connection connection = factory.newConnection(addresses);
		
		//创建信道
		final Channel channel = connection.createChannel();
		//设置客户端接收	未被ack的	消息个数
		channel.basicQos(64);
		
		//创建消费者
		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println("message----------:" + new String(body));
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//消息消费确认,必须加上,否则会重复消费,如果配置了这一句,下面的basicConsume()不用加true了
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		//这一句配置true:也是进行消息消费确认的,上面的channel.basicAck(envelope.getDeliveryTag(), false),如果没有注释这里省略true就OK
//		channel.basicConsume(QUEUE_NAME, true, consumer);
		channel.basicConsume(QUEUE_NAME, consumer);
		
		//等待回调函数执行完毕后,关闭 资源,这里让线程休息5秒
//		TimeUnit.SECONDS.sleep(5);
//		channel.close();
//		connection.close();
	}

}
