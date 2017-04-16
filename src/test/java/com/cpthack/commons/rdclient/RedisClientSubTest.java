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
import com.cpthack.commons.rdclient.event.RedisListener;

/**
 * <b>RedisClientSubTest.java</b></br>
 * 
 * <pre>
 * redis发布订阅 - 订阅者测试类
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月16日 上午1:19:14
 * @since JDK 1.7
 */
public class RedisClientSubTest {
	
	private static Logger logger = LoggerFactory.getLogger(RedisClientSubTest.class);
	
	public static void main(String[] args) {
		
		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		String channel = "send-email";
		redisClient.subscribe(channel, new RedisListener() {
			@Override
			public void onMessage(String channel, String message) {
				logger.info("Received message from the channel = [" + channel + "],the message = [" + message + "]");
			}
		});
		
		logger.info("redis的发布订阅模式中，消息是瞬时消费的，当订阅者出现中断时会存在消息丢失.");
	}
}
