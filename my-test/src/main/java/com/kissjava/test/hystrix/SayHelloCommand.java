package com.kissjava.test.hystrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

public class SayHelloCommand extends HystrixCommand<String>{

	private final String _name;
	
	public SayHelloCommand(String name){
		super(HystrixCommandGroupKey.Factory.asKey("HelloService"));
		_name = new String(name);//unmutable
		
	}
	@Override
	protected String run() throws Exception {
		return String.format("Hello %s!", _name) + Thread.currentThread().getName();
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException{
		SayHelloCommand helloCommand = new SayHelloCommand("sys-hystrix");
		String result = helloCommand.execute();
		System.out.println("result = " + result);
		////////////
		helloCommand = new SayHelloCommand("asy-hystrix");
		Future<String> future = helloCommand.queue();
		result = future.get(1000, TimeUnit.MILLISECONDS);
		System.out.println("result = " + result);
		///////////////////
		Observable<String> fs = new SayHelloCommand("observable").observe();
		fs.subscribe(new Action1<String>() {
			public void call(String result){
				System.out.println("hello subscribe in action1 call:"+result);
			}
		});
		fs.subscribe(new Observer<String>() {

			@Override
			public void onCompleted() {
				System.out.println("do completed.........");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("do error.........");
			}

			@Override
			public void onNext(String t) {
				System.out.println("do next.........");
			}
		});
	}
}
