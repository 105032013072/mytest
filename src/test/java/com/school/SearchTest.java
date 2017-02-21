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
import java.sql.ResultSetMetaData;
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
	 
	 //distinct
	 @Test
	 public void test1(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
		      	Statement st = con.createStatement();
		      	ResultSet rs = st.executeQuery("SELECT distinct dept_no,user_salary from user where user_salary>3000 order by dept_no,user_salary desc");
		      	while(rs.next()){
		       		System.out.println("dept_no:"+rs.getString("dept_no")+"  user_salary:"+rs.getFloat("user_salary"));
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 @Test
	 //in
	 public void test2(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
		      	Statement st = con.createStatement();
		      	//ResultSet rs = st.executeQuery("SELECT count(user_salary) as allmoney,min(user_salary) as min,MAX(user_salary) as max,sum(user_salary) as sum,avg(user_salary) as avg from user");
		      	ResultSet rs = st.executeQuery("SELECT user_no,user_name from user where user_salary in (6000.0,2200.0) order by user_no");
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
	 
	//多条件查询 +not
	 @Test
	 public void test3(){
	 	 try {
	 		 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
	 	      	Statement st = con.createStatement();
	 	      	
	 	      	
	 	      	
	 	      	ResultSet rs = st.executeQuery("SELECT * FROM user where not ((user_salary=2200 and user_age=20) or (dept_no='d1' and user_salary>=2000)) order by user_no");
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
	 
	 
	 @Test
	 //is null  /not null
	 public void test4(){
		 try {
			 //集群名为：escluster
			 Connection con = DriverManager.
					 getConnection("jdbc:es://localhost:9300/"+index);
		      	//ResultSet rs = st.executeQuery("SELECT * FROM uab_agen_item where tfname ='测试3' limit 2,3");
			 
			 Statement st = con.createStatement(); 	
			 
			 ResultSet rs = st.executeQuery("select * from user where user_birth is not null");
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
	 
	//group by and HAVING
		 @Test
		 public void test5(){
			 try {
				 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
			      	Statement st = con.createStatement();
			      	
			      	ResultSet rs = st.executeQuery("SELECT dept_no,AVG(user_salary)as avgSal FROM user group by dept_no having avgSal>3000 order by dept_no desc ");
			      	//ResultSet rs = st.executeQuery("SELECT dept_no,max(user_salary) FROM user group by dept_no");
			       	while(rs.next()){ 
			       		System.out.print("dept_no: "+rs.getString("dept_no")+" ");
			       		System.out.println("agvSal: "+rs.getFloat("avgSal"));
			       	 }
			       	 rs.close();
			       	 con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

		 }
		 
			//分页
		 @Test
		 public void test6(){
			 try {
		 		 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
		 	      	Statement st = con.createStatement();
		 	      
		 	      	ResultSet rs = st.executeQuery("SELECT * FROM user order by user_no limit 3,3");
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