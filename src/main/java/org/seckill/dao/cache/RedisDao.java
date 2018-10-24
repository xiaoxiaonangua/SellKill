package org.seckill.dao.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Daniel.Zhang
 * @email daniel.zhang.china@hotmail.com
 * @created on 2018-06-29
 * 
 *
 */
public class RedisDao {

	private final JedisPool jedisPool;

	private Jedis jedis;

	public RedisDao() {
		this.jedisPool = new JedisPool("localhost", 6379);
		this.jedis = jedisPool.getResource();
	}

	public RedisDao(String ip, int port) {
		this.jedisPool = new JedisPool(ip, port);
		this.jedis = jedisPool.getResource();
	}

	public Jedis getJedis() {
		return jedis;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void close() {

		if (jedis != null)
			jedis.close();
		if (jedisPool != null)
			jedisPool.close();

	}

}
