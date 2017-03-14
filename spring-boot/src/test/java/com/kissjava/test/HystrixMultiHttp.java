package com.kissjava.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

public class HystrixMultiHttp {
	private final String[] URLS = new String[]{
			"http://localhost:8080/product",
			"http://localhost:8080/order",
			"http://localhost:8080/cart",
			"http://localhost:8080/google",
			"http://localhost:8080/observe",
			"http://localhost:8080/future"
	};
	@Test
	public void test(){
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(15);
		
		final Runnable r = new Runnable() {
			
			@Override
			public void run() {
				int index = RandomUtils.nextInt(6);
				final String url = URLS[index];
				executor.execute(new Runnable() {
					
					@Override
					public void run() {
						System.out.println("##########################################################");
						System.out.println("RUL:"+url);
						System.out.println("RESULT:"+doGet(url));
					}
				});
			}
		};
		executor.scheduleWithFixedDelay(r, 0, 10L,TimeUnit.MILLISECONDS);
		executor.scheduleWithFixedDelay(r, 100, 10L,TimeUnit.MILLISECONDS);
		executor.scheduleWithFixedDelay(r, 1000, 10L,TimeUnit.MILLISECONDS);
		try{
			Thread.sleep(10000000);
		}catch(Exception e){}
	}
	
	private String doGet(String url){
		  CloseableHttpClient httpclient = HttpClients.createDefault();
	        HttpGet httpGet = new HttpGet(url);
	        try(CloseableHttpResponse response = httpclient.execute(httpGet)) {
	            HttpEntity entity = (HttpEntity) response.getEntity();
	            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	            String total = "";
	            String line = bufferedReader.readLine();
	            while (line != null){
	                total += line;
	                line = bufferedReader.readLine();
	            }
	            return total;
	        }catch(Exception e){
	        	return "null";
	        }
	}
}
