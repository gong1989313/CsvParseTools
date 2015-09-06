package com.person.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.person.dao.JdbcDao;
import com.person.model.ParmeterModel;
import com.person.utils.ConfigUtil;
import com.person.utils.JdbcUtil;

public class PreparedService {
	private JdbcDao jdbcDao = null;
	
	public ParmeterModel getPreparedParmeter(){
		String tableName = ConfigUtil.getTableName();
		jdbcDao = new JdbcDao();
		List<String> columnList = jdbcDao.getTableColumn(tableName);
		int fieldNum = columnList.size();
		String strBatchNum = ConfigUtil.getBatchNum();
		int batchNum;
		if(StringUtils.isNumeric(strBatchNum)){
			batchNum = Integer.parseInt(strBatchNum);
		}else{
			batchNum = 100;
		}
		jdbcDao = new JdbcDao();
		String sql = JdbcUtil.produceSQL("INSERT INTO", tableName, columnList);
		ParmeterModel pm = new ParmeterModel();
		pm.setBatchNum(batchNum);
		pm.setFieldNum(fieldNum);
		pm.setSql(sql);
		pm.setBaseFilePath(ConfigUtil.getFilePath());
		return pm;
	}
}
