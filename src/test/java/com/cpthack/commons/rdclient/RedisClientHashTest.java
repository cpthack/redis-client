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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpthack.commons.rdclient.core.RedisClient;
import com.cpthack.commons.rdclient.core.RedisClientFactory;

/**
 * <b>RedisClientHashTest.java</b></br>
 * 
 * <pre>
 * TODO(这里用一句话描述这个类的作用)
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月16日 上午11:36:44
 * @since JDK 1.7
 */
public class RedisClientHashTest {
	
	private static Logger logger = LoggerFactory.getLogger(RedisClientHashTest.class);
	
	public static void main(String[] args) {
		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		String key = "root-key";
		Map<String, String> hashMap = new HashMap<String, String>() {
			{
				put("abc", "abc");
			}
		};
		int expiredSeconds = 5 * 60;// 5分钟
		redisClient.hmset(key, hashMap, expiredSeconds);
		logger.info("设置HASH值hashMap：KEY = [" + key + "],SIZE = [" + hashMap.size() + "]");
		
		String hashKey = "hash-key";
		String hashValue = "hash-value";
		redisClient.hset(key, hashKey, hashValue);
		
		logger.info("获取KEY = [" + hashKey + "]的值 , VALUE = [" + redisClient.hget(key, hashKey) + "]");
		
		Map<String, String> resultMap = redisClient.hgetAll(key);
		for (String resultKey : resultMap.keySet()) {
			logger.info("遍历resultMap,KEY = [" + resultKey + "],VALUE = [" + resultMap.get(resultKey) + "]");
		}
		
	}
}
