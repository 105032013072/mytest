package com.bosssoft.platform.bosssoft_elasticsearch_jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import com.pojo.Staff;
import com.pojo.User;


public class Mytest{
	
	private Connection con;
	
	private Statement st;
	
	private String index="demo2";
	
    @Before
	public void init(){
		try{
			Class.forName("com.bosssoft.platform.es.jdbc.driver.ESDriver");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test() {
		try{
			Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
	      	Statement st = con.createStatement();
       	 // execute a query on mytype within myidx
       	 ResultSet rs = st.executeQuery("SELECT * FROM user");
       	 ResultSetMetaData rsmd = rs.getMetaData();
       	 int nrCols = rsmd.getColumnCount();
       	 // get other column information like type
       	 while(rs.next()){
       	     for(int i=1; i<=nrCols; i++){
       	         System.out.print(rs.getObject(i)+" ");
       	     }
       	     System.out.println();
       	 }
       	 rs.close();
       	 con.close();
   	 }catch(Exception e){
   		 e.printStackTrace();
   	 }
	}
	
	//创建表
	@Test
	public void testCreateTable(){
		try{
			Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
			Statement st = con.createStatement();
			String sql = "CREATE TABLE usertest " +
	                   "(user_no string," +
	                   " user_name string, " + 
	                   " dept_no string, " + 
	                   " user_salary integer,"
	                   + "user_age integer"
	                   + ")" ;
		/*	String sql="CREATE TABLE monkey (_id String)";*/
           st.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//插入数据(单条)
	@Test
	public void testInsert(){
		try{
			Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index);
			Statement st = con.createStatement();
			/*String sql="insert into user(user_no,user_name,deptNO,user_salary,user_age) values"+
			            "('s002','jon','d0',3500,12)";*/
			
			User user=new User("u3"+0, "小明", "d"+3, 6000.0, 40+1);
			String sql="insert into user(user_no,user_name,dept_no,user_salary,user_age) values("
			+"\'"+user.getUserNo()+"\'"+","+"\'"+user.getUserName()+"\'"+","+"\'"+user.getDeptNo()+"\'"+","+user.getUserSalary()+","+user.getUserAge()+")";
			System.out.println(sql);
			st.executeUpdate(sql);
		}catch(Exception e){
		}
	}
	
	//批量插入数据
	@Test
	public void testInsertBatch(){
		try {
		
			Connection con = DriverManager.getConnection("jdbc:es://localhost:9300/"+index+"?cluster.name=escluster");
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
	
	@Test
	public void testPreparedStatement(){
		try {
			Connection con = DriverManager.getConnection("jdbc:sql4es://192.168.10.43:9300/testes?cluster.name=my-application");
			String sql="insert into staff(staff_no,staff_name,dept_no,staff_salary) values (?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			Staff staff=new Staff("004", "jj", "d1", 3600);
			ps.setString(1, staff.getStaffNo());
			ps.setString(2, staff.getStaffName());
			ps.setString(3, staff.getDeptNo());
			ps.setInt(4, staff.getSalary());
			ps.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void testDeleteTable(){
		try{
			Connection con = DriverManager.getConnection("jdbc:sql4es://192.168.10.43:9300/testes?cluster.name=my-application");
			Statement st = con.createStatement();
			String sql="drop table monkey";
			st.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
