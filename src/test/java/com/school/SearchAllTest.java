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
 * Created on 2017年2月21日
 *******************************************************************************/


package com.school;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class SearchAllTest {
private String index="demo";
	
	private String host="localhost";
    
	 @Before
		public void init(){
			try{
				Class.forName("com.bosssoft.platform.es.jdbc.driver.ESDriver");
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	 @Test
	 public void test3(){
	 	 try {
	 		 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
	 	      	Statement st = con.createStatement();
	 	      
	 	      	ResultSet rs = st.executeQuery("SELECT * FROM user order by user_no");
	 	       	 ResultSetMetaData rsmd = rs.getMetaData();
	 	       	ResultSetMetaData metaData=rs.getMetaData();
	 			 int ncols=metaData.getColumnCount();
	 		      while(rs.next()){
	 		    	  for (int i=1;i<=ncols;i++) {
	 		    		  System.out.print(metaData.getColumnName(i)+": "+rs.getObject(i)+"   ");
	 				}
	 		    	  System.out.println();//换行
	 		      }
	 	       	 rs.close();
	 	       	 con.close();
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 		}
	 }
}

/*
 * 修改历史
 * $Log$ 
 */