package com.deepblue.inaction_01_rabbitmq.rabbitmq_02.exchange_03_fanout;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FirstFanoutExchangeConsumer {
	
	public static final String QUEUE_NAME = "MY_FANOUT_QUEUE_01";
	
	public static void main(String[] args) {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.188.19");
		factory.setPort(5672);
		factory.setUsername("root");
		factory.setPassword("123456");

		try {
			Connection connection = factory.newConnection();
			final Channel channel = connection.createChannel();

			// 设置客户端接收 未被ack的 消息个数
			channel.basicQos(64);

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
                                           byte[] body) throws IOException {
					System.out.println(FirstFanoutExchangeConsumer.class.getSimpleName() + "消费信息:--------->" + new String(body, "UTF-8"));
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 书籍中说这样消费者可以防止消息不必要的丢失
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			};

			//这一句配置true:也是进行消息消费确认的,上面的channel.basicAck(envelope.getDeliveryTag(), false),如果没有注释这里省略true就OK
//		    channel.basicConsume(queueName, true, consumer);
			channel.basicConsume(QUEUE_NAME, consumer);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

}
