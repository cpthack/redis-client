# redis-client

基于jedis封装的redis操作工具，包含了常用的操作方法，更加方便使用

## 使用示例：

#### 自定义RedisConfig配置
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

## 具体参考：

* String数据类型测试类[]()

* Hash数据类型测试类[]()

* List数据类型测试类[]()

* 发布订阅测试类[]()

