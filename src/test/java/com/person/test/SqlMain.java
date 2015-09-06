package com.person.test;

import java.util.List;

import com.person.dao.JdbcDao;
import com.person.utils.ConfigUtil;
import com.person.utils.JdbcUtil;

public class SqlMain {

	public static void main(String[] args) {
		JdbcDao jdbcSerivce = new JdbcDao();
		String tableName = ConfigUtil.getTableName();
		List<String> columnList = jdbcSerivce.getTableColumn(tableName);
		String sql = JdbcUtil.produceSQL("INSERT INTO", tableName, columnList);
		System.out.println(sql);
	}

}
