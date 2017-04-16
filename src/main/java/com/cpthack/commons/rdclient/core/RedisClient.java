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

import com.cpthack.commons.rdclient.config.RedisConfig;
import com.cpthack.commons.rdclient.event.RedisListener;
import com.cpthack.commons.rdclient.queue.IAtom;

/**
 * <b>RedisClient.java</b></br>
 * 
 * <pre>
 * Redis操作类接口定义
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @param <T>
 * @date 2017年4月12日 下午2:02:09
 * @since JDK 1.7
 */
public interface RedisClient<T> {
	
	/**
	 * 
	 * <b>setRedisConfig </b> <br/>
	 * 
	 * 设置RedisConfig配置 <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param redisConfig
	 * @return RedisClient
	 *
	 */
	RedisClient<?> setRedisConfig(RedisConfig redisConfig);
	
	/**
	 * <b>getJedis </b> <br/>
	 * <br/>
	 * 
	 * 获取redis的操作对象<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param redisConfig
	 * @return Jedis
	 *
	 */
	T getJedis();
	
	/**
	 * 
	 * <b>keys </b> <br/>
	 * <br/>
	 * 
	 * Returns all the keys matching the glob-style pattern as space separated strings. For example
	 * if you have in the database the keys "foo" and "foobar" the command "KEYS foo*" will return
	 * "foo foobar". <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param pattern
	 * @return Set<String>
	 *
	 */
	Set<String> keys(String pattern);
	
	/**
	 * 
	 * <b>按模式删除多个KEY</b> <br/>
	 * <br/>
	 * Delete value When the keys matching the glob-style pattern as space separated strings. For
	 * example if you have in the database the keys "foo" and "foobar" the method
	 * "deleteByPattern(foo*)" will delete "foo foobar".<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param patternKey
	 * @return Long
	 *
	 */
	Long deleteByPattern(String patternKey);
	
	/**
	 * 
	 * <b>String字符串写操作</b> <br/>
	 * <br/>
	 * 
	 * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
	 * GB).<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 *            缓存KEY
	 * @param value
	 *            缓存VALUE（字符串）
	 * @return boolean
	 *
	 */
	boolean set(String key, String value);
	
	/**
	 * 
	 * <b>String字符串写操作</b> <br/>
	 * 
	 * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
	 * GB).<br/>
	 * Moreover,Set the expiredSeconds of the key.<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 *            缓存KEY
	 * @param value
	 *            缓存VALUE（字符串）
	 * @param expiredSeconds
	 *            多长时间(秒)过期
	 * @return boolean
	 *
	 */
	boolean set(String key, String value, int expiredSeconds);
	
	/**
	 * 
	 * <b>String字符串读操作</b> <br/>
	 * <br/>
	 * 
	 * Get the value of the specified key. If the key does not exist null is returned. If the value
	 * stored at key is not a string an error is returned because GET can only handle string values. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @return String
	 *
	 */
	String get(String key);
	
	/**
	 * 
	 * <b>Hash写操作</b> <br/>
	 * <br/>
	 * 
	 * Set the respective fields to the respective values. HMSET replaces old values with new
	 * values.
	 * 
	 * If key does not exist, a new key holding a hash is created. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param hashMap
	 * @return boolean
	 *
	 */
	boolean hmset(String key, Map<String, String> hashMap);
	
	/**
	 * 
	 * <b>Hash写操作</b> <br/>
	 * <br/>
	 * 
	 * Set the respective fields to the respective values. HMSET replaces old values with new
	 * values.
	 * 
	 * If key does not exist, a new key holding a hash is created. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param hashMap
	 * @param expiredSeconds
	 * @return boolean
	 *
	 */
	boolean hmset(String key, Map<String, String> hashMap, int expiredSeconds);
	
	/**
	 * 
	 * <b>Hash读操作</b> <br/>
	 * <br/>
	 * 
	 * Return all the fields and associated values in a hash.<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @return Map<String,String>
	 *
	 */
	Map<String, String> hgetAll(String key);
	
	/**
	 * 
	 * <b>Hash写操作</b> <br/>
	 * <br/>
	 * 
	 * Set the specified hash field to the specified value.
	 * 
	 * If key does not exist, a new key holding a hash is created. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 * @return If the field already exists, and the HSET just produced an update of the value, 0 is
	 *         returned, otherwise if a new field is created 1 is returned.
	 *
	 */
	long hset(String key, String hashKey, String hashValue);
	
	/**
	 * 
	 * <b>Hash读操作</b> <br/>
	 * <br/>
	 * 
	 * If key holds a hash, retrieve the value associated to the specified field.
	 * 
	 * If the field is not found or the key does not exist, a special 'nil' value is returned. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param hashKey
	 * @return String
	 *
	 */
	String hget(String key, String hashKey);
	
	/**
	 * 
	 * <b>List写操作</b> <br/>
	 * <br/>
	 * 
	 * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
	 * key does not exist an empty list is created just before the append operation. If the key
	 * exists but is not a List an error is returned. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param values
	 * @return boolean
	 *
	 */
	boolean lpush(String key, String... values);
	
	/**
	 * 
	 * <b>List写操作（先进先出，可用于队列）</b> <br/>
	 * <br/>
	 * 
	 * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
	 * key does not exist an empty list is created just before the append operation. If the key
	 * exists but is not a List an error is returned. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param expiredSeconds
	 * @param values
	 * @return boolean
	 *
	 */
	boolean lpush(String key, int expiredSeconds, String... values);
	
	/**
	 * 
	 * <b>List写操作</b> <br/>
	 * <br/>
	 * 
	 * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
	 * key does not exist an empty list is created just before the append operation. If the key
	 * exists but is not a List an error is returned. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param values
	 * @return boolean
	 *
	 */
	boolean rpush(String key, String... values);
	
	/**
	 * 
	 * <b>List写操作</b> <br/>
	 * <br/>
	 * 
	 * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
	 * key does not exist an empty list is created just before the append operation. If the key
	 * exists but is not a List an error is returned. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param expiredSeconds
	 * @param values
	 * @return boolean
	 *
	 */
	boolean rpush(String key, int expiredSeconds, String... values);
	
	/**
	 * 
	 * <b>List读操作（先进后出读取-类似栈）</b> <br/>
	 * <br/>
	 * 
	 * Atomically return and remove the first (LPOP) or last (RPOP) element of the list. For example
	 * if the list contains the elements "a","b","c" LPOP will return "a" and the list will become
	 * "b","c".<br/>
	 * <br/>
	 * 
	 * If the key does not exist or the list is already empty the special value 'nil' is returned.
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @return String
	 *
	 */
	String lpop(String key);
	
	/**
	 * 
	 * <b>List读操作（先进先出读取-类似队列）</b> <br/>
	 * <br/>
	 * 
	 * Atomically return and remove the first (LPOP) or last (RPOP) element of the list. For example
	 * if the list contains the elements "a","b","c" RPOP will return "c" and the list will become
	 * "a","b". <br/>
	 * <br/>
	 * 
	 * If the key does not exist or the list is already empty the special value 'nil' is returned. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @return String
	 *
	 */
	String rpop(String key);
	
	/**
	 * 
	 * <b>List读操作(保证绝对的事务性)</b> <br/>
	 * <br/>
	 * 
	 * 先进先出 OR 先进后出 取决于数据打入时采用的是lpush OR rpush <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param atom
	 * @return String
	 *
	 */
	String popQueue(String key, IAtom atom);
	
	/**
	 * 
	 * <b>List读操作</b> <br/>
	 * <br/>
	 * 
	 * Return the specified elements of the list stored at the specified key. Start and end are
	 * zero-based indexes. 0 is the first element of the list (the list head), 1 the next element
	 * and so on.
	 * 
	 * For example LRANGE foobar 0 2 will return the first three elements of the list.
	 * 
	 * start and end can also be negative numbers indicating offsets from the end of the list. For
	 * example -1 is the last element of the list, -2 the penultimate element and so on. <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param start
	 * @param end
	 * @return List<String>
	 *
	 */
	List<String> lrange(String key, long start, long end);
	
	/**
	 * 
	 * <b>setnx </b> <br/>
	 * <br/>
	 * 
	 * SETNX works exactly like SET with the only difference that if the key already exists no
	 * operation is performed. SETNX actually means "SET if Not eXists". <br/>
	 * <br/>
	 * 1 if the key was set 0 if the key was not set
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key
	 * @param value
	 * @return long
	 *
	 */
	long setnx(String key, String value, int expiredSeconds);
	
	/**
	 * 
	 * <b>发布订阅</b> <br/>
	 * <br/>
	 * 
	 * 消息订阅<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param channel
	 * @param redisListener
	 * @return boolean
	 *
	 */
	boolean subscribe(String channel, RedisListener redisListener);
	
	/**
	 * 
	 * <b>发布订阅</b> <br/>
	 * <br/>
	 * 
	 * 消息发送<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param channel
	 * @param message
	 *            void
	 *
	 */
	void publish(String channel, String message);
}
