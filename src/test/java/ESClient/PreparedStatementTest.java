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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.bosssoft.platform.es.jdbc.enumeration.AggType;

import Test.pojo.User;

/**
 * TODO 开发测试类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class PreparedStatementTest {
 
	
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
	 
	 //查询
	 @Test
	 public void test0(){
		 try {
			 //集群名为：escluster
			 Connection con = DriverManager.
					 getConnection("jdbc:es://localhost:9300/"+index);
			 String sql="select * from user where dept_no=? and user_salary=?";
			 PreparedStatement ps = con.prepareStatement(sql);
			 ps.setString(1, "d0");
			 ps.setFloat(2, 3202);
			 ResultSet rs=ps.executeQuery();
			 ResultSetMetaData metaData=rs.getMetaData();
			 int ncols=metaData.getColumnCount();
		      while(rs.next()){
		    	  for (int i=1;i<=ncols;i++) {
		    		  System.out.print(metaData.getColumnName(i)+": "+rs.getObject(i)+"   ");
				}
		    	  System.out.println();//换行
		      }
			 
		       con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 //插入
	 @Test
	 public void test4(){
		 try {
			 //集群名为：escluster
			 Connection con = DriverManager.
					 getConnection("jdbc:es://localhost:9300/"+index);
			 String sql="insert into user(user_no,user_birth) values(?,?)";
			 PreparedStatement ps = con.prepareStatement(sql);
			 ps.setString(1, "uuuuu02");
			 ps.setTimestamp(2, new Timestamp(new Date().getTime()));
			 //ps.setDate(2, new java.sql.Date(new Date().getTime()));
			 ps.executeUpdate();
		       con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	//更新
		 @Test
		 public void test5(){
			 try {
				 //集群名为：escluster
				 Connection con = DriverManager.
						 getConnection("jdbc:es://localhost:9300/"+index);
				 String sql="UPDATE user SET user_name=? where user_no=?";
				 PreparedStatement ps = con.prepareStatement(sql);
				 ps.setString(1, "newName");
				 ps.setString(2, "uaaaa");
				ps.executeUpdate();
			       con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
	 
	 
		//删除
		 @Test
		 public void test6(){
			 try {
				 //集群名为：escluster
				 Connection con = DriverManager.
						 getConnection("jdbc:es://localhost:9300/"+index);
				 String sql="delete from  user  where user_no=?";
				 PreparedStatement ps = con.prepareStatement(sql);
				 ps.setString(1, "uaaaa");
				ps.executeUpdate();
			       con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }

		 //批量更新
		 @Test
		 public  void test7(){
			 try {
				 //集群名为：escluster
				 Connection con = DriverManager.
						 getConnection("jdbc:es://localhost:9300/"+index);
				 String sql="UPDATE user SET user_name=? where user_no=?";
				 PreparedStatement ps = con.prepareStatement(sql);
				 ps.setString(1, "qqq");
				 ps.setString(2, "un01");
				 ps.addBatch();
				
				ps.setString(1, "qqq");
				 ps.setString(2, "un02");
				ps.addBatch();
				
				ps.executeBatch();
			       con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
		 
		 @Test
		 public  void test8(){
			 try {
				 //集群名为：escluster
				 Connection con = DriverManager.
						 getConnection("jdbc:es://localhost:9300/"+index);
				 String sql="insert into user(user_no,user_name,dept_no,user_salary,user_age) values(?,?,?,?,?)";
				 PreparedStatement ps = con.prepareStatement(sql);
				 ps.setString(1, "un01");
				 ps.setString(2, "aaaa");
				 ps.setString(3, "d3");
				 ps.setFloat(4, 6000);
				 ps.setInt(5, 39);
				ps.addBatch();
				
				ps.setString(1, "un02");
				 ps.setString(2, "bbbb");
				 ps.setString(3, "d3");
				 ps.setFloat(4, 6000);
				 ps.setInt(5, 39);
				ps.addBatch();
				
				ps.executeBatch();
			       con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
		 
		 //批量删除
		 @Test
		 public  void test9(){
			 try {
				 //集群名为：escluster
				 Connection con = DriverManager.
						 getConnection("jdbc:es://localhost:9300/"+index);
				 String sql="delete from  user  where user_no=?";
				 PreparedStatement ps = con.prepareStatement(sql);
				 ps.setString(1, "un01");
				 ps.addBatch();
				
				 ps.setString(1, "un02");
				ps.addBatch();
				
				ps.executeBatch();
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