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
 * Created on 2016年12月22日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.statement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import com.bosssoft.platform.es.jdbc.constructer.QueryConstructer;
import com.bosssoft.platform.es.jdbc.constructer.QueryConstructerImpl;
import com.bosssoft.platform.es.jdbc.constructer.SearchConverter;
import com.bosssoft.platform.es.jdbc.constructer.SearchConverterImpl;
import com.bosssoft.platform.es.jdbc.constructer.UpdateConstructer;
import com.bosssoft.platform.es.jdbc.constructer.UpdateConstructerImpl;
import com.bosssoft.platform.es.jdbc.director.QueryDirector;
import com.bosssoft.platform.es.jdbc.director.SelectObjDirector;
import com.bosssoft.platform.es.jdbc.director.UpdateDirector;
import com.bosssoft.platform.es.jdbc.driver.ESClient;
import com.bosssoft.platform.es.jdbc.driver.ESConnection;
import com.bosssoft.platform.es.jdbc.model.QueryBody;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;
import com.facebook.presto.sql.parser.SqlParser;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ESStatement implements Statement{
  
	private static final SqlParser sqlparser = new SqlParser();
	
	private ESConnection connection;
	
	private SelectObjDirector selectObjDirector;
	
	private QueryDirector queryDirector;
	
	private UpdateDirector updateDirector;
	
	
	public ESStatement(ESConnection connection){
		this.connection=connection;
		
		SearchConverter searchConverter=new SearchConverterImpl();
		selectObjDirector=new SelectObjDirector(searchConverter);
		
		QueryConstructer queryBuilder=new QueryConstructerImpl();
		queryDirector=new QueryDirector(queryBuilder);
		
		UpdateConstructer updateBuilder=new UpdateConstructerImpl();
		updateDirector=new UpdateDirector(updateBuilder);
	}
	
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		sql=sql.trim().toLowerCase();
		
		//暂时去除分页的语句
		String formatSql=hashLimit(sql);
		com.facebook.presto.sql.tree.Statement statement = sqlparser.createStatement(formatSql);
		
		//封装查询信息
		SelectSqlObj sqlObj=selectObjDirector.convent(statement,sql);
		
		//构建es的查询体
		QueryBody queryBody=queryDirector.constructQuery(sqlObj);
		
		//调用ESClinet对es查询
		ESClient esClient=connection.getEsClient();
		esClient.search(queryBody.getQueryBuilder(), queryBody.getAggregationBuilder(), connection.getIndex(), sqlObj.getFrom());
		return null;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	@Override
	public int executeUpdate(String sql) throws SQLException {
		sql=sql.replace(" ", "");
		String formatSql=hashLimit(sql);
		return 0;
	}

	//sql语句的分页解析
	private String  hashLimit (String sql) {
			if(sql.contains("limit")){
				sql=sql.substring(0, sql.indexOf("limit"));
			}
			return sql;
		}
	

	public ESConnection getConnection() {
		return connection;
	}

	public void setConnection(ESConnection connection) {
		this.connection = connection;
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	@Override
	public void addBatch(String sql) throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#cancel()
	 */
	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#clearBatch()
	 */
	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#close()
	 */
	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#closeOnCompletion()
	 */
	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	@Override
	public boolean execute(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	@Override
	public boolean execute(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#executeBatch()
	 */
	@Override
	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}




	



	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
	 */
	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getMaxRows()
	 */
	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults()
	 */
	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSet()
	 */
	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetType()
	 */
	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getUpdateCount()
	 */
	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#isCloseOnCompletion()
	 */
	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#isPoolable()
	 */
	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	@Override
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	
	

}

/*
 * 修改历史
 * $Log$ 
 */