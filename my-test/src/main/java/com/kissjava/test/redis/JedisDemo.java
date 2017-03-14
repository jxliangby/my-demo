package com.kissjava.test.redis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class JedisDemo {
	
	public static String now(){
		return DateFormatUtils.format(new Date(), "yyy-MM-dd hh:mm:ss SSS");
	}
	public static void test(){
		Jedis jedis = new Jedis("192.168.56.101",6379);
		jedis.auth("123456");
		jedis.set("key", "value");
		System.out.println(jedis.get("key"));
		////////////////
		jedis.close();
	}
	
	public static void testSubscribe(){
		final Jedis subJedis = new Jedis("192.168.56.101",6379);
		subJedis.auth("123456");
		
		final Jedis pubJedis = new Jedis("192.168.56.101",6379);
		pubJedis.auth("123456");
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				String message = RandomStringUtils.randomAlphabetic(10)+":at time-->"+ now();
				publish(message);
			}
			private void publish(String message){
				long getIntegerReply = pubJedis.publish("chat", message);
				if(getIntegerReply<=0){
					System.out.println("无人订阅，重新发送..... getIntegerReply is " + getIntegerReply);
					publish(message);
				}
			}
		}, 0, 1000);
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				subJedis.subscribe(new JedisPubSub() { 
					public void onMessage(String channel, String message) {
						System.out.println("subscribe....channer:"+channel+" time is "+now()+"...message:"+message);
					}
				}, "chat","chat2");
			}
		}, 10000);
		
		//jedis.close();
	
	}
	
	public static void testList(){
		Jedis jedis = new Jedis("192.168.56.101",6379);
		jedis.auth("123456");
		String listKey = "test-list-key";
		jedis.lpush(listKey, "a","b","c");
		long listLength = jedis.llen(listKey);
		List<String> list = jedis.lrange(listKey, 0, listLength);
		System.out.println(list);
		
		String popValue = jedis.lpop(listKey);
		System.out.println("popValue=" + popValue);
		
		listLength = jedis.llen(listKey);
		list = jedis.lrange(listKey, 0, listLength);
		System.out.println(list);
		
		jedis.rpush(listKey, "111","2222","333333");
		listLength = jedis.llen(listKey);
		list = jedis.lrange(listKey, 0, listLength);
		System.out.println(list);
		
		/////////////
		jedis.close();
	}
	
	public static void testHash(){
		Jedis jedis = new Jedis("192.168.56.101",6379);
		jedis.auth("123456");
		String hashKey = "test-hash-key";
		
		jedis.hset(hashKey, "id", "id0001");
		jedis.hset(hashKey, "name", "kissjava");
		
		String id = jedis.hget(hashKey, "id");
		String name = jedis.hget(hashKey, "name");
		System.out.println("id = " + id + ",,,,name = " + name);
		
		Map<String, String> hash = jedis.hgetAll(hashKey);
		System.out.println("hash = " + hash);
		
		///////////////////////
		jedis.close();
	}
	
	public static void testSet(){
		Jedis jedis = new Jedis("192.168.56.101",6379);
		jedis.auth("123456");
		String setKey = "test-set-key";
		jedis.sadd(setKey, "111", "222", "333", "444", "555");
		System.out.println("members is" + jedis.smembers(setKey));
		String pop = jedis.spop(setKey);
		System.out.println("pop is " + pop+ "---members is"+jedis.smembers(setKey));
		
		long dd = jedis.srem(setKey, "111");
		System.out.println("rem is 111---members is"+jedis.smembers(setKey));
		
		String setKey2 = "test-set-key2";
		jedis.sadd(setKey2, "111","222","66666","77777","88888");
		Set<String> diffSet = jedis.sdiff(setKey, setKey2);
		System.out.println("diffSet is" + diffSet);
		
		Set<String> unionSet = jedis.sunion(setKey, setKey2);
		System.out.println("unionSet is" + unionSet);
		jedis.close();
	}
	public static void main(String[] args) {
		//testSubscribe();
		//testList();
		//testHash();
		testSet();
	}

}
