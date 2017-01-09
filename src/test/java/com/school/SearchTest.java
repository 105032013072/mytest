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
 * Created on 2017年1月9日
 *******************************************************************************/


package com.school;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO 查询测试
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class SearchTest {

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
	 //聚合函数
	 public void test0(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
		      	Statement st = con.createStatement();
		      	//ResultSet rs = st.executeQuery("SELECT count(user_salary) as allmoney,min(user_salary) as min,MAX(user_salary) as max,sum(user_salary) as sum,avg(user_salary) as avg from user");
		      	ResultSet rs = st.executeQuery("SELECT min(user_salary) as min,MAX(user_salary) as max,count(*),sum(user_salary),AVG(user_salary) as avg from user where user_salary<2900");
		      	while(rs.next()){
		       		System.out.println("count(*):"+rs.getFloat("count(*)"));
		       		System.out.println("min:"+rs.getDouble("min"));
		       		System.out.println("max:"+rs.getDouble("max"));
		       		System.out.println("sum(user_age):"+rs.getDouble("sum(user_salary)"));
		       		System.out.println("avg:"+rs.getDouble("avg"));
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