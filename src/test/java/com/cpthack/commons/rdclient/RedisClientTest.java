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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.cpthack.commons.rdclient.core.JedisRedisClient;
import com.cpthack.commons.rdclient.core.RedisClient;

/**
 * <b>RedisClientTest.java</b></br>
 * 
 * <pre>
 * TODO(这里用一句话描述这个类的作用)
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月14日 下午5:57:40
 * @since JDK 1.7
 */
public class RedisClientTest {
	private static Logger logger = LoggerFactory.getLogger(RedisClientTest.class);
	
	public static void main(String[] args) {
		
		logger.info("生成默认RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = new JedisRedisClient().setRedisConfig(null);
		
		logger.info("获取到Jedis对象，方便developers自定义扩展redis的其他操作");
		Jedis jedis = (Jedis) redisClient.getJedis();
		jedis.close();
		
		logger.info("Redis Set 字符串英文值");
		redisClient.set("test1", "my name is cpthack .");
		
		logger.info("Redis Set 字符串中文值");
		redisClient.set("test2", "我叫成佩涛 .");
		
		logger.info("获取 KEY = [test] 的值 VALUE = [" + redisClient.get("test") + "]");
		
		Set<String> keySet = redisClient.keys("t*");
		for (String key : keySet) {
			logger.info("遍历redis中的key，目前查询到有>>>>>KEY = [" + key + "]");
		}
		
	}
	
}
