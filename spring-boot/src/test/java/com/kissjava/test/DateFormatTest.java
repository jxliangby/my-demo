package com.kissjava.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateFormatTest {
	public static final String DATE_TIME_PATTON_4 = "yyyy-MM-dd HH:mm:ss:SSS";
	static SimpleDateFormat date_time_patton_4_format = new SimpleDateFormat(DATE_TIME_PATTON_4);
	
	
	public static void mutiTest(){
		ExecutorService execturor = Executors.newFixedThreadPool(10);
		for(int index=0,size=100; index<size; index++){
			execturor.execute(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						String sss = date_time_patton_4_format.format(new Date());
						System.out.println(Thread.currentThread().getName()+"----"+sss);
						try {
							Date date = date_time_patton_4_format.parse(sss);
							System.out.println(Thread.currentThread().getName()+"----"+date.getTime());
						} catch (ParseException e) {
							System.exit(1);
						}
						
					}
				}
			});
		}
	}
	public static void main(String[] args) {
		mutiTest();
		
	}

}
