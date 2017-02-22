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
import com.pojo.User;

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
			 st.executeUpdate("UPDATE user SET user_name='aaaa' where dept_no='d1'"); 
		       con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 //修改
	 @Test
	 public void test4(){
		 try {
			 //集群名为：escluster
			 Connection con = DriverManager.
					 getConnection("jdbc:es://localhost:9300/"+index);
			 
			 Statement st = con.createStatement(); 	
			 st.executeUpdate("alter table user"); 
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
			/* String sql="insert into user(user_no,user_name) values("
						+"\'"+"n02"+"\'"+","+"\'"+"小东"+"\'"+")";*/
			 String sql="insert into user(user_no,user_name,dept_no,user_salary,user_age) values ('u31','小东','d3',6000,42)";
			/* User user=new User("b0"+1, "jason"+1+"号", "d"+0, 3200+1, 20+1);
				String sql="insert into user(user_no,user_name,dept_no,user_salary,user_age) values ("
				+"\'"+user.getUserNo()+"\'"+","+"\'"+user.getUserName()+"\'"+","+"\'"+user.getDeptNo()+"\'"+","+user.getUserSalary()+","+user.getUserAge()+")";*/
			 st.executeUpdate(sql); 
		       con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	//delete
	 @Test
		 public void test3(){
			 try {
				 //集群名为：escluster
				 Connection con = DriverManager.
						 getConnection("jdbc:es://localhost:9300/"+index);
				 
				 Statement st = con.createStatement(); 	
				String sql="delete from user where dept_no='d03'";
				 //String sql="delete from user where dept_no is null";
				 st.executeUpdate(sql); 
			       con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
	 
	//批量插入数据
		@Test
		public void testInsertBatch(){
			try {
			
				Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
				Statement st = con.createStatement();
				for(int i=0;i<3;i++){
					User user=new User("u0"+i, "jason"+i+"号", "d"+0, 3200.0+i, 20+i);
					String sql="insert into user(user_no,user_name,dept_no,user_salary,user_age) values ("
					+"\'"+user.getUserNo()+"\'"+","+"\'"+user.getUserName()+"\'"+","+"\'"+user.getDeptNo()+"\'"+","+user.getUserSalary()+","+user.getUserAge()+")";
					st.addBatch(sql);
				}
				for(int i=0;i<3;i++){
					User user=new User("u1"+i, "tom"+i+"号", "d"+1, 2200.0+i, 20+i);
					String sql="insert into user(user_no,user_name,dept_no,user_salary,user_age) values ("
					+"\'"+user.getUserNo()+"\'"+","+"\'"+user.getUserName()+"\'"+","+"\'"+user.getDeptNo()+"\'"+","+user.getUserSalary()+","+user.getUserAge()+")";
					st.addBatch(sql);
				}
				for(int i=0;i<3;i++){
					User user=new User("u2"+i, "mical"+i+"号", "d"+2, 4200.0+i, 20+i);
					String sql="insert into user(user_no,user_name,dept_no,user_salary,user_age) values ("
					+"\'"+user.getUserNo()+"\'"+","+"\'"+user.getUserName()+"\'"+","+"\'"+user.getDeptNo()+"\'"+","+user.getUserSalary()+","+user.getUserAge()+")";
					st.addBatch(sql);
				}
				st.executeBatch();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//批量修改数据
				@Test
				public void testUpdateBatch(){
					try {
					
						Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
						Statement st = con.createStatement();
						String sql1="update user set user_name='NewTome' where user_salary>=2200 and user_salary<=2202";
						String sql2="update user set dept_no='dn' where dept_no='d2'";
						String sql3="insert into user(user_no,user_name,dept_no) values ('uu','aaa','ddd') ";
						st.addBatch(sql1);
						st.addBatch(sql2);
						st.addBatch(sql3);
						st.executeBatch();
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
				//批量删除数据
				@Test
				public void testDeleteBatch(){
					try {
					
						Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
						Statement st = con.createStatement();
						String sql1="delete from user where user_salary>=2200 and user_salary<=2202";
						String sql2="delete from  user  where dept_no='dn'";
						String sql3="insert into user(user_no,user_name,dept_no) values ('uu','aaa','ddd') ";
						st.addBatch(sql1);
						st.addBatch(sql2);
						st.addBatch(sql3);
						st.executeBatch();
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
				 //修改表
				 @Test
				 public void test7(){
					 try {
						 //集群名为：escluster
						 Connection con = DriverManager.
								 getConnection("jdbc:es://localhost:9300/"+index);
						 
						 Statement st = con.createStatement(); 	
						 st.executeUpdate("alter table user"); 
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