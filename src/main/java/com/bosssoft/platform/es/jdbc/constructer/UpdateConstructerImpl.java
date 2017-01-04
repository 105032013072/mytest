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


package com.bosssoft.platform.es.jdbc.constructer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bosssoft.platform.es.jdbc.model.DeleteSqlObj;
import com.bosssoft.platform.es.jdbc.model.InsertSqlObj;
import com.bosssoft.platform.es.jdbc.model.UpdateSqlObj;
import com.facebook.presto.sql.parser.SqlParser;
import com.facebook.presto.sql.tree.ComparisonExpression;
import com.facebook.presto.sql.tree.Delete;
import com.facebook.presto.sql.tree.Expression;
import com.facebook.presto.sql.tree.Insert;
import com.facebook.presto.sql.tree.QueryBody;
import com.facebook.presto.sql.tree.Row;
import com.facebook.presto.sql.tree.Values;
import com.fasterxml.jackson.annotation.JsonFormat.Value;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class UpdateConstructerImpl implements UpdateConstructer{
	
	private static Judger judger=new Judger();
   
	public  String buildCreate(String table,String index) {
		try{
			String mapping=null;
			URL url=Thread.currentThread().getContextClassLoader().getResource("mapping");
			File configDir=new File(url.toURI());
			File[] fileList=configDir.listFiles();
			for (File file : fileList) {
				if(file.getName().equals(table+".json")){
				   mapping=readFile(file);
					break;
				}
			}
			return mapping;
		}catch(Exception e){
			e.printStackTrace();
			return  null;
		}
		
	}
	
	
	private String readFile(File file) throws Exception{
		StringBuffer buffer=new StringBuffer();
		InputStream is = new FileInputStream(file);
		String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        return buffer.toString();
	}
	
	
	public UpdateSqlObj buildUpdateObj(String sql,String index,Statement esStatement) throws SQLException{
		sql = sql.replaceAll("\r", " ").replaceAll("\n", " ").trim();
		//更新所有数据（构建辅助语句）
		if(!sql.contains("where")) sql=sql+" where all";
				
		UpdateSqlObj updateSqlObj=new UpdateSqlObj();
		updateSqlObj.setIndex(index);
		
		Pattern regex = Pattern.compile("UPDATE\\s+(\\w+)\\.?(\\w+)?\\s+SET\\s+(.+)\\s+WHERE\\s+(.+)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = regex.matcher(sql);
		if(!matcher.find()) throw new SQLException("can not to parse UPDATE sql");
		updateSqlObj.setType(matcher.group(1));
		
		//构建更新列及其对应的新值
		String[] updates = matcher.group(3).replaceAll(",\\s*([\"|\\w|\\.]+\\s*=)", "<-SPLIT->$1").split("<-SPLIT->");
		for(String u : updates){
			ComparisonExpression comparison = (ComparisonExpression) new SqlParser().createExpression(u);
			updateSqlObj.addUpdateList(comparison.getLeft().toString().replaceAll("\"", ""), judger.judgeValueType(comparison.getRight()));
		}
		
	   //根据where条件查询 获取符合条件的文档id
		String searchsql=null; 
		if("all".equals(matcher.group(4))){
			searchsql="select _id from "+matcher.group(1);
		}else{
			searchsql="select _id from "+matcher.group(1)+" where "+matcher.group(4);
		}
		  
		ResultSet rs=esStatement.executeQuery(searchsql);
		while(rs.next()) updateSqlObj.addIds(rs.getString("_id"));
		return updateSqlObj;
	}
	
	public InsertSqlObj buildInsertObj(com.facebook.presto.sql.tree.Statement statement)throws SQLException{
		InsertSqlObj insertSqlObj=new InsertSqlObj();
		
		Insert insert=(Insert) statement;
		
		insertSqlObj.setType(insert.getTarget().toString());
		List<String> columns=insert.getColumns().get();
		
		Values value=(Values) insert.getQuery().getQueryBody();
		Expression exp=value.getRows().get(0);
		List<Expression> list=((Row)exp).getItems();
		
		for (int i = 0; i < columns.size(); i++) {
			String key=columns.get(i);
			Object obj=judger.judgeValueType(list.get(i));
			insertSqlObj.addValue(key, obj);
		}
	
		return insertSqlObj;
	}


	
	@Override
	public DeleteSqlObj builddeleteObj(String sql,Statement esStatement) throws SQLException {
		DeleteSqlObj deleteSqlObj=new DeleteSqlObj();
		
		Delete deleteStatement = (Delete) new SqlParser().createStatement(sql);
		String type=deleteStatement.getTable().getName().toString();
		deleteSqlObj.setType(type);
		
		String helpeSql=null;
		if(sql.contains("where")){
			String where=sql.substring(sql.indexOf("where"),sql.length());
			helpeSql="select _id from "+type+" "+where;
		}else{
			helpeSql="select _id from "+type;
		}
		
		//根据where条件查询 获取符合条件的文档id
		 ResultSet rs=esStatement.executeQuery(helpeSql);
		while(rs.next()) deleteSqlObj.addid(rs.getString("_id"));
		return deleteSqlObj;
	}



	
}

/*
 * 修改历史
 * $Log$ 
 */