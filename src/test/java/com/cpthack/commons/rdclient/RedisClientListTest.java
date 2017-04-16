/**
 * Copyright (c) 2013-2020, cpthack 成佩涛 (cpt@jianzhimao.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpthack.commons.rdclient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpthack.commons.rdclient.core.RedisClient;
import com.cpthack.commons.rdclient.core.RedisClientFactory;
import com.cpthack.commons.rdclient.queue.IAtom;

/**
 * <b>RedisClientListTest.java</b></br>
 * 
 * <pre>
 * TODO(这里用一句话描述这个类的作用)
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月16日 下午12:47:27
 * @since JDK 1.7
 */
public class RedisClientListTest {
	
	private static Logger logger = LoggerFactory.getLogger(RedisClientListTest.class);
	
	public static void main(String[] args) {
		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		String key = "list-key";
		String value = "list-value";
		int expireSeconds = 5 * 60;
		
		redisClient.deleteByPattern("*");// 清除所有数据
		for (int i = 0; i < 50; i++) {
			redisClient.lpush(key, expireSeconds, value + i);
		}
		
		// logger.info("从链表头部开始取出其中一个数据,VALUE = ["+redisClient.lpop(key)+"]");
		
		// logger.info("从链表尾部开始取出其中一个数据,VALUE = ["+redisClient.rpop(key)+"]");
		
		logger.info("以保证redis List取值的事务性，从队列中取出数据并消费，如果消费失败则撤回取数据操作");
		for (int i = 1; i < 51; i++) {
			logger.info("第[" + i + "]次取队列 KEY=[" + key + "]中的数据");
			redisClient.popQueue(key, new IAtom() {
				@Override
				public boolean run(String message) {
					logger.info("取出队列信息,VALUE = [" + message + "]");
					return true;
				}
			});
		}
		
		List<String> resultList = redisClient.lrange(key, 0, 5);
		for (String resultValue : resultList) {
			logger.info("获取List的值,VALUE = [" + resultValue + "]");
		}
	}
}
