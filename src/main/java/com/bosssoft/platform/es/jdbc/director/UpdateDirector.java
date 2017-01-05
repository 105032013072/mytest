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
import java.util.ArrayList;
import java.util.List;

import com.bosssoft.platform.es.jdbc.constructer.UpdateConstructer;
import com.bosssoft.platform.es.jdbc.driver.ESClient;
import com.bosssoft.platform.es.jdbc.driver.ESConnection;
import com.bosssoft.platform.es.jdbc.mate.BatchUpdateObj;
import com.bosssoft.platform.es.jdbc.model.DeleteSqlObj;
import com.bosssoft.platform.es.jdbc.model.InsertSqlObj;
import com.bosssoft.platform.es.jdbc.model.UpdateSqlObj;
import com.facebook.presto.sql.parser.SqlParser;

/**
 * TODO es的更新体的构建指导类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class UpdateDirector {

	private UpdateConstructer builder;
	
	private List<String> updateList;
	
	private List<String> insertList;
	
	private List<String> deleteList;
	

	
	public UpdateDirector(UpdateConstructer builder){
		this.builder=builder;
		updateList=new ArrayList<>();
		insertList=new ArrayList<>();
		deleteList=new ArrayList<>();
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
	public UpdateSqlObj  buildUpdate(String sql,String index,Statement esStatement) throws SQLException{
	 
		//封装更新信息
		UpdateSqlObj updateSqlObj=builder.buildUpdateObj(sql, index, esStatement);
		return updateSqlObj;
		
	}
	
	/**
	 * 插入数据
	 * @param statement
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public InsertSqlObj buildInsert(String sql) throws SQLException{
		com.facebook.presto.sql.tree.Statement statement = new SqlParser().createStatement(sql);
		return builder.buildInsertObj(statement);
		
	}
	
	/**
	 * 删除数据
	 * @param sql
	 * @param esStatement
	 * @return
	 * @throws SQLException
	 */
	public DeleteSqlObj buildDelete(String sql,Statement esStatement) throws SQLException{
		
		return builder.builddeleteObj(sql, esStatement);
		 
	}
	
	public BatchUpdateObj buildBatch(Statement esStatement,String index) throws SQLException{
		BatchUpdateObj batchObj=new BatchUpdateObj();
		//更新
		for (String sql : updateList) {
			batchObj.addUpObj(buildUpdate(sql, index, esStatement));
		}
		
		//插入
		for (String sql : insertList) {
			batchObj.addInObj(buildInsert(sql));
		}
		
		//删除
		for (String sql : deleteList) {
			batchObj.addDeObj(buildDelete(sql, esStatement));
		}
		return batchObj;
	}
	
	public void addupdate(String sql){
		updateList.add(sql);
	}
	
	public void addinsert(String sql){
		insertList.add(sql);
	}
	
	public void adddelete(String sql){
		deleteList.add(sql);
	}
}

/*
 * 修改历史
 * $Log$ 
 */