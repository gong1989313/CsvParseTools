package com.person.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.person.dao.JdbcDao;
import com.person.enums.StatusTypeEnum;
import com.person.utils.JdbcUtil;
import com.person.utils.Utils;

public class ParseCsvService {
	private PreparedStatement prest = null;

	private Connection conn = null;

	private BufferedReader br = null;

	private JdbcDao jdbcDao = null;

	public ParseCsvService(String path) throws IOException {
		br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
	}

	public void parseCSVFile(int batchNum, int fieldNum, String sql) {
		jdbcDao = new JdbcDao();
		int count = 0, size, index = 1;
		// open batch jdbc
		try {
			conn = JdbcUtil.getConnection();
			prest = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String[] parms = new String[fieldNum - 1];
		String[] bakParms = null;
		size = parms.length - 1;
		StringBuilder temp = new StringBuilder();
		StringBuilder quotesFieldBak = new StringBuilder();
		StatusTypeEnum status = StatusTypeEnum.NewFieldStart;
		int ch = 0, pos = -1;
		boolean endFlag = false, quotesText = false, isQutesFlag = false;
		try {
			while ((ch = br.read()) != -1) {
				if (ch == ',') {
					if (status == StatusTypeEnum.QuotesField) {
						temp.append((char) ch);
					} else {
						if (status == StatusTypeEnum.NewFieldStart) {
							parms[count] = temp.toString();
							temp.delete(0, temp.length());
							count++;
						}
						if (status == StatusTypeEnum.NonQuotesField) {
							parms[count] = temp.toString();
							temp.delete(0, temp.length());
							count++;
							status = StatusTypeEnum.NewFieldStart;
						}
					}
				} else {
					if (ch == '\"' || ch == '\'') {
						if (status == StatusTypeEnum.QuotesField) {
							temp.append((char) ch);
							status = StatusTypeEnum.NonQuotesField;
							quotesText = true;
							continue;
						}
						if (status == StatusTypeEnum.NewFieldStart) {
							status = StatusTypeEnum.QuotesField;
							if (!isQutesFlag) {
								// bakup parms
								bakParms = Utils.bakArray(parms, count);
								pos = count;
							}
							isQutesFlag = true;
						}
						if (status == StatusTypeEnum.NonQuotesField) {
							temp.append((char) ch);
							if (quotesText) {
								status = StatusTypeEnum.NonQuotesField;
								quotesText = false;
							} else {
								status = StatusTypeEnum.QuotesField;
							}
						}
					}
					if (ch == '\r' || ch == '\n') {
						if (count == size) {
							parms[count] = temp.toString();
							temp.delete(0, temp.length());
							quotesFieldBak.delete(0, quotesFieldBak.length());
							isQutesFlag = false;
							pos = -1;
							count++;
						}
						if (isQutesFlag && count < size) {
							parms = Utils.bakQuotesFieldProcess(parms, pos, quotesFieldBak);
							quotesFieldBak.delete(0, quotesFieldBak.length());
							isQutesFlag = false;
							pos = -1;
							count = size + 1;
						}
					}
					if (count == size + 1) {
						if (isQutesFlag && ch != '\r') {
							while ((ch = br.read()) != '\r') {
								quotesFieldBak.append((char) ch);
							}
							parms = Utils.specialCharProcess(parms, bakParms, pos, quotesFieldBak);
							quotesFieldBak.delete(0, quotesFieldBak.length());
							isQutesFlag = false;
							pos = -1;
						}
						conn = jdbcDao.getBatchProcessConn(conn);
						prest = jdbcDao.getBatchPS(conn, prest, sql);
						jdbcDao.updateData(conn, prest, index, batchNum, sql, parms);
						temp.delete(0, temp.length());
						count = 0;
						status = StatusTypeEnum.NewFieldStart;
						index++;
					}
					if (status == StatusTypeEnum.NewFieldStart) {
						if (ch == '\r' || ch == '\n') {
							continue;
						}
						temp.append((char) ch);
					}
					if (status == StatusTypeEnum.QuotesField) {
						temp.append((char) ch);
					}
				}
				if (isQutesFlag) {
					quotesFieldBak.append((char) ch);
				}
			}
			endFlag = true;
			conn = jdbcDao.getBatchProcessConn(conn);
			prest = jdbcDao.getBatchPS(conn, prest, sql);
			jdbcDao.endUpdateData(conn, prest, endFlag);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
