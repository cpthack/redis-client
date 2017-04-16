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
package com.cpthack.commons.rdclient.event;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.cpthack.commons.rdclient.exception.AssertHelper;

/**
 * <b>RedisMsgPubSubListener.java</b></br>
 * 
 * <pre>
 * 发布订阅 - 消息监听统一处理</br>
 * <b>注意要点：</b>
 * 1、消息可靠性不强，如果在订阅方断线，那么他将会丢失所有在短线期间发布者发布的消息。
 * 2、如果一个客户端订阅了频道，但自己读取消息的速度却不够快的话，那么不断积压的消息会使
 * redis输出缓冲区的体积变得越来越大，这可能使得redis本身的速度变慢，甚至直接崩溃。
 * 3、适合场景：使用较简单，但是需要容忍存在消息丢失的情况。
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月16日 上午12:17:23
 * @since JDK 1.7
 */
public class RedisMsgPubSubListener extends JedisPubSub {
	private static Logger       logger            = LoggerFactory.getLogger(RedisMsgPubSubListener.class);
	private List<RedisListener> redisListenerList = new ArrayList<RedisListener>();
	private boolean             isListen          = false;
	private String              channel           = null;
	
	private int                 messageCount      = 0;                                                    // 收到的消息次数
	                                                                                                       
	public RedisMsgPubSubListener(String channel) {
		this.channel = channel;
	}
	
	public void startRedisMsgPubSubListener(final Jedis jedis, final RedisMsgPubSubListener redisMsgPubSubListener) {
		AssertHelper.notBlank(channel, "The channel is Not Allow Empty .");
		
		if (!isListen) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						jedis.subscribe(redisMsgPubSubListener, channel);
					}
					catch (Exception e) {
						logger.error("", e);
						isListen = false;
						startRedisMsgPubSubListener(jedis, redisMsgPubSubListener);// 发生异常后不断重连，消息不可靠，重连期间会发生消息丢失
					}
				}
			});
			thread.start();
		}
		logger.debug("This Channel is [" + channel + "]. The RedisMsgPubSubListener is Start Now !");
	}
	
	/**
	 * 
	 * <b>addRedisListener</b> <br/>
	 * <br/>
	 * 
	 * 添加发布订阅-消息监听器<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param channel
	 * @param redisListener
	 *            void
	 *
	 */
	public void addRedisListener(RedisListener redisListener) {
		redisListenerList.add(redisListener);
		logger.debug("Add RedisListener To RedisMsgPubSubListener . "
		        + "The channel = [" + channel + "],The RedisListener = [" + redisListener.getClass().getName() + "]");
	}
	
	@Override
	public void unsubscribe() {
		super.unsubscribe();
	}
	
	@Override
	public void unsubscribe(String... channels) {
		super.unsubscribe(channels);
	}
	
	@Override
	public void subscribe(String... channels) {
		super.subscribe(channels);
	}
	
	@Override
	public void psubscribe(String... patterns) {
		super.psubscribe(patterns);
	}
	
	@Override
	public void punsubscribe() {
		super.punsubscribe();
	}
	
	@Override
	public void punsubscribe(String... patterns) {
		super.punsubscribe(patterns);
	}
	
	@Override
	public void onMessage(String channel, String message) {
		logger.debug("channel:" + channel + "receives message :" + message);
		for (RedisListener redisListener : redisListenerList) {
			redisListener.onMessage(channel, message);
		}
		synchronized (this) {
			logger.debug("收到的消息次数，messageCount = [" + ++messageCount + "]");
		}
		// this.subscribe(channel);
	}
	
	@Override
	public void onPMessage(String pattern, String channel, String message) {
		
	}
	
	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		logger.debug("channel:" + channel + "is been subscribed:" + subscribedChannels);
	}
	
	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		
	}
	
	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		
	}
	
	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		logger.debug("channel:" + channel + "is been unsubscribed:" + subscribedChannels);
	}
}
