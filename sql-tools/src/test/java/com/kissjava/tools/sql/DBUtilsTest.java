package com.kissjava.tools.sql;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.kissjava.tools.sql.utils.DBUtils;
import com.kissjava.tools.sql.utils.callbak.ReplaceCallBack;
import com.kissjava.tools.sql.utils.callbak.TokenReplaceCallBack;

public class DBUtilsTest {
	private String url = "jdbc:mysql://10.0.53.19:3306/openapi20161022?allowMultiQueries=true&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true";
	private String user = "root";
	private String password = "1qaz@WSX";

	@Before
	public void init() {
		DBUtils.url = url;
		DBUtils.user = user;
		DBUtils.password = password;
	}

	@Test
	@Ignore
	public void tableNames() {
		System.out.println("tableNames:"+DBUtils.instance().tableNames());
	}
	@Test
	public void nullDump() {
		System.out.println("nullDump" + DBUtils.instance().dump("api_interface_comm_param"));
	}
	@Test
	public void tableReplaceDump(){
		ReplaceCallBack callBack = new ReplaceCallBack("target_url");
		Map<String, String> map = new HashMap<String, String>();
		map.put("https://www.10.0.3.1:80.com/", "https://wwww.google.com/");
		callBack.setRepalceMap(map);
		
		String ss = DBUtils.instance().dump("api_interface",callBack);
		System.out.println(ss);
	}
	
	@Test
	public void tableTokenReplace(){
		String ss = DBUtils.instance().dump("api_app",
				new TokenReplaceCallBack("app_secret")
				,new TokenReplaceCallBack("token")
				,new TokenReplaceCallBack("sandbox_app_secret")
				,new TokenReplaceCallBack("sandbox_token"));
		System.out.println(ss);	
	}
}
