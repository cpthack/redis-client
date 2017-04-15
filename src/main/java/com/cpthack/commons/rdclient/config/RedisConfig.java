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
package com.cpthack.commons.rdclient.config;

/**
 * <b>RedisConfig.java</b></br>
 * 
 * <pre>
 * TODO(这里用一句话描述这个类的作用)
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月11日 下午11:59:15
 * @since JDK 1.7
 */
public class RedisConfig extends AbstractConfig {
	
	private static final String FILE_NAME                = "redis_config.properties";
	private static final String CONFIG_NAME_SERVER_IP    = "redis.server.ip";
	private static final String CONFIG_NAME_SERVER_PORT  = "redis.server.port";
	private static final String CONFIG_NAME_SERVER_PWD   = "redis.server.password";
	
	private static final String CONFIG_NAME_POOL_MAX     = "redis.client.pool.max";
	private static final String CONFIG_NAME_POOL_IDLE    = "redis.client.pool.idle";
	private static final String CONFIG_NAME_TESTONBORROW = "redis.client.pool.TestOnBorrow";
	private static final String CONFIG_NAME_TRY_TIMEOUT  = "redis.client.pool.try.timeout";
	
	public RedisConfig() {
		reloadConfig();
	}
	
	@Override
	public String getConfigFile() {
		return FILE_NAME;
	}
	
	public String getServerIp() {
		return getProperty(CONFIG_NAME_SERVER_IP);
	}
	
	public int getServerPort() {
		return getPropertyToInt(CONFIG_NAME_SERVER_PORT, 6379);
	}
	
	public String getServerAuth() {
		return getProperty(CONFIG_NAME_SERVER_PWD);
	}
	
	public int getPoolMax() {
		return getPropertyToInt(CONFIG_NAME_POOL_MAX, 10);
	}
	
	public int getPoolIdle() {
		return getPropertyToInt(CONFIG_NAME_POOL_IDLE, 1);
	}
	
	public boolean getTestOnBorrow() {
		String temp = getProperty(CONFIG_NAME_TESTONBORROW);
		return ("true".equalsIgnoreCase(temp));
	}
	
	public int getTryTimeout() {
		return getPropertyToInt(CONFIG_NAME_TRY_TIMEOUT, 10000);
	}
	
}
