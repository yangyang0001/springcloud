package com.deepblue.inaction_01_kafka.consumer.higher;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * 提交策略分为两种:
 * 		自动提交和手动提交
 * 		自动提交要配置参数
 * 		手动提交又分为两种方式:同步提交和异步提交两种方式!同时也得配置参数！
 * 需要手动提交偏移量的场景:
 * 		在做完一系列的业务之后我们认为成功了!
 * @author YangJianWei
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class KafkaConsumerHighLevelTest{
	
	private static Properties config = new Properties();
	private static KafkaConsumer consumer = null;
	
	private static String BROKER_LIST = "192.168.188.70:9092, 192.168.188.71:9092, 192.168.188.72:9092";

	
	static {
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "first_group");	    //属于哪个分组
		config.put(ConsumerConfig.CLIENT_ID_CONFIG, "first_client_2");	//客户端定义一个ID,区分不同的客户端!

		//配置是否从最开始的偏移量开始读取数据!(group_id和client_id取个全新的名字就OK了)[在测试的时候要特别注意,把这个注释掉]
//		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


		/**
		 * 自动提交的时候要配置的两个参数!
		 * 自动提交和手动提交偏移量是kafka对offset的两种提交策略!自动提交的一定要配置这两个参数!(这也是默认的提交策略)
		 */
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); 		//显示设置偏移量自动提交
		config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000); 	//设置偏移量提交时间间隔

		/**
		 * 手动提交需要配置以下的参数
		 */
//		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);		//设置手动提交偏移量
//		config.put (ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 1024); 			//为了便于测试,这里设置一次 fetch 请求取得的数据最大值为 lKB,默认是 5MB
		
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer") ;
		consumer = new KafkaConsumer(config);
	}
	
	/**
	 * 消费者消费完消息后自动提交消息的offset
	 * 自动提交策略
	 * 由消费者协调器(ConsumerCoordinator)每隔${auto.commit.interval.ms}毫秒执行一次偏移量的提交
	 * 自动提交就不要用commit*的方法了,因为策略不同采用的方式是两种分支!
	 * auto.commit.interval.ms
	 */
	public static void consumeZiDongCommit(String topic){
		consumer.subscribe(Collections.singletonList(topic));
		try {
			while(true){
				ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
				System.out.println("consumerRecords.count -----------------------------:" + consumerRecords.count());
				for(ConsumerRecord<String, String> consumerRecord : consumerRecords){
					System.out.println("partition ---:" + consumerRecord.partition() +
							           ", topic -----:" + consumerRecord.topic() +
							           ", offset ----:" + consumerRecord.offset() +
								       ", value -----:" + consumerRecord.value() +
							           ", key -------:" + consumerRecord.key());
				}
				Thread.sleep(2000L);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}
	
	/**
	 * 手动提交中的同步提交:
	 * 同步提交需要配置参数,不管是同步提交还是异步提交都要提交最后的偏移量offset(消息的偏移量offset) commitSync
	 * @param topic
	 */
	public static void consumeTongBuCommit(String topic){
		consumer.subscribe(Collections.singletonList(topic));
		try {
			while(true){
				ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
				for(ConsumerRecord consumerRecord : consumerRecords){
					System.out.println("topic ------------:" + consumerRecord.topic());
					System.out.println("partition --------:" + consumerRecord.partition());
					System.out.println("offset -----------:" + consumerRecord.offset());
					System.out.println("key --------------:" + consumerRecord.key());
					System.out.println("value ------------:" + consumerRecord.value());
					System.out.println("============================================================================");
				}
				try {
					consumer.commitSync();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}
	
	/**
	 * 手动提交中的异步提交:
	 * 使用回调的异步提交偏移量的方法! commitAsync
	 * @param topic
	 */
	public static void consumeYiBuCommit(String topic){
		consumer.subscribe(Collections.singletonList(topic));
		try {
			while(true){
				ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
				for(ConsumerRecord consumerRecord : consumerRecords){
					System.out.println("topic ------------:" + consumerRecord.topic());
					System.out.println("partition --------:" + consumerRecord.partition());
					System.out.println("offset -----------:" + consumerRecord.offset());
					System.out.println("key --------------:" + consumerRecord.key());
					System.out.println("value ------------:" + consumerRecord.value());
					System.out.println("============================================================================");
				}
				try {
					consumer.commitAsync();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

	/**
	 * 手动提交中的异步提交(有回调方法):
	 * 使用回调的异步提交偏移量的方法! commitAsync
	 * @param topic
	 */
	public static void consumeYiBuCommitWithCallback(String topic){
		consumer.subscribe(Collections.singletonList(topic));
		try {
			while(true){
				ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
				for(ConsumerRecord consumerRecord : consumerRecords){
					System.out.println("topic ------------:" + consumerRecord.topic());
					System.out.println("partition --------:" + consumerRecord.partition());
					System.out.println("offset -----------:" + consumerRecord.offset());
					System.out.println("key --------------:" + consumerRecord.key());
					System.out.println("value ------------:" + consumerRecord.value());
					System.out.println("============================================================================");
				}
				try {
					consumer.commitAsync(new OffsetCommitCallback() {
						@Override
						public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
							if(exception == null){
								System.out.println("commitAsync Callback offsets ------------:" + offsets);
							} else {
								//此时提交发生了异常就需要重新提交这部分的偏移量!
								exception.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

	/**
	 * 根据时间,主题,分区kafka API中提供了一个根据时间查询大于等于这个时间的最近的一条消息的偏移量的方法!
	 */
	public static void consumeRecordWithTime(String topic, int partition, long time){
		//订阅主题分区的方法!
		consumer.assign(Arrays.asList(new TopicPartition(topic, partition)));
		try {
			Map<TopicPartition, Long> timestampsToSearch = new HashMap<TopicPartition, Long>() ;
			//1.构造待查询的分区
			TopicPartition topicPartition = new TopicPartition (topic, partition);
			//2.设置查询time之前消息的偏移量
			timestampsToSearch.put (topicPartition, (System.currentTimeMillis() - time));
			//3.会返回 时间大于等于查找时间的第一个偏移量,这里是核心,获取是大于等于时间后的第一个偏移量的信息!
			Map<TopicPartition, OffsetAndTimestamp> offsetMap = consumer.offsetsForTimes(timestampsToSearch);
			OffsetAndTimestamp offsetTimestamp = null ;
			//这里依然用 for 轮询 ， 当然由于本例是查询的一个分区 ， 因此也可以用 if 处理
			for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : offsetMap. entrySet ()) {
				//若查询时间大于时间戳索引文件中最大记录索引时间,此时 value 为空 ， 即待查询时间点之后没有新消息生成
				offsetTimestamp = entry.getValue() ;
				if (null != offsetTimestamp) {
					consumer.seek(topicPartition, entry.getValue().offset());
				}
			}
			
			while(true){
				ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
				for(ConsumerRecord consumerRecord : consumerRecords){
					System.out.println("topic ------------:" + consumerRecord.topic());
					System.out.println("partition --------:" + consumerRecord.partition());
					System.out.println("offset -----------:" + consumerRecord.offset());
					System.out.println("key --------------:" + consumerRecord.key());
					System.out.println("value ------------:" + consumerRecord.value());
					System.out.println("============================================================================");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}
	
	/**
	 * 消费特定分区,特定偏移量之间的消息
	 * @param topic
	 * @param startOffset   //如果不指定就是从lasted offset开始消费
	 * @param endOffset
	 */
	public static void consumeStartOffsetAndEndOffsetRecord(String topic, int partition, long startOffset, long endOffset){
		TopicPartition topicPartition = new TopicPartition(topic, partition);
		//指定topic个某个分区下的必须用assign否则报No current assignment for partition 异常 
		consumer.assign(Arrays.asList(topicPartition));
		try {
			//从指定的偏移量开始消费
//			consumer.seek(topicPartition, startOffset);
			/**
			 * 从开始偏移量消费,没有起作用,一定要注意!
			 */
//			consumer.seekToBeginning(Arrays.asList(topicPartition));
			//从结束偏移量开始消费
//			consumer.seekToEnd(Arrays.asList(topicPartition));
			while(true){
				ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
				for(ConsumerRecord consumerRecord : consumerRecords){
					System.out.println("topic ------------:" + consumerRecord.topic());
					System.out.println("partition --------:" + consumerRecord.partition());
					System.out.println("offset -----------:" + consumerRecord.offset());
					System.out.println("key --------------:" + consumerRecord.key());
					System.out.println("value ------------:" + consumerRecord.value());
					System.out.println("============================================================================");
//					if(consumerRecord.offset() == endOffset){
//						Thread.currentThread().sleep(1000L * 3600);
//					}
					if(consumerRecord.offset() % 10 == 0){
						Thread.currentThread().sleep(10000L);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}
	
	public static void main(String[] args) {
		/**
		 * 首先测试自动提交offset的情况
		 * config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); 		//显示设置偏移量自动提交
		 * config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000); //设置偏移量提交时间间隔
		 */
		String topic = "GuPiaoHangQing";
		consumeZiDongCommit(topic);

		/**
		 * 测试手动提交中的同步提交
		 * config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);		//设置手动提交偏移量
		 * config.put (ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 1024); 		//为了便于测试,这里设置一次 fetch 请求取得的数据最大值为 lKB,默认是 5MB
		 */
//		String topic = "FirstTopic";
//		consumeTongBuCommit(topic);

		/**
		 * 测试手动提交中的异步提交
		 * config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);		//设置手动提交偏移量
		 * config.put (ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 1024); 		//为了便于测试,这里设置一次 fetch 请求取得的数据最大值为 lKB,默认是 5MB
		 */
//		String topic = "FirstTopic";
//		consumeYiBuCommit(topic);

		/**
		 * 测试消费指定分区的
		 */
//		String topic = "GuPiaoHangQing";
//		int partition = 2;
//		long time = System.currentTimeMillis() - 48 * 3600 * 1000;
//		consumeRecordWithTime(topic, partition, time);

		/**
		 * 测试消费指定分区的,指定偏移量的消息
		 */
//		String topic = "FirstTopic";
//		int partition = 0;
//		long startOffset = 0;
//		long endOffset = 5;
//		consumeStartOffsetAndEndOffsetRecord(topic, partition, startOffset, endOffset);

	}

}
