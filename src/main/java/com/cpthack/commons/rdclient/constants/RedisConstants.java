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
package com.cpthack.commons.rdclient.constants;

/**
 * <b>RedisConstants.java</b></br>
 * 
 * <pre>
 * redis常量类
 * </pre>
 * 
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月15日 下午8:37:56
 * @since JDK 1.7
 */
public class RedisConstants {
	/**
	 * redis默认配置 配置文件名
	 */
	public static final String DEFALUT_REDIS_FILE_NAME             = "redis_config.properties";
	
	/**
	 * redis默认配置 端口
	 */
	public static final int    DEFAULT_REDIS_PORT                  = 6379;
	
	/**
	 * redis默认配置 最大连接数
	 */
	public static final int    DEFAULT_REDIS_POOL_MAX              = 10;
	
	/**
	 * redis默认配置 每次连接数增加数
	 */
	public static final int    DEFAULT_REDIS_POOL_IDLE             = 1;
	
	/**
	 * redis默认配置 连接超时时间
	 */
	public static final int    DEFAULT_REDIS_TRY_TIMEOUT           = 10000;
	
	/**
	 * redis默认配置 临时队列名
	 */
	public static final String DEFAULT_REDIS_TEMP_QUEUE_TIMEP_NAME = "tmp_queue";
}
