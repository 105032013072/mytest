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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO date型数据测试
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class TimeTest {
   
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
		 
		 //创建表
		 @Test
		 public void test0(){
			 try {
				 //集群名为：escluster
				 Connection con = DriverManager.
						 getConnection("jdbc:es://localhost:9300/"+index);
				 Statement st = con.createStatement();
			      	//ResultSet rs = st.executeQuery("SELECT * FROM uab_agen_item where tfname ='测试3' limit 2,3");
				 String sql="CREATE TABLE  timetest";
				 st.executeUpdate(sql);
				 
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
		 
		 @Test
		 //insert
		 public void testinsert(){
			 try {
				 //集群名为：escluster
				 Connection con = DriverManager.
						 getConnection("jdbc:es://localhost:9300/"+index);
				 String sql="insert into timetest(time_date,time_time,time_stamp) values (?,?,?)";
				 PreparedStatement ps = con.prepareStatement(sql); 	
				 ps.setDate(1, new Date(new java.util.Date().getTime()));
				 ps.setTime(2, new Time(System.currentTimeMillis()));
				 ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				 ps.executeUpdate(); 
			       con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
		 
		 //普通查询
		 @Test
		 public void test1(){
			 try {
				 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
			      	Statement st = con.createStatement();
			      	ResultSet rs = st.executeQuery("SELECT * from timetest");
			      	while(rs.next()){
			       		System.out.println("time_date:"+rs.getDate("time_date"));
			       		System.out.println("time_time:"+rs.getTime("time_time"));
			       		System.out.println("time_stamp:"+rs.getTimestamp("time_stamp"));
			       	 }
			       	 rs.close();
			       	 con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
		 
		 
		//复杂查询
		 @Test
		 public void test2(){
			 try {
				 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
			      	Statement st = con.createStatement();
			      	ResultSet rs = st.executeQuery("SELECT * from timetest where time_date>'2017-01-06'");
			      	while(rs.next()){
			       		System.out.println("time_date:"+rs.getDate("time_date"));
			       		System.out.println("time_time:"+rs.getTime("time_time"));
			       		System.out.println("time_stamp:"+rs.getTimestamp("time_stamp"));
			       	 }
			       	 rs.close();
			       	 con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
		 
		 
		//PreparedStatement复杂查询
		 @Test
		 public void test3(){
			 try {
				 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
				 String sql="SELECT * from timetest where time_date<=?";
				 PreparedStatement ps = con.prepareStatement(sql); 	
			     ps.setDate(1, new Date(new java.util.Date().getTime()));
			     ResultSet rs=ps.executeQuery();
			      	while(rs.next()){
			       		System.out.println("time_date:"+rs.getDate("time_date"));
			       		System.out.println("time_time:"+rs.getTime("time_time"));
			       		System.out.println("time_stamp:"+rs.getTimestamp("time_stamp"));
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