package com.deepblue.inaction_01_kafka.producer.thread;

import com.deepblue.inaction_01_kafka.producer.GuPiao;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KafkaProducerThreadTest {
	
	private static Properties configs = new Properties();
	private static KafkaProducer<String, String> kafkaProducer = null;
	private static String BROKER_LIST = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094";
	private static int THREAD_NUM = 20;
	private static int MSG_SIZE = 50;
	private static String TOPIC = "GuPiaoHangQing";
	
	static {
		//服务器节点的匹配
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
		//key序列化规则器
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//value序列化规则器
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//在生产者中使用自定义的分区器!
		//configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.inspur.partition.MyPartitioner");
		
		kafkaProducer = new KafkaProducer<String, String>(configs);
	}
	
	private static GuPiao createGuPiaoInfo(){
		GuPiao gupiao = new GuPiao();
		Random random = new Random();
		int price = random.nextInt(100) + 1;
		
		gupiao.setCurrentTime(System.currentTimeMillis());
		gupiao.setGuPiaoName("深蓝集团");
		gupiao.setGuPiaoNickName("CDBT");
		gupiao.setGuPiaoPrice(price);
		gupiao.setGuPiaoDesc("深蓝集团股票");
		
		return gupiao;
	}
	
	public static void main(String[] args) {
		/**
		 * corePoolSize：核心池的大小
		 * maximumPoolSize：线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程
		 */
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 200, TimeUnit.MILLISECONDS,
                					  new LinkedBlockingQueue<Runnable>(THREAD_NUM));
		GuPiao gupiao = null;
		ProducerRecord<String, String> producerRecord = null;
		KafkaProducerThread producerThread = null;
		try {
			for(int i= 0; i < MSG_SIZE; i++){
				gupiao = createGuPiaoInfo();
				producerRecord = new ProducerRecord<String, String>(TOPIC, null, gupiao.getCurrentTime(), gupiao.getGuPiaoNickName(), gupiao.toString());
				producerThread = new KafkaProducerThread(kafkaProducer, producerRecord);
				executor.submit(producerThread);
				if(i % 50 == 0){
					Thread.currentThread().sleep(100L);
					System.gc();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			kafkaProducer.close();
			executor.shutdown();
		}
	}

}
