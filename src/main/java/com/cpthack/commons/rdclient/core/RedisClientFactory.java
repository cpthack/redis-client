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
package com.cpthack.commons.rdclient.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpthack.commons.rdclient.config.RedisConfig;
import com.cpthack.commons.rdclient.constants.RedisConstants;

/**
 * <b>RedisClientFactory.java</b></br>
 * 
 * <pre>
 * TODO(这里用一句话描述这个类的作用)
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月15日 下午9:56:31
 * @since JDK 1.7
 */
public class RedisClientFactory {
	private static Logger                   logger         = LoggerFactory.getLogger(RedisClientFactory.class);
	private static Map<String, RedisClient> redisClientMap = new ConcurrentHashMap<String, RedisClient>();
	
	/**
	 * 
	 * <b>获取RedisClient对象</b> <br/>
	 * <br/>
	 * 
	 * 获取默认RedisConfig配置的RedisClient对象<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @return RedisClient
	 *
	 */
	public static RedisClient getClient() {
		return getClient(null);
	}
	
	public static RedisClient getClient(RedisConfig redisConfig) {
		RedisClient redisClient = null;
		String redisConfigFileName = RedisConstants.DEFALUT_REDIS_FILE_NAME;
		
		// 获取默认RedisConfig配置的RedisClient对象，并缓存到redisClientMap对象中
		if (null == redisConfig) {
			redisClient = redisClientMap.get(redisConfigFileName);
			if (redisClient == null) {
				redisClient = new JedisRedisClient().setRedisConfig(null);
				redisClientMap.put(redisConfigFileName, redisClient);
			}
			return redisClient;
		}
		
		// 获取自定义的RedisConfig配置的RedisClient对象，并缓存到redisClientMap对象中
		redisConfigFileName = redisConfig.getConfigFile();
		redisClient = redisClientMap.get(redisConfigFileName);
		if (redisClient == null) {
			redisClient = new JedisRedisClient().setRedisConfig(redisConfig);
			redisClientMap.put(redisConfigFileName, redisClient);
		}
		return redisClient;
	}
}
