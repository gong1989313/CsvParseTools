package com.person.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.person.utils.JdbcUtil;

public class JdbcDao {
	private static Connection conn = null;
	private static PreparedStatement prest = null;
	private static ResultSet rs = null;
	private int batchNumber = 0;

	// get database table columns
	public List<String> getTableColumn(String tableName) {
		List<String> columnList = new ArrayList<String>();
		conn = JdbcUtil.getConnection();
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			//rs = metadata.getTables(null, null, tableName, null);
			rs = metaData.getTables(conn.getCatalog(), "SCOTT", null, new String[]{"TABLE"});
			boolean isExist = false;
			while (rs.next()) {
				isExist = true;
			}
			if (isExist) {
				prest = conn.prepareStatement("select * from " + tableName + " where 1=2");
				ResultSetMetaData rsd = prest.executeQuery().getMetaData();
				int column = rsd.getColumnCount();
				for(int i=1; i<=column; i++){
					columnList.add(rsd.getColumnLabel(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.release(rs);
			JdbcUtil.release(prest);
			JdbcUtil.release(conn);
		}
		return columnList;
	}
	
	public void updateData(Connection conn, PreparedStatement prest, int index, int batchNum, String sql, String[] parms){
		try {
			for(int i=1; i<=parms.length; i++){
				prest.setString(i, parms[i-1]);
			}
			prest.addBatch();
			if(index % batchNum == 0){
				prest.executeBatch();
				prest.clearBatch();
				conn.commit();
				JdbcUtil.release(prest);
				JdbcUtil.release(conn);
				batchNumber++;
				System.out.println("Thread: "+Thread.currentThread().getName()+" excute "+batchNumber+" jdbc batch submit.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void endUpdateData(Connection conn, PreparedStatement prest, boolean endFlag){
		if (endFlag) {
			try {
				prest.executeBatch();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				JdbcUtil.release(prest);
				JdbcUtil.release(conn);
				batchNumber++;
				System.out.println("Thread: "+Thread.currentThread().getName()+" excute last jdbc batch submit.");
			}
		}
	}
	
	public Connection getBatchProcessConn(Connection conn){
		try {
			if(conn == null || conn.isClosed()){
				conn = JdbcUtil.getConnection();
				conn.setAutoCommit(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public PreparedStatement getBatchPS(Connection conn, PreparedStatement prest, String sql){
		try {
			if(prest == null || prest.isClosed()){
				prest = conn.prepareStatement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prest;
	}
}