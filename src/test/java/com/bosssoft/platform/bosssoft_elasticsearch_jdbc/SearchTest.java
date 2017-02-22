package com.bosssoft.platform.bosssoft_elasticsearch_jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;



public class SearchTest {
	
	//private String index="sqles";
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
					 getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	//ResultSet rs = st.executeQuery("SELECT * FROM uab_agen_item where tfname ='测试3' limit 2,3");
			 
			 Statement st = con.createStatement(); 	
			 ResultSet rs = st.executeQuery("SELECT STAFF_NAME FROM jdbc where staff_name='明'");
		      while(rs.next()){
		       	System.out.println(rs.getString("staff_name"));
		      }
		       rs.close();
		       con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 
	 @Test
	 public void test7(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	
		      	//ResultSet rs = st.executeQuery("SELECT * FROM uab_agen_item where tfname ='测试3' limit 2,3");
		     
				String sql="SELECT * FROM jdbc where staff_name=?";
				PreparedStatement ps = con.prepareStatement(sql);
		        ps.setString(1, "张三");
		        ResultSet rs=ps.executeQuery();
		        ResultSetMetaData rsmd = rs.getMetaData();
		       	 int nrCols = rsmd.getColumnCount();
		       	 while(rs.next()){
		       	     for(int i=1; i<=nrCols; i++){
		       	         System.out.print(rs.getObject(i)+" ");
		       	     }
		       	 }
		       	 rs.close();
		       	 con.close();
		        
			} catch (Exception e) {
				
			}
	 }
	 
	 
	 //测试模糊查询（like）
	 @Test
	 public void test1(){
        try {
        	Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
	      	Statement st = con.createStatement();
	      	
	      	ResultSet rs = st.executeQuery("SELECT * FROM user where user_name like 'jason%'");
	       	 ResultSetMetaData rsmd = rs.getMetaData();
	       	 int nrCols = rsmd.getColumnCount();
	       	 while(rs.next()){
	       	     for(int i=1; i<=nrCols; i++){
	       	         System.out.print(rs.getObject(i)+" ");
	       	     }
	       	 }
	       	 rs.close();
	       	 con.close();
		} catch (Exception e) {
			
		}
	 }
	 
	//多条件查询
		 @Test
		 public void test2(){
			 try {
				 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?");
			      	Statement st = con.createStatement();
			      	
			      	
			      	
			      	ResultSet rs = st.executeQuery("SELECT * FROM user where (user_salary=2200 and user_age=20) or dept_no='d1'");
			       	 ResultSetMetaData rsmd = rs.getMetaData();
			       	 int nrCols = rsmd.getColumnCount();
			       	 // get other column information like type
			       	 while(rs.next()){
			       		System.out.print(rs.getString(""));
			       	    
			       	     System.out.println();
			       	 }
			       	 rs.close();
			       	 con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
	 
	 //group by and HAVING
	 @Test
	 public void test4(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	
		      	ResultSet rs = st.executeQuery("SELECT dept_no,count(*) as total FROM jdbc group by dept_no HAVING total>1 ");
		       	 ResultSetMetaData rsmd = rs.getMetaData();
		       	 int nrCols = rsmd.getColumnCount();
		       	 while(rs.next()){
		       		System.out.print(rs.getString("dept_no")+" ");
		       		System.out.println(rs.getFloat("total"));
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

	 }
	 
	 
	//分页（limit） order by 
	 @Test
	 public void test5(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	
		      	ResultSet rs = st.executeQuery("SELECT *  FROM jdbc order by STAFF_SAL  limit 1,2");
		      	ResultSetMetaData rsmd = rs.getMetaData();
		       	 int nrCols = rsmd.getColumnCount();
		       	 while(rs.next()){
		       	     for(int i=1; i<=nrCols; i++){
		       	         System.out.print(rs.getObject(i)+" ");
		       	     }
		       	     System.out.println();
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				
			}
	 }
	 
	 
	 //大小比较
	 @Test
	 public void test6(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	
		      	ResultSet rs = st.executeQuery("SELECT * FROM uab_agen_item where tfeffdate>'2016-09-29'");

		       	 ResultSetMetaData rsmd = rs.getMetaData();
		       	 int nrCols = rsmd.getColumnCount();
		       	 // get other column information like type
		       	 while(rs.next()){
		       		System.out.print(rs.getString("aifid")+"  ");
		       		System.out.print(rs.getString("tfeffdate"));
		       	    System.out.println();
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				
			}
	 }
	 
	 @Test
	 //聚合函数
	 public void test8(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	ResultSet rs = st.executeQuery("SELECT count(*) as total,min(STAFF_SAL) as min,MAX(STAFF_SAL) as max,sum(STAFF_SAL) as sum,avg(STAFF_SAL) as avg from jdbc");
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
				
			}
	 }
	 
	 @Test
	 //DISTINCT
	 public void test9(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	ResultSet rs = st.executeQuery("SELECT DISTINCT STAFF_SAL from jdbc");
		      	while(rs.next()){
		       		System.out.println("STAFF_SAL:"+rs.getFloat("STAFF_SAL"));
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				
			}
	 }
	 
	 
	 @Test
	 public void test10(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	ResultSet rs = st.executeQuery("SELECT * from jdbc where (STAFF_SAL=2000 and DEPT_NO is not null)or STAFF_SAL=5000");
		      	while(rs.next()){
		       		System.out.println("STAFF_SAL:"+rs.getFloat("STAFF_SAL"));
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				
			}
	 }
	 
	 @Test
	 public void test11(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	ResultSet rs = st.executeQuery("SELECT * from jdbc order by fid");
		      	ResultSetMetaData rsmd = rs.getMetaData();
		       	 int nrCols = rsmd.getColumnCount();
		       	 while(rs.next()){
		       	     for(int i=1; i<=nrCols; i++){
		       	         System.out.print(rs.getObject(i)+"   ");
		       	     }
		       	     System.out.println();
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				
			}
	 }
	 
	 @Test
	 public void test12(){
		 try {
			 Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
		      	Statement st = con.createStatement();
		      	ResultSet rs = st.executeQuery("SELECT * from jdbc where dept.staff.sal=3000");
		      	ResultSetMetaData rsmd = rs.getMetaData();
		       	 int nrCols = rsmd.getColumnCount();
		       	 while(rs.next()){
		       	     for(int i=1; i<=nrCols; i++){
		       	         System.out.print(rs.getObject(i)+"   ");
		       	     }
		       	     System.out.println();
		       	 }
		       	 rs.close();
		       	 con.close();
			} catch (Exception e) {
				
			}
	 }
	 
	 
	 
}
