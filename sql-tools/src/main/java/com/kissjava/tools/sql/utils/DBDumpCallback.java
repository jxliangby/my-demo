package com.kissjava.tools.sql.utils;

public interface DBDumpCallback {
	/***
	 * 
	 * @param columnName
	 * @param value
	 */
	Object exce(String columnName, Object value);
}
