package com.kissjava.tools.sql.utils.callbak;

import com.kissjava.tools.sql.utils.DBDumpCallback;

public abstract class AbstractDBReplaceCallBack implements DBDumpCallback {
	/**
	 * 需要替换的columnName
	 */
	protected String repaceCloumnName;
	
	public AbstractDBReplaceCallBack(String repaceCloumnName){
		this.repaceCloumnName = repaceCloumnName;
	}
	

	
	protected boolean cloumnNameEquals(String columnName){
		return repaceCloumnName.equals(columnName) ;
	}
}
