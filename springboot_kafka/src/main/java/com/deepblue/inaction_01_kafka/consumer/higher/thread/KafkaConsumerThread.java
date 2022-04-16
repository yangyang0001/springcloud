package com.deepblue.inaction_01_kafka.consumer.higher.thread;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Map;

public class KafkaConsumerThread extends Thread{
	
	private KafkaConsumer<String, String> kafkaConsumer;
	private String topic;
	private int threadId;
	
	public KafkaConsumerThread(Map<String, Object> config, String topic, int threadId){
		this.kafkaConsumer = new KafkaConsumer<String, String>(config);
		this.topic = topic;
		this.threadId = threadId;
		kafkaConsumer.subscribe(Arrays.asList(this.topic));
	}
	

	@Override
	public void run() {
		try {
			while(true){
				ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
				for(ConsumerRecord<String, String> consumerRecord : consumerRecords){
					System.out.println("threadId =========:" + threadId + "=========================================");
					System.out.println("topic ------------:" + consumerRecord.topic());
					System.out.println("partition --------:" + consumerRecord.partition());
					System.out.println("offset -----------:" + consumerRecord.offset());
					System.out.println("key --------------:" + consumerRecord.key());
					System.out.println("value ------------:" + consumerRecord.value());
					System.out.println("============================================================================");
				}
				Thread.sleep(1000L);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			kafkaConsumer.close();
		}
		
	}
	
	

}
