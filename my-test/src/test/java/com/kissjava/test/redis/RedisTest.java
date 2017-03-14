package com.kissjava.test.redis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:spring-redis.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTest {
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	public void test(){
		String key = "spring-test-hash-key-";
		
		redisTemplate.opsForValue().set(key+"str", "this is a string");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", "aaaaaaaaa");
		
		List<String> list = new ArrayList<>();
		list.add("1111");
		list.add("3333");
		
		map.put("b", list);
		
		//redisTemplate.opsForValue().set(key+"map", map);
		redisTemplate.opsForHash().put(key, "map", map);
		
		//System.out.println(redisTemplate.opsForValue().get(key+"str"));
		System.out.println(redisTemplate.opsForHash().get(key,"map"));
		
		
	}
}
