package com.kissjava.tools.sql.utils.callbak;

import java.util.Map;

public class ReplaceCallBack extends AbstractDBReplaceCallBack {
	
	
	public ReplaceCallBack(String repaceCloumnName ){
		super(repaceCloumnName);
	}
	/***
	 * key是原始
	 */
	private Map<String, String> repalceMap;
	
	

	public Map<String, String> getRepalceMap() {
		return repalceMap;
	}

	public void setRepalceMap(Map<String, String> repalceMap) {
		this.repalceMap = repalceMap;
	}

	@Override
	public Object exce(String columnName, Object value) {
		if(!cloumnNameEquals(columnName) || value==null || repalceMap==null){
			return value;
		}
		String toRepalce = value.toString();
		for(Map.Entry<String, String> entry:repalceMap.entrySet()){
			String regex = entry.getKey();
			String replacement = entry.getValue();
			if(toRepalce.startsWith(regex)){
				toRepalce = toRepalce.replaceAll(regex, replacement);
				break;
			}
		}
		return toRepalce;
	}

}
