package com.kissjava.tools.sql.utils.callbak;

import com.kissjava.tools.sql.utils.GuidGenerator;

public class TokenReplaceCallBack extends AbstractDBReplaceCallBack {
	
	public TokenReplaceCallBack(String repaceCloumnName ){
		super(repaceCloumnName);
	}
	@Override
	public Object exce(String columnName, Object value) {
		if(!cloumnNameEquals(columnName) || value==null){
			return value;
		}
		
		return GuidGenerator.generate();
	}

}
