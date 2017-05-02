package com.kissjava.test.boot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.kissjava.util.FileUtils;

@RestController
@SpringBootApplication
public class Bootstrap {
	
	private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "user id =" + counter.incrementAndGet() + String.format(template, name);
	}
	
	@RequestMapping("/15s")
	public void s15(HttpServletRequest request, HttpServletResponse response){
		try{
			Thread.sleep(15000);
		}catch(Exception e){
			
		}
		openApi("15s", request, response);
	}
	
	@RequestMapping("/10s")
	public void s10(HttpServletRequest request, HttpServletResponse response){
		try{
			Thread.sleep(10000);
		}catch(Exception e){
			
		}
		openApi("10s", request, response);
	}
	
	@RequestMapping("/5s")
	public void s5(HttpServletRequest request, HttpServletResponse response){
		try{
			Thread.sleep(5000);
		}catch(Exception e){
			
		}
		openApi("5s", request, response);
	}
	
	@RequestMapping(value = "/big")
	public void big(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			 String msg  = "this is valid json msg";//FileUtils.readFile2("/post.txt");
			// openApi(msg, request, response);
			 validmsg(msg, request, response);
		} catch (Exception e1) {
		}
	}
	@RequestMapping(value = "/normal")
	public void printHead2(HttpServletRequest request, HttpServletResponse response) {
		JSONObject msg = new JSONObject();
		msg.put("id", RandomStringUtils.randomAlphanumeric(10));
		msg.put("name", RandomStringUtils.randomAlphabetic(10));
		openApi(msg.toJSONString(), request, response);
	}
	
	private void encoding(HttpServletResponse response){
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8"); 
	}
	
	private void validmsg(String msg, HttpServletRequest request, HttpServletResponse response){
		encoding(response); 
		try {
			response.getWriter().println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void openApi(String msg , HttpServletRequest request, HttpServletResponse response){
		
		encoding(response); 
		
		JSONObject root = new JSONObject();
		JSONObject respJson= new JSONObject();
		root.put("RESPONSE", respJson);
		respJson.put("RETURN_CODE", "B000001");
		respJson.put("RETURN_DESC", "成功");
		respJson.put("RETURN_STAMP", now());
		JSONObject  data = new JSONObject();
		respJson.put("RETURN_DATA", data);
		
		data.put("msg", msg);
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headName = headerNames.nextElement();
			String headValue = request.getHeader(headName);
			data.put(headName, headValue);
		}
		data.put("ip", getClientIpAddr(request));
		String jsonstr = root.toJSONString();
		try {
			response.getWriter().println(jsonstr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("json:" + jsonstr);
	}
	private static String now(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
		return sdf.format(new Date());
	}
	public final static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	public static void main(String[] args) {
		logger.error("Message logged at ERROR level");
		SpringApplication.run(Bootstrap.class, args);
		logger.warn("Message logged at WARN level");
	    logger.info("Message logged at INFO level");
	    logger.debug("Message logged at DEBUG level");
	}
}
