package com.kissjava.tools.sql.vo;

public class SelectVo {
	
	private String name;
	
	private boolean selected;
	public SelectVo(){
		
	}
	public SelectVo(String name, boolean selected){
		this.name = name;
		this.selected = selected;
	}
	public String getName() {
		return name;
	}

	public void setName(String tableName) {
		this.name = tableName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
