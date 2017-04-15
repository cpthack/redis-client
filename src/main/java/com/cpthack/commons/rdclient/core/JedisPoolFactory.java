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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.cpthack.commons.rdclient.config.RedisConfig;

/**
 * 
 * <b>JedisPoolFactory.java</b></br>
 * 
 * <pre>
 * TODO(这里用一句话描述这个类的作用)
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月15日 下午12:04:35
 * @since JDK 1.7
 */
public class JedisPoolFactory {
	private static Logger                 logger        = LoggerFactory
	                                                            .getLogger(JedisPoolFactory.class);
	private static RedisConfig            configDefault = new RedisConfig();
	private static Map<String, JedisPool> mapConfigPool = new ConcurrentHashMap<String, JedisPool>();
	
	// 禁止实例化
	private JedisPoolFactory() {
	}
	
	private static JedisPool getConfigPool(String key) {
		JedisPool pool = mapConfigPool.get(key);
		return pool;
	}
	
	private synchronized static boolean initConfigPool(RedisConfig config) {
		if (config == null) {
			return false;
		}
		String key = config.getConfigFile();
		JedisPool jedisPool = getConfigPool(key);
		if (jedisPool != null) {
			return true;
		}
		String server = config.getServerIp();
		int port = config.getServerPort();
		String auth = config.getServerAuth();
		boolean isAuth = false;
		if (StringUtils.isNotBlank(auth)) {
			isAuth = true;
		}
		logger.warn("The redis server:" + server + ":" + port);
		if (StringUtils.isBlank(server)) {
			return false;
		}
		// 池基本配置
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(config.getPoolMax());
		poolConfig.setMaxIdle(config.getPoolIdle());
		poolConfig.setMaxWaitMillis(config.getTryTimeout());
		poolConfig.setTestOnBorrow(config.getTestOnBorrow());
		int timeout = config.getTryTimeout();// msec
		
		if (isAuth) {
			jedisPool = new JedisPool(poolConfig, server, port, timeout, auth);
		}
		else {
			jedisPool = new JedisPool(poolConfig, server, port, timeout);
		}
		if (jedisPool != null) {
			mapConfigPool.put(key, jedisPool);
		}
		return jedisPool != null;
	}
	
	// 获取默认的Redis连接
	public static Jedis getClient() {
		return getClient(configDefault);
	}
	
	// 获取指定配置的Redis连接
	public static Jedis getClient(RedisConfig config) {
		if (config == null) {
			return null;
		}
		String key = config.getConfigFile();
		JedisPool jedisPool = getConfigPool(key);
		if (jedisPool == null) {
			initConfigPool(config);
			jedisPool = getConfigPool(key);
		}
		if (jedisPool == null) {
			return null;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		}
		catch (Exception e) {
			logger.warn("获取指定配置的redis连接失败，Error:", e);
		}
		if (jedis == null) {
			logger.warn("Cannot get Jedis object from the pool!");
		}
		return jedis;
	}
}
