package com.kissjava.tools.sql.vo;

import java.util.*;


public class DumpRule {
	private String[] tableNames;
	private String[] colNames;
	
	public String[] getTableNames() {
		return tableNames;
	}

	public void setTableNames(String[] tableNames) {
		this.tableNames = tableNames;
	}

	public String[] getColNames() {
		return colNames;
	}

	public void setColNames(String[] colNames) {
		this.colNames = colNames;
	}

	private Map<String, String> ruleMap = new HashMap<String, String>();
	
	
//	
	public Map<String, String> getRuleMap() {
		return ruleMap;
	}
	
	public void setRuleMap(Map<String, String> ruleMap) {
		this.ruleMap = ruleMap;
	}
	
	public void addRule(String key, String value){
		ruleMap.put(key, value);
	}
}
