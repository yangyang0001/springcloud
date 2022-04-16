package com.deepblue.inaction_01_kafka.consumer.higher.thread;

import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerThreadTest {
	
	public static void main(String[] args) {
		
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("bootstrap.servers", "192.168.188.35:9092, 192.168.188.35:9092, 192.168.188.35:9092");
		config.put("group.id", "test");	//属于哪个分组
		config.put("client.id", "test");	//客户端定义一个ID,区分不同的客户端!
		/**
		 * 自动提交的时候要配置的连个参数!
		 * 自动提交和手动提交偏移量是kafka对offset的两种提交策略!自动提交的一定要配置这两个参数!(这也是默认的提交策略)
		 */
		config.put("enable.auto.commit", true); 		//显示设置偏移量自动提交
		config.put("auto.commit.interval.ms", 1000); 	//设置偏移量提交时间间隔
		
//		config.put("enable.auto.commit", false);		//设置手动提交偏移量
//		config.put ("fetch.max.bytes", 1024); 			//为了便于测试,这里设置一次 fetch 请求取得的数据最大值为 lKB,默认是 5MB
		
		config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer") ;
		String topic = "GuPiaoHangQing";
		
		for(int i = 0; i < 5; i++){
			new KafkaConsumerThread(config, topic, i).start();
		}
	}

}
