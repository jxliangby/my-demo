package com.kissjava.test.web.vo;

import java.util.List;

public class A {
	private String name;
	
	private B b;
	private List<String> c;
	
	public List<String> getC() {
		return c;
	}
	public void setC(List<String> c) {
		this.c = c;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public B getB() {
		return b;
	}
	public void setB(B b) {
		this.b = b;
	}
}
