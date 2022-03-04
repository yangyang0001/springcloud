package com.deepblue.inaction_01_rabbitmq.rabbitmq_02.message_property;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: YANG
 * Date: 2019/7/4-13:44
 * Description: No Description
 */
public class RabbitMQMessagePropertyConsumer {

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

		//3.创建Channel
		Channel channel = connection.createChannel();


		//4.创建一个持久化的,非排他的、非自动删除的队列
		String queueName = "test01";
		channel.queueDeclare(queueName, true, false, false, null);

		//5.创建消费者
		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println("message----------:" + new String(body) +
						           ",expiration -------:" + properties.getExpiration() +
						           ",contentEncoding --:" + properties.getContentEncoding());

				Map<String, Object> headers = properties.getHeaders();

				headers.forEach((key , value) -> {
					System.out.println("key -----:" + key + ",value ----:" + value);
				});

				System.out.println("******************************************************************");

				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//消息消费确认,必须加上,否则会重复消费,如果配置了这一句,下面的basicConsume()不用加true了
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};

		//这一句配置true:也是进行消息消费确认的; 上面的channel.basicAck(envelope.getDeliveryTag(), false),如果没有注释这里省略true就OK
//		channel.basicConsume(queueName, true, consumer);
		channel.basicConsume(queueName, consumer);

		//等待回调函数执行完毕后,关闭 资源,这里让线程休息5秒,这里注释掉,因为现实中消费者不可能关闭的,因为一直等待消息来处理就OK了
//		TimeUnit.SECONDS.sleep(5);
//		channel.close();
//		connection.close();

	}
}
