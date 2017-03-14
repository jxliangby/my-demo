package com.kissjava.test.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kissjava.test.web.vo.A;

@Controller
@RequestMapping(value="/home")
public class TestController {
	
	@RequestMapping(value = "/head")
    public void printHead(HttpServletRequest request, HttpServletResponse response) {
			Enumeration<String> headerNames = request.getHeaderNames();
			while(headerNames.hasMoreElements()){
				String headName = headerNames.nextElement();
				String headValue = request.getHeader(headName);
				System.out.println("name:"+headName+", value="+headValue);
			}
			
			System.out.println("ip:" + getClientIpAddr(request));
    }
	
	@RequestMapping(value = "/test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
		String path = "d:\\goods.txt";
		try {
			String requestData = FileUtils.readFileToString(new File(path));
			response.getWriter().print(requestData);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	@RequestMapping(value="/post",method={RequestMethod.POST})
	public void post(A a,HttpServletRequest request, HttpServletResponse response){
		System.out.println(a);
		try {
			response.getWriter().print("ok!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public final static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
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
}
