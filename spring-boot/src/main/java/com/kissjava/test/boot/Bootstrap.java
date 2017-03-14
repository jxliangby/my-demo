package com.kissjava.test.boot;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

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

	@RequestMapping(value = "/head")
	public void printHead(HttpServletRequest request, HttpServletResponse response) {
		JSONObject root = new JSONObject();
		JSONObject respJson= new JSONObject();
		root.put("RESPONSE", respJson);
		respJson.put("RETURN_CODE", "B000001");
		respJson.put("RETURN_DESC", "成功");
		respJson.put("RETURN_STAMP", "2017-03-02 09:43:34:228");
		JSONObject  data = new JSONObject();
		respJson.put("RETURN_DATA", data);
		
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
		System.out.println("json:" + jsonstr);
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
