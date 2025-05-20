package com.deepblue.inaction_01_kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.Random;


/**
 * 单线程的股票生产者
 * @author YangJianWei
 */
public class GuPiaoProducer {
	
	private static Logger LOG = Logger.getLogger(GuPiaoProducer.class);
	private static String BROKER_LIST = "192.168.188.70:9092, 192.168.188.71:9092, 192.168.188.72:9092";
	private static String TOPIC = "GuPiaoHangQing";
//	private static String TOPIC = "first-topic";
	private static int MSG_SIZE = 20;
	
	private static Properties producerConfig = new Properties();
	private static KafkaProducer<String, String> kafkaProducer = null;
	
	
	static {
		//服务器节点的匹配
		producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
		//key序列化规则器
		producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//value序列化规则器
		producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//在生产者中使用自定义的分区器!
		//producerConfig.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.inspur.partition.MyPartitioner");

		kafkaProducer = new KafkaProducer<String, String>(producerConfig);
	}
	
	/**
	 * 创建股票信息
	 * @return
	 */
	private static GuPiao createGuPiaoInfo(){
		GuPiao gupiao = new GuPiao();
		Random random = new Random();
		int price = random.nextInt(100) + 1;

		Long currentTime = System.currentTimeMillis();
		gupiao.setCurrentTime(currentTime);
		gupiao.setGuPiaoName("深蓝集团");
		gupiao.setGuPiaoNickName("CDBT-" + currentTime);
		gupiao.setGuPiaoPrice(price);
		gupiao.setGuPiaoDesc("深蓝集团股票");
		
		return gupiao;
	}

	public static void sendMessage() {
		ProducerRecord<String, String> record = null;
		GuPiao gupiao = null;
		int num = 1;
		try {
			for(int i = 0; i < MSG_SIZE; i++){
				gupiao = createGuPiaoInfo();
				/**
				 * 设置不同的key 测试 分区管理器是否进行不同的路由存放
				 */
				record = new ProducerRecord<String, String>(TOPIC, null, gupiao.getCurrentTime(),
						 gupiao.getGuPiaoNickName(), gupiao.toString());
//				record = new ProducerRecord<String, String>(TOPIC, null, gupiao.getCurrentTime(),
//						 null, gupiao.toString());
				kafkaProducer.send(record);//发送并遗忘!
				if(num++ % 100 == 0	){
					Thread.currentThread().sleep(1000L);		//每100条消息记录线程休息1s
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			kafkaProducer.close();
		}
	}
	
	/**
	 * 有回调的生产者进行产生消息,发送消息是可以记录日志,或打印日志之类的操作!
	 */
	public static void sendMessageWithCallBack(){
		ProducerRecord<String, String> record = null;
		GuPiao gupiao = null;
		int num = 1;
		try {
			for(int i = 0; i < MSG_SIZE; i++){
				gupiao = createGuPiaoInfo();
				record = new ProducerRecord<String, String>(TOPIC, null, gupiao.getCurrentTime(), 
						 gupiao.getGuPiaoNickName(), gupiao.toString());
				kafkaProducer.send(record, new Callback() {
					@Override
					public void onCompletion(RecordMetadata metadata, Exception exception) {
						if(exception != null){
							System.out.println("exception -------------->:" + exception.getMessage());
							LOG.error(exception.getMessage());
						}
						if(metadata != null){
							System.out.println("topic ---:" + metadata.topic() + ",partition ---:" + metadata.partition() + ",offset ---:" + metadata.offset());
						}
					}
				});
				if(num++ % 2 == 0){
					Thread.currentThread().sleep(500L);
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			kafkaProducer.close();
		}
	}
	

	public static void main(String[] args) {
		sendMessage();
//		sendMessageWithCallBack();
	}
}
