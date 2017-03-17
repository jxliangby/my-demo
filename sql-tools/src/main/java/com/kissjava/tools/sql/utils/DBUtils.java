package com.kissjava.tools.sql.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {
	public static String url = null;
	public static String user = null;
	public static String password = null;

	private static class DBUtilsHolder {
		static DBUtils instance = new DBUtils();
	}

	public static DBUtils instance() {
		return DBUtilsHolder.instance;
	}

	private DBUtils() {
		if (url == null || user == null || password == null) {
			throw new RuntimeException("init db properties");
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("找不到驱动程序类 ，加载驱动失败！");
		}
	}

	public Connection getConn() {
		try {
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (SQLException se) {
			throw new RuntimeException("数据库连接失败！");
		}
	}

	public List<String> tableNames() {
		List<String> tbNames = new ArrayList<String>();
		Connection conn = DBUtils.instance().getConn();
		try {
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				tbNames.add(rs.getString(3));
			}
			conn.close();
		} catch (Exception e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return tbNames;
	}

	public String dump(String tableName, DBDumpCallback ...callbacks) {
		Connection conn = DBUtils.instance().getConn();
		StringBuilder sb = new StringBuilder(10000);
		try {
			PreparedStatement ps = conn.prepareStatement("select * from " + tableName);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsme = rs.getMetaData();
			sb.append("insert into ").append(tableName).append(" (");
			Map<String, String> columnMetaMap = new HashMap<String, String>();
			int columnCount = rsme.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnMetaMap.put(rsme.getColumnName(i), rsme.getColumnTypeName(i));
				sb.append(rsme.getColumnName(i));
				if(i<columnCount){
					sb.append(",");
				}
			}
			sb.append(") values ") ;
			int cnt = 0;
			while(rs.next()){
				cnt++;
				sb.append("(");
				for (int i = 1; i <= columnCount; i++) {
					String columnName = rsme.getColumnName(i);
					Object obj = rs.getString(i);
					if(callbacks!=null){
						for(DBDumpCallback callback:callbacks){
							obj = callback.exce(columnName, obj);
						}
					}
					
					if(obj==null){
						sb.append("null");
					}else{
						sb.append("'").append(obj).append("'");
					}
					if(i<columnCount){
						sb.append(",");
					}
				}
				sb.append(")");
				if(!rs.isLast()){
					sb.append(",");
				}
			}
			if(cnt==0){
				return null;
			}
			sb.append(";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	//class Colum
}
