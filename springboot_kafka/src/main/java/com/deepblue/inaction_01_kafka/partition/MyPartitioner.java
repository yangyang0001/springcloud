package com.deepblue.inaction_01_kafka.partition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * 自定义分区器
 * @author YangJianWei
 */
public class MyPartitioner implements Partitioner{

	@Override
	public void configure(Map<String, ?> configs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes,
			Object value, byte[] valueBytes, Cluster cluster) {
		
		/**
		 * 获取当前topic的所有分区的信息集合
		 */
		List<PartitionInfo> partitionInfoList = cluster.partitionsForTopic(topic);
		
		/**
		 * 当前topic的分区有多少个
		 */
		int partitionsNum = partitionInfoList.size();
		
		if ((keyBytes == null) || (!(key instanceof String))){
			throw new InvalidRecordException("We expect all messages to have customer name as key");
		}
		
		/**
		 * 处理Key为指定值(这里为my_country)的record被发送到最后一个分区上!
		 */
		if (((String) key).equals("cccc")) {
			System.out.println("topic ----------------------------:" + topic);
			System.out.println("key ------------------------------:" + key);
			System.out.println("value ----------------------------:" + value);
			System.out.println("definition_partition-----------------------------------------------------:" + partitionsNum);
			return partitionsNum; // Banana will always go to last partition
		} else {
			/**
			 * 其他记录被散列到除了最后一个分区之外的分区!
			 */
			System.out.println("topic ----------------------------:" + topic);
			System.out.println("key ------------------------------:" + key);
			System.out.println("value ----------------------------:" + value);
			System.out.println("other_partition----------------------------------------------------------:" + (Math.abs(Utils.murmur2(keyBytes)) % (partitionsNum - 1)));
			return (Math.abs(Utils.murmur2(keyBytes)) % (partitionsNum - 1));
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}
