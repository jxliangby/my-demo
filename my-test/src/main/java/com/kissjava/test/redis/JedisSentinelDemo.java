package com.kissjava.test.redis;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinelDemo {
	
	private JedisSentinelPool pool;
	public JedisSentinelDemo(){
		init();
	}
	public void init(){
		String master = "mymaster";
		String password = "123456";
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.56.101:9000");
		pool = new JedisSentinelPool(master, sentinels,password);
	}
	public Jedis get(){
		try{
			return pool.getResource();
		}catch(Exception e){
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return get();
		}
	}
	public void test(){
		//Jedis jedis = get();
		//jedis.set("sentinel-key", "sentinel-test");
		//System.out.println(jedis.get("sentinel-key"));
		int size = 100000;
		String preKey = "sentinel-key-";
		long t1 = System.currentTimeMillis();
		for(int index=0;index<size; index++){
			if(index%100==0){
				System.out.println("for set index is " + index);
			}
			set(get(), preKey+index, "the value is value-" + index);
		}
		long t2 = System.currentTimeMillis();
		System.out.println("set time = " + (t2-t1)/1000);
		
		for(int index=0;index<size; index++){
			if(index%100==0){
				System.out.println("for set index is " + index);
			}
			get().get(preKey+index);
		}
		long t3 = System.currentTimeMillis();
		System.out.println("get time = " + (t3-t2)/1000);
		//jedis.close();
	}
	public void set(Jedis jedis, String key,String value){
		try{
			jedis.set(key, value);
			jedis.close();
		}catch(Exception e){
			e.printStackTrace();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			set(get(), key, value);
			
		}
	}
	public static void main(String[] args) {
		JedisSentinelDemo demo = new JedisSentinelDemo();
		demo.test();
	}

}
