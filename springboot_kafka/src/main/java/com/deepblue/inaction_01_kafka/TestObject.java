package com.deepblue.inaction_01_kafka;

/**
 * User: YANG
 * Date: 2019/6/18-11:03
 * Description: No Description
 */
public class TestObject {

	public static void main(String[] args){

		/**
		 * partition 路由规则:
		 *
		 * 如果key 为空则随机分配到分区中!
		 * 如果key 不为空
		 *      计算通过key 到哪个分区中的算法:
		 *      key.hashCode() % partition数量
		 */
		System.out.println("CDBT1".hashCode() % 3);

		/**
		 * group对应的 offset 偏移量的路由规则:
		 *      group_id.hashCode() % 50来计算
		 * 配合命令:
		 *      sh kafka-run-class.sh kafka.tools.DumpLogSegments --files /tmp/kafka-logs/__consumer_offsets-10/00000000000000000000.log --print-data-log
		 *      使用就OK了
		 */
		System.out.println("AAAA".hashCode() % 50);
		System.out.println("test_consumer_group".hashCode() % 50);
		System.out.println("test".hashCode() % 50);




	}
}
