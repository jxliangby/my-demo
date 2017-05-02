package com.kissjava.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
	
	public static String readFile2(String path) throws IOException {
		StringBuilder sb = new StringBuilder(300);
		BufferedReader input = null;
		try {
			InputStream ips = FileUtils.class.getResourceAsStream(path);
			input = new BufferedReader(new InputStreamReader(ips));
			String str = null;
			while ((str = input.readLine()) != null) {
				sb.append(str);
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return sb.toString();
	}
}
