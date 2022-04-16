package com.deepblue.inaction_01_kafka.topic;

import kafka.admin.AdminUtils;
import kafka.admin.BrokerMetadata;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 主题信息属于kafka中的元数据信息,这些信息都是有zookeeper管理的
 * @author YangJianWei
 */
public class TopicTest {
	
	private static final String ZK_CONNECT = "192.168.188.15:2181,192.168.188.16:2181,192.168.188.17:2181,192.168.188.18:2181";
	private static final int SESSION_TIMEOUT = 30000;
	private static final int CONNECT_TIMEOUT = 30000;
//	private static final String TOPIC = "GuPiaoHangQing";
	private static final String TOPIC = "topic-api-test";

	
	
	//TODO main方法
		public static void main(String[] args) {
			
			//1.创建主题
			String topic = "GuPiaoHangQing";
			int partition = 3;
			int replica = 3;
			createTopic(topic, partition, replica, AdminUtils.createTopic$default$5());

			//7.删除主题 TODO
//			String topic = "GuPiaoHangQing";
//			deleteTopic(topic);

			//2.查询主题配置信息
//			String topic = "";
//			Properties topicProperties = queryTopicInfo(topic);
//			for(Map.Entry<Object, Object> entry : topicProperties.entrySet()) {
//				System.out.println("key --------:" + entry.getKey() + ";value ------:" + entry.getValue());
//			}
			
			
			//3.查询所有主题配置信息
//			Map<String, Properties> topicInfoMap = queryAllTopicInfo();
//			for(Map.Entry<String, Properties> topicEntry : topicInfoMap.entrySet()){
//				System.out.println("topic ----------------------------------------------------------:" + topicEntry.getKey());
//				Properties properties = topicEntry.getValue();
//				for(Map.Entry<Object, Object> entry : properties.entrySet()){
//					System.out.println("topic_info------> key-----:" + entry.getKey() + ",value-----:" + entry.getValue());
//				}
//			}
			
			
			//4.修改主题级别的配置
//			String topic = "";
//			Properties topicProperties = new Properties();
//			topicProperties.put("flush.messages", "1");
//			topicProperties.put("flush.ms", "1000");
//			modifyTopicConfig(topic, topicProperties, null);
			
			
			//5.添加分区
//			String topic = "AAAA";
//			int partition = 3;
//			String replicaAssignment = "";
//			addPartition(topic, partition, replicaAssignment);
			
			
			//6.添加副本
//			String topic = "AAAA";
//			int partition = 3;
//			int replicaCount = 2;
//			addReplica(topic, partition, replicaCount);
			
			//8.删除所有除了特殊主题consumer_offsets之外的所有主题
//			Map<String, Properties> topicInfoMap = queryAllTopicInfo();
//			for(Map.Entry<String, Properties> topicEntry : topicInfoMap.entrySet()){
//				if(StringUtils.isNotBlank(topicEntry.getKey()) && !topicEntry.getKey().contains("consumer_offsets")){
//					deleteTopic(topicEntry.getKey());
//				}
//			}
			
			
			
		}
	
	
	/**
	 * 查询主题信息
	 * @param topic
	 * @return
	 */
	public static Properties queryTopicInfo(String topic){
		ZkUtils zkUtils = null;
		try {
			zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
			Properties properties = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);
			return properties;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zkUtils.close();
		}
		return null;
	}
	
	
	/**
	 * 经确认是存在一个比较特殊的topic,同样通过8089端口也可以看出来有一个特殊的topic;这个topic为:__consumer_offsets
	 * 查询出所有的topic的信息
	 */
	public static Map<String, Properties> queryAllTopicInfo() {
		ZkUtils zkUtils = null;
		Map<String, Properties> topicInfoMap = new HashMap<String, Properties>();
		try {
			zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
			scala.collection.Map<String, Properties> map = AdminUtils.fetchAllTopicConfigs(zkUtils);
			scala.collection.Iterator<String> iterator = map.keysIterator();
			while (iterator.hasNext()){
				String topic = iterator.next();
				/**
				System.out.println("topic ---------------------------------------------------:" + topic);
				scala语言获取主题信息
				scala.Option<Properties> propertiesOption = map.get(topic);
				scala.collection.immutable.List<Properties> propertiesList = propertiesOption.toList();
				for(int i = 0; i < propertiesList.length(); i++){
					Properties properties = propertiesList.apply(i);
					Set<Object> keySet = properties.keySet();
					for(Object keyObject : keySet){
						Object valueObject = properties.get(keyObject);
						System.out.println("key -------:" + keyObject + ",value ------:" + valueObject);
					}
				}*/
				Properties topicProperties = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);
				topicInfoMap.put(topic, topicProperties);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zkUtils.close();
		}
		return topicInfoMap;
	}
	
	
	/**
	 * 修改主题的的相关配置
	 * @param topic			主题
	 * @param properties	配置参数
	 */
	public static void modifyTopicConfig(String topic, Properties properties, String modifyFlag){
		ZkUtils zkUtils = null;
		try {
			zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
			//获取要修改topic的相关配置
			Properties currentProperties = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);
			
			//将传入的配置properties添加到要修改的主题配置中!
			if("DELETE".equals(modifyFlag)){
				for(Map.Entry<Object, Object> entry : properties.entrySet()){
					currentProperties.remove(entry.getKey());
				}
			} else {
				currentProperties.putAll(properties);
			}
			
			//执行修改主题配置
			AdminUtils.changeTopicConfig(zkUtils, topic, currentProperties);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zkUtils.close();
		}
	}
	
	
	/**
	 * 创建主题
	 * @param topic			主题名称
	 * @param partition		分区个数-0,-1表示
	 * @param replica		分区副本总个数(Leader副本,Follow副本)
	 * @param properties	主题配置参数
	 */
	public static void createTopic(String topic, int partition, int replica, Properties properties){
		ZkUtils zkUtils = null;
		try {
			//初始化Zookeeper各种参数
			zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT , CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
			//如果topic在zookeeper中不存在就创建topic
			if(!AdminUtils.topicExists(zkUtils, topic)){
				AdminUtils.createTopic(zkUtils, topic, partition, replica, properties, AdminUtils.createTopic$default$6());
			} else {
				//相应的topic已经存在
				System.out.println(topic + "已经存在,无需重新创建!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zkUtils.close();
		}
	}
	
	
	/**
	 * 删除主题
	 * @param topic
	 */
	public static void deleteTopic(String topic){
		ZkUtils zkUtils = null;
		try {
			zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
			AdminUtils.deleteTopic(zkUtils, topic);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zkUtils.close();
		}
	}
	
	
	/**
	 * 添加分区
	 * @param topic				主题名称
	 * @param partition			分区总数 	----->只能增加不能减少!
	 * @param replicaAssignment	逗号分隔分区,冒号分隔brokerId	同时需要注意的是，副本分配方案要包括己有分区的副本分配信息，根据分配顺序从左到右依次与分区对应，分区编号递增。
	 * 
	 */
	public static void addPartition(String topic, int partition, String replicaAssignment){
		ZkUtils zkUtils = null;
		try {
			zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
			AdminUtils.addPartitions(zkUtils, topic, partition, replicaAssignment, true, AdminUtils.addPartitions$default$6());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zkUtils.close();
		}
	}
	
	
	/**
	 * 添加主题 的 分区副本
	 * @param topic			主题名称
	 * @param partition		分区总数
	 * @param replicaCount
	 */
	public static void addReplica(String topic, int partition, int replicaCount){
		ZkUtils zkUtils = null;
		try {
			//申请zookeeper
			zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
			//获取原有的代理信息
			scala.collection.Seq<BrokerMetadata> brokerMetadatas = AdminUtils.getBrokerMetadatas(zkUtils, AdminUtils.getBrokerMetadatas$default$2(), AdminUtils.getBrokerMetadatas$default$3());
			//配置新的分区副本信息
			scala.collection.Map<Object, scala.collection.Seq<Object>> map = AdminUtils.assignReplicasToBrokers(brokerMetadatas, partition, replicaCount, AdminUtils.assignReplicasToBrokers$default$4(), AdminUtils.assignReplicasToBrokers$default$5());
			AdminUtils.createOrUpdateTopicPartitionAssignmentPathInZK(zkUtils, topic, map, null, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zkUtils.close();
		}
	}
	

}
 