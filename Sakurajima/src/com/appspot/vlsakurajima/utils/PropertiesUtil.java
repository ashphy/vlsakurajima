package com.appspot.vlsakurajima.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties getProperiesFromResource(String filename) {
		Properties properties = new Properties();
		InputStream in = null;
		try{
			in = PropertiesUtil.class.getResourceAsStream(filename);
			properties.load(in);
		} catch (IOException e) {
			return null;
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return properties;
	}
	
}
