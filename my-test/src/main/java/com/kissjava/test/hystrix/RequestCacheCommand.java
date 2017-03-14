package com.kissjava.test.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class RequestCacheCommand extends HystrixCommand<String>{
	private final int id;
	
	
	public RequestCacheCommand(int id){
		super(HystrixCommandGroupKey.Factory.asKey("RequestCacheCommand222"));
		this.id = id;
	}
	
	protected String run() throws Exception { 
		System.out.println(Thread.currentThread().getName() + " execute id=" + id); 
		 return "executed=" + id;  
	}
	
	protected String getCacheKey() {  
		return String.valueOf(id); 
	}
	
	public static void main(String[] args) {
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		
		RequestCacheCommand rcc1 = new RequestCacheCommand(2);
		RequestCacheCommand rcc2 = new RequestCacheCommand(2);
		try{
			String result = rcc1.execute();
			boolean fromCache = rcc1.isResponseFromCache();
			System.out.println("rcc1:result="+result+",fromCache="+fromCache);
			String result2 = rcc2.execute();
			boolean fromCache2 = rcc2.isResponseFromCache();
			System.out.println("rcc2:result="+result2+",fromCache="+fromCache2);
		}finally{
			context.shutdown();
		}
		context = HystrixRequestContext.initializeContext();  
		try{
			RequestCacheCommand rcc3 = new RequestCacheCommand(2);
			String result3 = rcc3.execute();
			boolean fromCache3 = rcc3.isResponseFromCache();
			System.out.println("rcc1:result="+result3+",fromCache="+fromCache3);
		}finally{
			context.shutdown();
		}
	}

}
