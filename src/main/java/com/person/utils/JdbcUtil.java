package com.person.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 封装重用代码 获取Jdbc连接，释放资源
 * 
 * @author Administrator
 *
 */
public class JdbcUtil {
	static Connection conn = null;

	static {
		try {
			// 加载驱动
			Class.forName(ConfigUtil.getDriver());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			String url = ConfigUtil.getUrl();
			String user = ConfigUtil.getUser();
			String pwd = ConfigUtil.getPwd();
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void release(Object o) {
		if (o == null) {
			return;
		}
		if (o instanceof ResultSet) {
			try {
				((ResultSet) o).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (o instanceof Statement) {
			try {
				((Statement) o).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (o instanceof Connection) {
			Connection c = (Connection) o;
			try {
				if (!c.isClosed()) {
					c.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void release(ResultSet rs, Statement stmt, Connection conn) {
		release(rs);
		release(stmt);
		release(conn);
	}

	public static String produceSQL(String sqlType, String tableName, List<String> columnList) {
		StringBuilder columnBuilder = new StringBuilder(sqlType + " " + tableName);
		columnBuilder.append("(");
		StringBuilder valuesBuilder = new StringBuilder(" VALUES(");
		int i = 1;
		for (; i < columnList.size() - 1; i++) {
			columnBuilder.append(columnList.get(i) + ",");
			valuesBuilder.append("?, ");
		}
		columnBuilder.append(columnList.get(i) + ")");
		valuesBuilder.append("?)");
		columnBuilder.append(valuesBuilder);
		return columnBuilder.toString();
	}
}
