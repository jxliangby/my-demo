package com.kissjava;

public class AppTest {
	private static void err(){
		throw new RuntimeException("haha err");
	}
    public static void main(String[] args){
    	try{
    		err();
    	}catch(Exception e){
    		System.out.println(String.format("server is other error,errorType[%s] ,errorInfo[%s]", new String[]{e.getClass().getName(), e.getMessage()}));
    		
    	}
    }
}
