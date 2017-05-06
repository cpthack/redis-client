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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpthack.commons.rdclient.config.RedisConfig;
import com.cpthack.commons.rdclient.constants.RedisConstants;
import com.cpthack.commons.rdclient.event.RedisListener;
import com.cpthack.commons.rdclient.event.RedisMsgPubSubListener;
import com.cpthack.commons.rdclient.exception.AssertHelper;
import com.cpthack.commons.rdclient.exception.RedisClientException;
import com.cpthack.commons.rdclient.queue.IAtom;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * 
 * <b>JedisRedisClient.java</b></br>
 * 
 * <pre>
 * 基于jedis第三方依赖库实现RedisClient接口
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月15日 下午12:04:27
 * @since JDK 1.7
 */
public class JedisRedisClient implements RedisClient<Jedis> {
	
	private static Logger							  logger					= LoggerFactory.getLogger(JedisRedisClient.class);
	private RedisConfig								  redisConfig				= null;
	private final Map<String, RedisMsgPubSubListener> redisMsgPubSubListenerMap	= new ConcurrentHashMap<String, RedisMsgPubSubListener>();
	
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
		}
		catch (Exception e) {
			logger.warn("Read redis in error:" + e);
			throw new RedisClientException(e);
		}
		finally {
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
		}
		catch (Exception e) {
			logger.warn("Delete redis pattern keys in error:" + e);
			throw new RedisClientException(e);
		}
		finally {
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
		}
		catch (Exception e) {
			logger.error("Write String Value To Redis Error:" + e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public boolean set(String key, String value, int expiredSeconds) {
		AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			Transaction ts = jedis.multi();
			ts.set(key, value);
			ts.expire(key, expiredSeconds);
			ts.exec();
			return true;
		}
		catch (Exception e) {
			logger.error("Write String Value To Redis Error:" + e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public String get(String key) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		String value = null;
		try {
			value = jedis.get(key);
		}
		catch (Exception e) {
			logger.warn("Read redis in error:" + e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
		return value;
	}
	
	@Override
	public boolean hmset(String key, Map<String, String> hashMap) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			key = jedis.hmset(key, hashMap);
			return "OK".equalsIgnoreCase(key);
		}
		catch (Exception e) {
			logger.error("Execute hmset in Redis Error:" + e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public boolean hmset(String key, Map<String, String> hashMap, int expiredSeconds) {
		AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		try {
			Transaction ts = jedis.multi();
			ts.hmset(key, hashMap);
			ts.expire(key, expiredSeconds);
			key = (String) ts.execGetResponse().get(0).get();// 提交事务并返回"ts.hmset(key, hashMap)"的执行结果
			return "OK".equalsIgnoreCase(key);
		}
		catch (Exception e) {
			logger.error("Execute hmset in Redis Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			return jedis.hgetAll(key);
		}
		catch (Exception e) {
			logger.error("Get all value from hash Error:" + e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public long hset(String key, String hashKey, String hashValue) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			long result = jedis.hset(key, hashKey, hashValue);
			return result;
		}
		catch (Exception e) {
			logger.error("Write Hash Value To Redis Error:" + e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public String hget(String key, String hashKey) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			return jedis.hget(key, hashKey);
		}
		catch (Exception e) {
			logger.error("Get value from Hash Error:" + e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public boolean lpush(String key, String... values) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			long result = jedis.lpush(key, values);
			return result > 0;
		}
		catch (Exception e) {
			logger.error("Write List Value To Redis Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public boolean lpush(String key, int expiredSeconds, String... values) {
		AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		try {
			Transaction ts = jedis.multi();
			ts.lpush(key, values);
			ts.expire(key, expiredSeconds);
			long result = Long.parseLong(String.valueOf(ts.execGetResponse().get(0).get()));// 提交事务并返回"ts.setnx(key,value)"的执行结果
			return result > 0;
		}
		catch (Exception e) {
			logger.error("Write List Value To Redis Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public boolean rpush(String key, String... values) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			long result = jedis.rpush(key, values);
			return result > 0;
		}
		catch (Exception e) {
			logger.error("Write List Value To Redis Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public boolean rpush(String key, int expiredSeconds, String... values) {
		AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		try {
			Transaction ts = jedis.multi();
			ts.rpush(key, values);
			ts.expire(key, expiredSeconds);
			// 提交事务并返回"ts.setnx(key,value)"的执行结果
			long result = Long.parseLong(String.valueOf(ts.execGetResponse().get(0).get()));
			return result > 0;
		}
		catch (Exception e) {
			logger.error("Write List Value To Redis Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public String lpop(String key) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			return jedis.lpop(key);
		}
		catch (Exception e) {
			logger.error("Lpop value from list Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public String rpop(String key) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			return jedis.rpop(key);
		}
		catch (Exception e) {
			logger.error("Rpop value from list Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public String popQueue(String key, IAtom atom) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		
		String dstkey = RedisConstants.DEFAULT_REDIS_TEMP_QUEUE_TIMEP_NAME + "-" + key;
		
		try {
			
			// 事务1，从临时队列中消费数据并继续存回队列中
			Transaction ts = jedis.multi();
			ts.rpoplpush(dstkey, dstkey);
			String result = (String) ts.execGetResponse().get(0).get();
			
			// 事务2，从临时队列中消费数据
			ts = jedis.multi();
			ts.rpop(dstkey);
			if (atom.run(result)) {
				ts.exec();
			}
			
			// 事务3，当临时队列为空时，从目标队列中取出数据到临时队列中，以供下次使用
			if (result == null) {
				ts = jedis.multi();
				ts.rpoplpush(key, dstkey);
				result = (String) ts.execGetResponse().get(0).get();
			}
			return result;
		}
		catch (Exception e) {
			logger.error("Execute lpopQueue in Redis Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			return jedis.lrange(key, start, end);
		}
		catch (Exception e) {
			logger.error("Get range value from list Error:" + e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public long setnx(String key, String value, int expiredSeconds) {
		AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		try {
			Transaction ts = jedis.multi();
			ts.setnx(key, value);
			ts.expire(key, expiredSeconds);
			long result = Long.parseLong(String.valueOf(ts.execGetResponse().get(0).get()));// 提交事务并返回"ts.setnx(key,value)"的执行结果
			return result;
		}
		catch (Exception e) {
			logger.error("Execute setnx in Redis Error:", e);
			throw new RedisClientException(e);
		}
		finally {
			release(jedis);
		}
	}
	
	@Override
	public boolean subscribe(String channel, RedisListener redisListener) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
		
		RedisMsgPubSubListener redisMsgPubSubListener = redisMsgPubSubListenerMap.get(channel);
		
		if (redisMsgPubSubListener == null) {
			redisMsgPubSubListener = new RedisMsgPubSubListener(channel);
			
			try {
				redisMsgPubSubListener.startRedisMsgPubSubListener(jedis, redisMsgPubSubListener);
				redisMsgPubSubListenerMap.put(channel, redisMsgPubSubListener);
			}
			catch (Exception e) {
				logger.error("The Redis subscribe Error:", e);
				throw new RedisClientException(e);
			}
			finally {
				release(jedis);
			}
			
		}
		
		// 添加发布订阅-自定义监听器
		redisMsgPubSubListener.addRedisListener(redisListener);
		return true;
	}
	
	@Override
	public void publish(String channel, String message) {
		Jedis jedis = getJedis();
		AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
		try {
			jedis.publish(channel, message);
		}
		catch (Exception e) {
			logger.error("The Redis publish message Error:", e);
			throw new RedisClientException(e);
		}
		finally {
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
	 *            void
	 *
	 */
	protected void release(Jedis jedis) {
		if (jedis == null) {
			return;
		}
		try {
			jedis.close();
		}
		catch (Exception e) {
			logger.error("Release jedis Error: ", e);
			throw new RedisClientException(e);
		}
	}
	
}
