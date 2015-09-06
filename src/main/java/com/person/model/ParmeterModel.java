package com.person.model;

public class ParmeterModel {
	private int batchNum;
	private int fieldNum;
	private String sql;
	private String baseFilePath;
	public int getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}
	public int getFieldNum() {
		return fieldNum;
	}
	public void setFieldNum(int fieldNum) {
		this.fieldNum = fieldNum;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getBaseFilePath() {
		return baseFilePath;
	}
	public void setBaseFilePath(String baseFilePath) {
		this.baseFilePath = baseFilePath;
	}
}
