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

import com.cpthack.commons.rdclient.config.RedisConfig;

/**
 * <b>RedisClient.java</b></br> TODO(这里用一句话描述这个类的作用)</br>
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
	  * <b>keys </b> <br/><br/>
	  * 
	  *Returns all the keys matching the glob-style pattern as space separated strings. For example 
	  *if you have in the database the keys "foo" and "foobar" the command "KEYS foo*" will return "foo foobar". <br/>
	  * 
	  * @author cpthack cpt@jianzhimao.com
	  * @param pattern
	  * @return 
	  * Set<String>
	  *
	 */
	Set<String> keys(String pattern);
	
	/**
	 * 
	  * <b>deleteByPattern </b> <br/>
	  * 
	  * <br/>按模式删除多个KEY <br/>
	  * Delete value When the keys matching the glob-style pattern as space separated strings. For example 
	  * if you have in the database the keys "foo" and "foobar" the method "deleteByPattern(foo*)" will delete "foo foobar". 
	  * 
	  * @author cpthack cpt@jianzhimao.com
	  * @param patternKey 模式的KEY
	  * @return 
	  * Long
	  *
	 */
	Long deleteByPattern(String patternKey);
	
	
	/**
	 * 
	 * <b>set </b> <br/><br/>
	 * 
	 * Set the string value as value of the key. The string can't be longer than
	 * 1073741824 bytes (1 GB).<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param key 缓存KEY
	 * @param value 缓存VALUE（字符串）
	 * @return boolean
	 *
	 */
	boolean set(String key, String value);

	/**
	 * 
	  * <b>set </b> <br/><br/>
	  * 
	  * Set the string value as value of the key. The string can't be longer than
	  * 1073741824 bytes (1 GB).<br/>
	  * Moreover,Set the expiredSeconds of the key.<br/>
	  * 
	  * @author cpthack cpt@jianzhimao.com
	  * @param key 缓存KEY
	  * @param value 缓存VALUE（字符串）
	  * @param expiredSeconds 多长时间(秒)过期
	  * @return 
	  * boolean
	  *
	 */
	boolean set(String key, String value, int expiredSeconds);
	

}
