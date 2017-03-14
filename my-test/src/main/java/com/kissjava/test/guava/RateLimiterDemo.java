package com.kissjava.test.guava;

import com.google.common.util.concurrent.RateLimiter; 

public class RateLimiterDemo {
	private static final int exceuteSite = 100;
	
	public static void main(String[] args) {  
        testNoRateLimiter();  
        testWithRateLimiter();  
    }  
   
    public static void testNoRateLimiter() {  
        Long start = System.currentTimeMillis();  
        for (int i = 0; i < exceuteSite; i++) {  
            System.out.println("call execute.." + i);  
        }  
        Long end = System.currentTimeMillis();  
        System.out.println(end - start);  
          
    }  
      
    public static void testWithRateLimiter() {  
        Long start = System.currentTimeMillis();  
        RateLimiter limiter = RateLimiter.create(10.0); // 每秒不超过10个任务被提交  
        for (int i = 0; i < exceuteSite; i++) {  
            limiter.acquire(); // 请求RateLimiter, 超过permits会被阻塞  
            System.out.println("call execute.." + i);  
              
        }  
        Long end = System.currentTimeMillis();  
        System.out.println(end - start);  
    }  
}
