package com.kissjava.test.redis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JesdisClusterDemo {
	
	private JedisCluster jedis;
	
	private void init(){
		HostAndPort hp1 = new HostAndPort("192.168.56.101", 7000);
		HostAndPort hp2 = new HostAndPort("192.168.56.101", 7001);
		HostAndPort hp3 = new HostAndPort("192.168.56.101", 7002);
		HostAndPort hp4 = new HostAndPort("192.168.56.101", 7003);
		HostAndPort hp5 = new HostAndPort("192.168.56.101", 7004);
		HostAndPort hp6 = new HostAndPort("192.168.56.101", 7005);
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(hp1);
		nodes.add(hp2);
		nodes.add(hp3);
		nodes.add(hp4);
		nodes.add(hp5);
		nodes.add(hp6);
		JedisPoolConfig poolConfig = new JedisPoolConfig();  
		// 最大连接数   
		poolConfig.setMaxTotal(5);  
		// 最大空闲数   
		poolConfig.setMaxIdle(2);  
		poolConfig.setTestOnBorrow(true);
		// 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：   
	    // Could not get a resource from the pool   
		poolConfig.setMaxWaitMillis(1000); 
		jedis = new JedisCluster(nodes, poolConfig);
	}
	
	public  void test(){
		jedis.set("key1", "value1");
		jedis.set("key2", "value2");
		jedis.set("key3", "value3");
		jedis.set("key4", "value4");
		jedis.set("key5", "value5");
		System.out.println(jedis.get("key1"));
		System.out.println(jedis.get("key2"));
		System.out.println(jedis.get("key3"));
		System.out.println(jedis.get("key4"));
		System.out.println(jedis.get("key5"));
		int size = 100000;
		String preKey = "cluster-key-";
		long t1 = System.currentTimeMillis();
		for(int index=0;index<size; index++){
			jedis.set(preKey+index, "the value is value-" + index);
		}
		long t2 = System.currentTimeMillis();
		System.out.println("set time = " + (t2-t1)/1000);
		
		for(int index=0;index<size; index++){
			jedis.get(preKey+index);
		}
		long t3 = System.currentTimeMillis();
		System.out.println("get time = " + (t3-t2)/1000);
		////////////////
	}
	
	private void close(){
		try {
			jedis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		JesdisClusterDemo demo = new JesdisClusterDemo();
		demo.init();
		demo.test();
		demo.close();
	}

}
