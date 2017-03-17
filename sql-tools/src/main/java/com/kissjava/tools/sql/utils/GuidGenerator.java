package com.kissjava.tools.sql.utils;

import java.util.UUID;

public class GuidGenerator {
	
	private GuidGenerator() {
	}

	public static String generate() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
