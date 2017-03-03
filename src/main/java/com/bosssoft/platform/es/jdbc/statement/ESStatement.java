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

import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;

import com.bosssoft.platform.es.jdbc.constructer.QueryConstructer;
import com.bosssoft.platform.es.jdbc.constructer.QueryConstructerImpl;
import com.bosssoft.platform.es.jdbc.constructer.ResultSetConstructer;
import com.bosssoft.platform.es.jdbc.constructer.ResultSetConstructerImpl;
import com.bosssoft.platform.es.jdbc.constructer.SearchConverter;
import com.bosssoft.platform.es.jdbc.constructer.SearchConverterImpl;
import com.bosssoft.platform.es.jdbc.constructer.UpdateConstructer;
import com.bosssoft.platform.es.jdbc.constructer.UpdateConstructerImpl;
import com.bosssoft.platform.es.jdbc.director.QueryDirector;
import com.bosssoft.platform.es.jdbc.director.ResultSetDirector;
import com.bosssoft.platform.es.jdbc.director.SelectObjDirector;
import com.bosssoft.platform.es.jdbc.director.UpdateDirector;
import com.bosssoft.platform.es.jdbc.driver.ESClient;
import com.bosssoft.platform.es.jdbc.driver.ESConnection;
import com.bosssoft.platform.es.jdbc.mate.BatchUpdateObj;
import com.bosssoft.platform.es.jdbc.mate.InExpression;
import com.bosssoft.platform.es.jdbc.model.DeleteSqlObj;
import com.bosssoft.platform.es.jdbc.model.ESResultSet;
import com.bosssoft.platform.es.jdbc.model.InsertSqlObj;
import com.bosssoft.platform.es.jdbc.model.QueryBody;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;
import com.bosssoft.platform.es.jdbc.model.UpdateSqlObj;
import com.facebook.presto.sql.parser.SqlParser;
import com.facebook.presto.sql.tree.CreateTable;
import com.facebook.presto.sql.tree.RenameTable;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;

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
	
	private ResultSetDirector resultDirector;
	
	private UpdateDirector updateDirector;
	
	
	public ESStatement(ESConnection connection){
		this.connection=connection;
		
		SearchConverter searchConverter=new SearchConverterImpl();
		selectObjDirector=new SelectObjDirector(searchConverter);
		
		QueryConstructer queryBuilder=new QueryConstructerImpl();
		queryDirector=new QueryDirector(queryBuilder);
		
		ResultSetConstructer resultBuilder=new ResultSetConstructerImpl();
		resultDirector=new ResultSetDirector(resultBuilder);
		
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
		SearchResponse response=esClient.search(queryBody.getQueryBuilder(), queryBody.getAggregationBuilder(),queryBody.getOrderby(),queryBody.getPageMate(), connection.getIndex(), sqlObj.getFrom());
		
		//构建resultSet
		ESResultSet resultSet=resultDirector.construct(response,sqlObj,connection);
		
		return resultSet;
	}




	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	@Override
	public int executeUpdate(String sql) throws SQLException{
		sql = sql.replaceAll("\r", " ").replaceAll("\n", " ").trim().toLowerCase();
		if(sql.startsWith("create")){
			String helpesql=sql+"(_id integer)";//借助新字符串来转换，以获取表名
			CreateTable statement = (CreateTable) sqlparser.createStatement(helpesql);
			String tableName=statement.getName().toString();
			//构建mapping
		    String mapping=updateDirector.buildCreate(tableName, connection.getIndex());
		    
		    //调用es 建立mapping
		    ESClient esClient=connection.getEsClient();
		    esClient.createType(connection.getIndex(), mapping, tableName);
		    
		}
		else if(sql.startsWith("update")){
			UpdateSqlObj updateSqlObj=updateDirector.buildUpdate(sql, connection.getIndex(), this);
			
			//调用esclient
			ESClient esClient=connection.getEsClient();
			for (String id : updateSqlObj.getIds()) {
				esClient.updateDoc(connection.getIndex(), updateSqlObj.getType(), updateSqlObj.getUpdateList(), id);
			}
		}
		else if(sql.startsWith("insert")){
			
			InsertSqlObj sqlObj=updateDirector.buildInsert(sql);
			
			//调用esclient
			ESClient esClient=connection.getEsClient();
			esClient.IndexDoc(connection.getIndex(), sqlObj.getType(), sqlObj.getValueList());
		}else if(sql.startsWith("delete")){//删除
			DeleteSqlObj deleteSqlObj=updateDirector.buildDelete(sql, this);
			
			//调用esclient
			ESClient esClient=connection.getEsClient();
			for (String id : deleteSqlObj.getIds()) {
				esClient.deleteDoc(connection.getIndex(), deleteSqlObj.getType(), id);
			}
			
		}else if(sql.startsWith("alter")){//修改表结构
			String helpersql=sql+" RENAME TO name";//构建新sql以获取表名
			RenameTable statement =  (RenameTable) sqlparser.createStatement(helpersql);
			String tableName=statement.getSource().toString();
			
			//构建mapping
		    String mapping=updateDirector.buildCreate(tableName, connection.getIndex());
		    ESClient esClient=connection.getEsClient();
			esClient.addMapping(connection.getIndex(), tableName, mapping);
			
		}else throw new SQLException("illegal sql");
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeBatch()
	 */
	@Override
	public int[] executeBatch() throws SQLException {
		BatchUpdateObj batchObj=updateDirector.buildBatch(this, connection.getIndex());
		ESClient esClient=connection.getEsClient();
		
		
		List<UpdateSqlObj> upobjList=batchObj.getUpobjList();
		 List<DeleteSqlObj> deobjList=batchObj.getDeobjList();
		 List<InsertSqlObj> inobjList=batchObj.getInobjList();
		 
		 if(upobjList.size()!=0)  esClient.updateBatch(connection.getIndex(), upobjList);
		 if(deobjList.size()!=0)  esClient.deletBatch(connection.getIndex(), deobjList);
		 if(inobjList.size()!=0)  esClient.indexBatch(connection.getIndex(), inobjList);
		 
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	@Override
	public void addBatch(String sql) throws SQLException {
		sql=sql.toLowerCase().trim();
		if(sql.startsWith("insert")) updateDirector.addinsert(sql);
		else if(sql.startsWith("update")) updateDirector.addupdate(sql);
		else if(sql.startsWith("delete")) updateDirector.adddelete(sql);
		else throw new SQLException("illegal sql");
		
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