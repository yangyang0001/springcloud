package com.deepblue.inaction_01_kafka.producer.thread;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

/**
 * 使用多线程的生产者进行生产消息!
 * 目前一个线程处理一条消息,不知道这种提高并发的方式是否真正的能够提高效率!
 * @author YangJianWei
 */
public class KafkaProducerThread implements Runnable{
	
	private static Logger LOG = Logger.getLogger(KafkaProducerThread.class);
	
	private KafkaProducer<String, String> kafkaProducer = null;
	private ProducerRecord<String, String> producerRecord = null;
	
	public KafkaProducerThread(KafkaProducer<String, String> kafkaProducer, ProducerRecord<String, String> producerRecord) {
		this.kafkaProducer = kafkaProducer;
		this.producerRecord  = producerRecord;
	}

	@Override
	public void run() {
		this.kafkaProducer.send(producerRecord, new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if(exception != null){
					LOG.error(exception.getMessage());
				}
				if(metadata != null){
					System.out.println("threadID ------:" + Thread.currentThread().getId() + 
									   ",topic --------:" + metadata.topic() + 
									   ",partition ----:" + metadata.partition() + 
									   ",offset -------:" + metadata.offset());	
				}
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public KafkaProducer<String, String> getKafkaProducer() {
		return kafkaProducer;
	}

	public void setKafkaProducer(KafkaProducer<String, String> kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}

	public ProducerRecord<String, String> getProducerRecord() {
		return producerRecord;
	}

	public void setProducerRecord(ProducerRecord<String, String> producerRecord) {
		this.producerRecord = producerRecord;
	}
	
}
