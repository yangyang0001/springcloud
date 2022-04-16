package com.deepblue.inaction_01_kafka.partition;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 使用自定义分区器!在kafkaProperties中添加属性
 * 现在分区已经固定为5个
 * 		AAAA aaaa partition=1
 * 		BBBB bbbb partition=0
 * 		CCCC cccc partition=5
 * 		DDDD cccc partition=5
 * 		默认分区器和自定义分区器都是根据key值来判定到底被发送到哪个分区!
 * 		这里使用自定义的分区器来处理,key=cccc时被发送到最后一个分区!
 * @author YangJianWei
 */
public class PartitionTest {
	
	private static Properties kafkaProperties = new Properties();
	
	static {
		//服务器节点的匹配
		kafkaProperties.put("bootstrap.servers", "192.168.188.35:9092, 192.168.188.35:9092, 192.168.188.35:9092");

		//key序列化规则器
		kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		//value序列化规则器
		kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		//在生产者中使用自定义的分区器!
		kafkaProperties.put("partitioner.class", "com.inspur.partition.MyPartitioner");
	}
	
	private static KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(kafkaProperties);

	/**
	 * Kafka会使用默认的分区器来处理消息去哪个分区!
	 * 被默认分区器分配到哪个分区是根据key值来判断的!
	 * 
	 * 
	 * 这里不使用自定义的分区器来处理消息去哪个分区!
	 */
	public static void sendMessageYiBan(){
		String topic = "AAAA";
		String key = "aaaa";
		String value = "USA";
		
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key, value);
	
		try {
			/*Future<RecordMetadata> future = */kafkaProducer.send(producerRecord);
			/*System.out.println("topic --------------------:" + future.get().topic());
			System.out.println("partition ----------------:" + future.get().partition());
			System.out.println("offset -------------------:" + future.get().offset());
			System.out.println("serializedKeySize --------:" + future.get().serializedKeySize());
			System.out.println("serializedValueSize ------:" + future.get().serializedValueSize());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 在某些特殊的情况下,如果有一些消息是比较重要的,且不想和其他的某些消息放在同一个分区中;这就要你去强制的去把这类重要的消息放到指定的分区!
	 */
	public static void sendMessageByMyPartitioner(){
		String topic = "DDDD";
		String key = "cccc";
		String value = "China";
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key, value);
		
		try {
			/*Future<RecordMetadata> future = */kafkaProducer.send(producerRecord);
			/*System.out.println("topic --------------------:" + future.get().topic());
			System.out.println("partition ----------------:" + future.get().partition());
			System.out.println("offset -------------------:" + future.get().offset());
			System.out.println("serializedKeySize --------:" + future.get().serializedKeySize());
			System.out.println("serializedValueSize ------:" + future.get().serializedValueSize());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		sendMessageYiBan();
		System.out.println("==================================================================================================");
		sendMessageByMyPartitioner();
	}
	
}
