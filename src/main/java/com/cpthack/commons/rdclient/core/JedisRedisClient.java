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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.cpthack.commons.rdclient.config.RedisConfig;
import com.cpthack.commons.rdclient.exception.AssertHelper;
import com.cpthack.commons.rdclient.exception.RedisClientException;

/**
 * <b>JedisRedisClient.java</b></br> TODO(这里用一句话描述这个类的作用)</br>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月12日 下午2:50:39
 * @since JDK 1.7
 */
public class JedisRedisClient implements RedisClient<Jedis> {

	private static Logger logger = LoggerFactory
			.getLogger(JedisRedisClient.class);
	private RedisConfig redisConfig = null;

	@Override
	public RedisClient<Jedis> setRedisConfig(RedisConfig redisConfig) {
		this.redisConfig = redisConfig;
		return this;
	}

	@Override
	public Jedis getJedis() {
		if (null == redisConfig) {
			return JedisPoolFactory.getClient();
		}
		return JedisPoolFactory.getClient(redisConfig);
	}
	
	
	@Override
	public Set<String> keys(String pattern) {
		AssertHelper.notBlank(pattern, "The patternKey is Not allow blank.");
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		Set<String> set = null;
		try {
			set = jedis.keys(pattern);
		} catch (Exception e) {
			logger.warn("Read redis in error:" + e);
			throw new RedisClientException(e);
		} finally {
			release(jedis);
		}
		return set;
	}

	@Override
	public Long deleteByPattern(String pattern) {
		Long count = 0L;
		AssertHelper.notBlank(pattern, "The patternKey is Not allow blank.");
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		try {
			Set<String> keys = jedis.keys(pattern);
			if (keys != null) {
				for (String k : keys) {
					count += jedis.del(k);
				}
			}
		} catch (Exception e) {
			logger.warn("Delete redis pattern keys in error:" + e);
			throw new RedisClientException(e);
		} finally {
			release(jedis);
		}
		return count;
	}
	
	
	@Override
	public boolean set(String key, String value) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		try {
			key = jedis.set(key, value);
			return "OK".equalsIgnoreCase(key);
		} catch (Exception e) {
			logger.error("Write String Value To Redis Error:" + e);
			throw new RedisClientException(e);
		}finally{
			release(jedis);
		}
	}
	
	@Override
	public boolean set(String key, String value, int expiredSeconds) {
		AssertHelper.isTrue(expiredSeconds<=0, "The expiredSeconds is not less then 0 .");
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			Transaction ts = jedis.multi();
			ts.set(key, value);
			ts.expire(key, expiredSeconds);
			ts.exec();
			return true;
		} catch (Exception e) {
			logger.error("Write String Value To Redis Error:" + e);
			throw new RedisClientException(e);
		}finally{
			release(jedis);
		}
	}
	
	
	/**
	 * 
	  * <b>release </b> <br/>
	  * 
	  * 释放jedis资源<br/>
	  * 
	  * @author cpthack cpt@jianzhimao.com
	  * @param jedis 
	  * void
	  *
	 */
	protected void release(Jedis jedis) {
		if (jedis == null) {
			return;
		}
		try {
			jedis.close();
		} catch (Exception e) {
			logger.error("Release jedis Error: " + e);
			throw new RedisClientException(e);
		}
	}
}
