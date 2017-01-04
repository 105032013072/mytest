/*******************************************************************************
 * $Header$
 * $Revision$
 * $Date$
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2016 Bosssoft Co, Ltd.
 * All rights reserved.
 * 
 * Created on 2016年12月25日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.director;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Statement;

import com.bosssoft.platform.es.jdbc.constructer.UpdateConstructer;
import com.bosssoft.platform.es.jdbc.driver.ESClient;
import com.bosssoft.platform.es.jdbc.driver.ESConnection;
import com.bosssoft.platform.es.jdbc.model.UpdateSqlObj;

/**
 * TODO es的更新体的构建指导类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class UpdateDirector {

	private UpdateConstructer builder;
	
	public UpdateDirector(UpdateConstructer builder){
		this.builder=builder;
	}
	
   /**
    * 创建表
    * @param table
    * @param index
    * @return
    */
	public String buildCreate(String table,String index) {
		try {
			return builder.buildCreate(table, index);
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 修改表数据
	 * @param sql
	 * @param index
	 * @throws SQLException 
	 */
	public  void buildUpdate(String sql,String index,Statement esStatement) throws SQLException{
	 
		//封装更新信息
		UpdateSqlObj updateSqlObj=builder.buildUpdateObj(sql, index, esStatement);
		
		//调用es客户端
		ESConnection con=(ESConnection) esStatement.getConnection();
		ESClient esClient=con.getEsClient();
		for (String id : updateSqlObj.getIds()) {
			esClient.updateDoc(index, updateSqlObj.getType(), updateSqlObj.getUpdateList(), id);
		}
	}
}

/*
 * 修改历史
 * $Log$ 
 */