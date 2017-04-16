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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpthack.commons.rdclient.core.RedisClient;
import com.cpthack.commons.rdclient.core.RedisClientFactory;

/**
 * <b>RedisClientNxTest.java</b></br>
 * 
 * <pre>
 * setnx、hsetnx...等操作测试类
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月16日 上午11:15:08
 * @since JDK 1.7
 */
public class RedisClientNxTest {
	private static Logger logger = LoggerFactory.getLogger(RedisClientNxTest.class);
	
	public static void main(String[] args) {
		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		String key = "setnx-key";
		String value = "setnx-value";
		int expireSeconds = 5 * 60;// 5分钟
		long result = redisClient.setnx(key, value, expireSeconds);
		logger.info("[setnx]第一次执行结果为：" + result);
		result = redisClient.setnx(key, value, expireSeconds);
		logger.info("[setnx]第二次执行结果为：" + result);
		result = redisClient.setnx(key, value, expireSeconds);
		logger.info("[setnx]第三次执行结果为：" + result);
		
//		redisClient.deleteByPattern(key);
//		logger.info("删除KEY= [" + key + "]");
		
	}
	
}
