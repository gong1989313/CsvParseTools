package com.person.utils;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {
	private static Properties p = new Properties();
	// ÓÃ¾²Ì¬´úÂë¿é
	static {
		try {
			p.load(ClassLoader.getSystemResourceAsStream("config_oracle.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDriver() {
		return p.getProperty("database.driver");
	}

	public static String getUrl() {
		return p.getProperty("database.url");
	}

	public static String getUser() {
		return p.getProperty("database.user");
	}

	public static String getPwd() {
		return p.getProperty("database.password");
	}
	
	public static String getFilePath(){
		return p.getProperty("sysconfig.filePath");
	}
	
	public static String getTableName(){
		return p.getProperty("databse.tableName");
	}
	
	public static String getSeparator(){
		return p.getProperty("sysconfig.separator");
	}
	
	public static String getBatchNum(){
		return p.getProperty("sysconfig.batchNum");
	}
}
