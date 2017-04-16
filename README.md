# redis-client

基于jedis封装的redis操作工具，包含了常用的操作方法，更加方便使用

## 一、使用示例：

#### 1、自定义RedisConfig配置
> TestRedisConfig.java代码引用

	public class TestRedisConfig extends RedisConfig {
		private static final String FILE_NAME = "test_redis_config.properties";
	
		@Override
		public String getConfigFile() {
			return FILE_NAME;
		}
	}
	继承RedisConfig类并且重写getConfigFile方法即可，重新指定redis的配置文件.

配置文件模板如：[test_redis_config.properties](https://github.com/cpthack/redis-client/blob/master/src/test/resources/test_redis_config.properties)所示，主要参考配置项

#### 2、String数据类型使用
> RedisClientStringTest.java代码引用

		logger.info("生成默认RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient();
		
		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		logger.info("获取到Jedis对象，方便developers自定义扩展redis的其他操作");
		Jedis jedis = (Jedis) redisClient.getJedis();
		jedis.close();
		
		logger.info("Redis Set 字符串英文值");
		redisClient.set("test1", "my name is cpthack .");
		
		logger.info("Redis Set 字符串中文值");
		redisClient.set("test2", "我叫成佩涛 .");
		
		logger.info("获取 KEY = [test] 的值 VALUE = [" + redisClient.get("test") + "]");
		
		Set<String> keySet = redisClient.keys("t*");
		for (String key : keySet) {
			logger.info("遍历redis中的key，目前查询到有>>>>>KEY = [" + key + "]");
		}
		
		logger.info("删除所有模式匹配[t*]的KEY,总共删除COUNT=[" + redisClient.deleteByPattern("t*") + "]");
		keySet = redisClient.keys("t*");
		logger.info("目前还剩符合模式匹配[t*]的KEY，总共有COUNT=[" + keySet.size() + "]");

#### 3、Hash数据类型使用
> RedisClientHashTest.java代码引用

		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		String key = "root-key";
		Map<String, String> hashMap = new HashMap<String, String>() {
			{
				put("abc", "abc");
			}
		};
		int expiredSeconds = 5 * 60;// 5分钟
		redisClient.hmset(key, hashMap, expiredSeconds);
		logger.info("设置HASH值hashMap：KEY = [" + key + "],SIZE = [" + hashMap.size() + "]");
		
		String hashKey = "hash-key";
		String hashValue = "hash-value";
		redisClient.hset(key, hashKey, hashValue);
		
		logger.info("获取KEY = [" + hashKey + "]的值 , VALUE = [" + redisClient.hget(key, hashKey) + "]");
		
		Map<String, String> resultMap = redisClient.hgetAll(key);
		for (String resultKey : resultMap.keySet()) {
			logger.info("遍历resultMap,KEY = [" + resultKey + "],VALUE = [" + resultMap.get(resultKey) + "]");
		}

#### 4、List数据类型使用
> RedisClientListTest.java代码引用

		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		String key = "list-key";
		String value = "list-value";
		int expireSeconds = 5 * 60;
		
		redisClient.deleteByPattern("*");// 清除所有数据
		for (int i = 0; i < 50; i++) {
			redisClient.lpush(key, expireSeconds, value + i);
		}
		
		// logger.info("从链表头部开始取出其中一个数据,VALUE = ["+redisClient.lpop(key)+"]");
		
		// logger.info("从链表尾部开始取出其中一个数据,VALUE = ["+redisClient.rpop(key)+"]");
		
		logger.info("以保证redis List取值的事务性，从队列中取出数据并消费，如果消费失败则撤回取数据操作");
		for (int i = 1; i < 51; i++) {
			logger.info("第[" + i + "]次取队列 KEY=[" + key + "]中的数据");
			redisClient.popQueue(key, new IAtom() {
				@Override
				public boolean run(String message) {
					logger.info("取出队列信息,VALUE = [" + message + "]");
					return true;
				}
			});
		}
		
		List<String> resultList = redisClient.lrange(key, 0, 5);
		for (String resultValue : resultList) {
			logger.info("获取List的值,VALUE = [" + resultValue + "]");
		}

其中，redisClient.popQueue方法已经封装了对List队列数据中取数据的事务性操作，防止在队列取操作后准备其他业务逻辑执行时出现断电等极端情况导致的数据丢失。

#### 5、发布订阅使用

> 消息订阅者：RedisClientSubTest.java代码引用

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

> 消息发布者：RedisClientPubTest.java代码引用

		logger.info("生成自定义RedisConfig配置的RedisClient对象.");
		RedisClient redisClient = RedisClientFactory.getClient(new TestRedisConfig());
		
		String channel = "send-email";
		
		for (int i = 0; i < 50000; i++)
			redisClient.publish(channel, "发布订阅，我是成佩涛，你收到信息了吗？");


## 二、具体参考：

* String数据类型测试类：[RedisClientStringTest.java](https://github.com/cpthack/redis-client/blob/master/src/test/java/com/cpthack/commons/rdclient/RedisClientStringTest.java)

* Hash数据类型测试类：[RedisClientHashTest.java](https://github.com/cpthack/redis-client/blob/master/src/test/java/com/cpthack/commons/rdclient/RedisClientHashTest.java)

* List数据类型测试类：[RedisClientListTest.java](https://github.com/cpthack/redis-client/blob/master/src/test/java/com/cpthack/commons/rdclient/RedisClientListTest.java)

* 发布订阅-消息订阅者测试类：[RedisClientSubTest.java](https://github.com/cpthack/redis-client/blob/master/src/test/java/com/cpthack/commons/rdclient/RedisClientSubTest.java)

* 发布订阅-消息发布者测试类：[RedisClientPubTest.java](https://github.com/cpthack/redis-client/blob/master/src/test/java/com/cpthack/commons/rdclient/RedisClientPubTest.java)
