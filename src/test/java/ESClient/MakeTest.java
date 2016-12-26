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


package ESClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import com.bosssoft.platform.es.jdbc.enumeration.AggType;

/**
 * TODO 开发测试类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class MakeTest {
 
	
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
	 
	 //简单查询
	 @Test
	 public void test0(){
		 try {
			 //集群名为：escluster
			 Connection con = DriverManager.
					 getConnection("jdbc:es://localhost:9300/"+index);
		      	//ResultSet rs = st.executeQuery("SELECT * FROM uab_agen_item where tfname ='测试3' limit 2,3");
			 
			 Statement st = con.createStatement(); 	
			 ResultSet rs = st.executeQuery("SELECT user_salary FROM user where user_salary>=2200 ");
		      while(rs.next()){
		       	System.out.println(rs.getInt("user_salary"));
		       	System.out.println(rs.getString("user_no"));
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
		      	ResultSet rs = st.executeQuery("SELECT  distinct user_salary,user_no from user where user_salary>2200");
		      	while(rs.next()){
		       		System.out.println("user_salary:"+rs.getFloat("user_salary"));
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 
	 @Test
	 //聚合函数
	 public void test2(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
		      	Statement st = con.createStatement();
		      	//ResultSet rs = st.executeQuery("SELECT count(user_salary) as allmoney,min(user_salary) as min,MAX(user_salary) as max,sum(user_salary) as sum,avg(user_salary) as avg from user");
		      	ResultSet rs = st.executeQuery("SELECT min(user_salary) as min,MAX(user_salary) as max,sum(user_salary) as sum,avg(user_salary) as avg from user");
		      	while(rs.next()){
		       		System.out.println("total:"+rs.getFloat("total"));
		       		System.out.println("min:"+rs.getDouble("min"));
		       		System.out.println("max:"+rs.getDouble("max"));
		       		System.out.println("sum:"+rs.getDouble("sum"));
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