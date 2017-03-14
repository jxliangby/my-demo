package com.kissjava.test.hystrix;

import java.util.concurrent.TimeUnit;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class HelloFallbackCommand extends HystrixCommand<String>{
	
	private String name;
	
	public HelloFallbackCommand(String name) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("helloFallbak"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionIsolationThreadTimeoutInMilliseconds(500)));
		this.name = name;
	}
	
	@Override
	protected String getFallback(){
		Throwable e = getFailedExecutionException();
		System.out.println("e:" + e);
		return "exectued fall";
	}
	
	@Override
	protected String run() throws Exception {  
		TimeUnit.MILLISECONDS.sleep(1000); 
		return "Hello " + name +" thread:" + Thread.currentThread().getName();  
	}
	
	
	public static void main(String[] args) {
		HelloFallbackCommand command = new HelloFallbackCommand("test-Fallback");  
		String result = command.execute();
		System.out.println("result = " + result);
	}

}
