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
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import com.bosssoft.platform.es.jdbc.enumeration.AggType;

/**
 * TODO 开发测试类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class updateTest {
 
	
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
	 
	 //更新
	 @Test
	 public void test0(){
		 try {
			 //集群名为：escluster
			 Connection con = DriverManager.
					 getConnection("jdbc:es://localhost:9300/"+index);
			 
			 Statement st = con.createStatement(); 	
			 st.executeUpdate("UPDATE user SET user_name='aaaa'"); 
		       con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 

	 
	 
	 @Test
	 //insert
	 public void test2(){
		 try {
			 //集群名为：escluster
			 Connection con = DriverManager.
					 getConnection("jdbc:es://localhost:9300/"+index);
			 
			 Statement st = con.createStatement(); 	
			 String sql="insert into user(user_no,user_name,dept_no,user_salary,user_age) values("
						+"\'"+"n01"+"\'"+","+"\'"+"newname"+"\'"+","+"\'"+"d3"+"\'"+","+5000+","+38+")";
			 st.executeUpdate(sql); 
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