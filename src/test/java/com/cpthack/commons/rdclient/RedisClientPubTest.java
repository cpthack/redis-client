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
 * <b>RedisClientPubTest.java</b></br>
 * 
 * <pre>
 * TODO(这里用一句话描述这个类的作用)
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月16日 上午1:19:14
 * @since JDK 1.7
 */
public class RedisClientPubTest {
	
	private static Logger logger = LoggerFactory.getLogger(RedisClientPubTest.class);
	
	public static void main(String[] args) {
		
		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		String channel = "send-email";
		
		for (int i = 0; i < 50000; i++)
			redisClient.publish(channel, "发布订阅，我是成佩涛，你收到信息了吗？");
		
	}
}
