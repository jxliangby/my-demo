package com.crt.openapi.storm.entity;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

public class ProductsReader {
	String redisHost;
	int redisPort;
	Jedis jedis;

	public ProductsReader(String redisHost, int redisPort) {
		this.redisHost = redisHost;
		this.redisPort = redisPort;
		reconnect();
	}

	private void reconnect() {
		jedis = new Jedis(redisHost, redisPort);
	}

	public Product readItem(String id) throws Exception {
		String content = jedis.get(id);
		if (content == null || ("nil".equals(content)))
			return null;
		JSONObject json = JSONObject.parseObject(content);
		Product i = new Product((Long) json.get("id"), (String) json.get("title"), (Long) json.get("price"),
				(String) json.get("category"));
		return i;
	}

}