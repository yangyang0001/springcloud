package com.deepblue.inaction_01_kafka.consumer.lower;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import org.apache.log4j.Logger;


/**
 * 低级别的消费者
 * @author YangJianWei
 * 要点:
 * 		1.在每次获取消息之前,都要获取一下消息的Offset值,以便下一次进行消费!
 */
public class KafkaSimpleConsumer {
	
	private static Logger LOG = Logger.getLogger(KafkaSimpleConsumer.class);
	
	/**
	 * 指定 Kafka 集群代理列表 ， 列表无需指定所有的代理地址 ，
	 * 只要保证能连上 Kafka 集群即可， 一般建议多个节点时至少写两个节点的地址
	 */
	private static String BROKER_LIST = "192.168.188.35:9092, 192.168.188.35:9092, 192.168.188.35:9092";
	
	/**连接超时时间设置为 1 分钟 */
	private static final int TIME_OUT = 60 * 1000;
	
	/**设置读取消息缓冲区大小为1M*/
	private static final int BUFFER_SIZE = 1024 * 1024;
	
	/**设置每次获取消息的条数 */
	private static final int FETCH_SIZE = 100000;
	
	/** broker 的端口*/
	private static final int PORT = 9092;
	
	/**设置容忍发生错误时重试的最大次数 */
	private static final int MAX_ERROR_NUM = 3;
	
	
	/**
	 * 获取主题对应的分区中的源数据信息
	 * --------------------->获取分区元数据方法的具体实现代码
	 * 获取主题指定分区下的元数据信息!
	 * 
	 * @param brokerListString	代理列表
	 * @param port				端口号
	 * @param topic				主题
	 * @param partitionid		分区
	 * @return
	 */
	public static PartitionMetadata queryTopicZhiDingPartitionInfo(String brokerListString, int port, String topic, int partitionid){
		SimpleConsumer consumer = null;
		TopicMetadataRequest topicMetadataRequest = null;
		TopicMetadataResponse topicMetadataResponse = null;
		List<TopicMetadata> metadataList = null;
		
		String[] brokerList = brokerListString.split(",");
		System.out.println(brokerListString);
		try {
			for(int i = 0; i < brokerList.length; i++){
				String broker = brokerList[i];
				String brokerIP = broker.split(":")[0];
				int brokerHOST = Integer.valueOf(broker.split(":")[1]);
				System.out.println("broker ----------:" + broker + ",brokerIP ----------:" + brokerIP + ",brokerHOST -----------:" + brokerHOST);
				
				//TODO
				//1.首先构建一个读取元数据的消费者
				consumer = new SimpleConsumer(brokerIP, brokerHOST, TIME_OUT, BUFFER_SIZE, topic + "-" + partitionid + "-" + i);
				//2.创建获取主题元数据的请求
				topicMetadataRequest = new TopicMetadataRequest(Arrays.asList(topic));
				//3.接收主题相关的元数据的返回
				topicMetadataResponse = consumer.send(topicMetadataRequest);
				//4.获取主题相关的元数据信息列表
				metadataList = topicMetadataResponse.topicsMetadata();
				//5.获取指定分区下主题的元数据信息!
				for(TopicMetadata topicMetadata : metadataList){
					for(PartitionMetadata partitionMetadata : topicMetadata.partitionsMetadata()){
						if(partitionMetadata.partitionId() != partitionid){
							continue;
						} else {
							return partitionMetadata;
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {	
			if(consumer != null){
				consumer.close();
			}
		}
		return null;
	}
	
	/**
	 * 该方法用beginTime确定    是起始FirstOffset还是LastOffset
	 * @param consumer
	 * @param topic
	 * @param partition
	 * @param beginTime
	 * @param clientName
	 * @return
	 */
	public static long getLastOffset(SimpleConsumer consumer, String topic, int partition, long beginTime, String clientName){
		
		/**
		 * 构造offset请求前主备的参数!
		 */
		TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
		Map<TopicAndPartition, PartitionOffsetRequestInfo> offsetRequestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		offsetRequestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(beginTime, 1));
		
		OffsetRequest offsetRequest = new OffsetRequest(offsetRequestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
		OffsetResponse offsetResponse = consumer.getOffsetsBefore(offsetRequest);
		
		if (offsetResponse.hasError()) {
			LOG.error("getLastOffset occurs exception :" + offsetResponse.errorCode(topic, partition));
			return -1 ;
		}
		
		long [] offsets= offsetResponse.offsets(topic, partition);
		
		if (null == offsets || offsets.length == 0) {
			LOG.error("getLastOffset occurs error,offses is null.");
			return -1;
		}
		
		return offsets[0];
	}
	
	/**
	 * 消费主题分区中的消息!
	 * @param brokerListString
	 * @param port
	 * @param topic
	 * @param partitionId
	 */
	public static void consume(String brokerListString, int port, String topic, int partitionId){
		SimpleConsumer consumer = null ;
		try {
			//1.首先获取指定分区的元数据信息
			PartitionMetadata metadata = queryTopicZhiDingPartitionInfo(brokerListString , port, topic, partitionId ) ;
			if (metadata == null) {
				LOG.error("Can’t find metadata info.");
				return ;
			}
			if (metadata.leader() == null) {
				LOG.error ("Can’t find the partition:" + partitionId + "’s leader.") ;
				return ;
			}
			String leadBroker = metadata.leader().host();
			String clientId ="client-" + topic + "-" + partitionId;
			
			//TODO
			//2.创建一个消息者作为消费消息的真正执行者
			consumer = new SimpleConsumer(leadBroker, port , TIME_OUT, BUFFER_SIZE, clientId);
			//设置时间为 kafka.api.OffsetRequest.EarliestTime ()从最新消息起始处开始
			long lastOffset = getLastOffset(consumer, topic, partitionId, kafka.api.OffsetRequest.LatestTime(), clientId);
			System.out.println("lastOffset ----------------:" + lastOffset);
			int errorNum = 0;
			FetchRequest fetchRequest = null;
			FetchResponse fetchResponse = null;
			while (lastOffset >= 0) {
				if (consumer == null) {// 当在循环过程中若出错时会将起始实例化的consumer关闭掉并设置为null
					consumer = new SimpleConsumer(leadBroker, port, TIME_OUT, BUFFER_SIZE, clientId);
				}
				// 3.构造获取消息的request
				fetchRequest = new FetchRequestBuilder().clientId(clientId)
									.addFetch(topic, partitionId, lastOffset, FETCH_SIZE)
									.build();
				// 4.获取响应并处理
				fetchResponse = consumer.fetch(fetchRequest);
				if (fetchResponse.hasError()) {// 若发生错误
					errorNum++;
					if (errorNum > MAX_ERROR_NUM) {// 达到发生错误的最大次数时退出循环
						break;
					}
					// 获取错误码
					short errorCode = fetchResponse.errorCode(topic,
							partitionId);
					if (ErrorMapping.OffsetOutOfRangeCode() == errorCode) {
						// offset已无效,因为在获取lastOffset时设置为从最早开始时间,若是这种错误码我们再将时间设置为从LatestTime()开始查找
						lastOffset = getLastOffset(consumer, topic, partitionId, kafka.api.OffsetRequest.LatestTime(), clientId);
						continue;
					} else if (ErrorMapping.OffsetsLoadInProgressCode() == errorCode) {
						Thread.sleep(30000);// 若是这种异常则让线程阻塞30秒
						continue;
					} else {// 这里只是简单的关闭当前分区Leader事例化的Consumer，并没有对Leader失效时做相应处理
						consumer.close();
						consumer = null;
						continue;
					}
				} else {
					errorNum = 0;//错误次数清零
					long fetchNum = 0;
					for (MessageAndOffset messageAndOffset : fetchResponse
							.messageSet(topic, partitionId)) {
						long currentOffset = messageAndOffset.offset();
						if (currentOffset < lastOffset) {
							LOG.error("Fetch an old offset: " + currentOffset + "expect the offset is greater than " + lastOffset);
							continue;
						}
						lastOffset = messageAndOffset.nextOffset();
						ByteBuffer payload = messageAndOffset.message().payload();
						
						byte[] bytes = new byte[payload.limit()];
						payload.get(bytes);
						// 简单打印出消息及消息offset
						System.out.println("offset:" + messageAndOffset.offset() + ",message:" + (new String(bytes, "UTF-8")) );
						fetchNum++;
					}
					
					if (fetchNum == 0) {// 还没有消息则让线程阻塞几秒
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					}
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != consumer) {
				consumer.close();
			}
		}
	}
	
	
	public static void main(String[] args) {
		
		/*KafkaSimpleConsumer consumer = new KafkaSimpleConsumer();
		PartitionMetadata partitionMetadata = queryTopicZhiDingPartitionInfo(BROKER_LIST, PORT, "GuPiaoHangQing", 0);
		System.out.println(partitionMetadata);*/
		
		//
		String topic = "GuPiaoHangQing";
		int partitionId = 0;
		consume(BROKER_LIST, PORT, topic, partitionId);
		
	}
	
}
