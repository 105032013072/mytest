package Test.pojo;

public class User {
  private String userNo;
  
  private String userName;
  
  private String deptNo;
  
  private Double userSalary;
  
  private Integer userAge ;
  
  

public User(String userNo, String userName, String deptNo, Double userSalary, Integer userAge) {
	super();
	this.userNo = userNo;
	this.userName = userName;
	this.deptNo = deptNo;
	this.userSalary = userSalary;
	this.userAge = userAge;
}



public String getUserNo() {
	return userNo;
}

public void setUserNo(String userNo) {
	this.userNo = userNo;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getDeptNo() {
	return deptNo;
}

public void setDeptNo(String deptNo) {
	this.deptNo = deptNo;
}



public Double getUserSalary() {
	return userSalary;
}



public void setUserSalary(Double userSalary) {
	this.userSalary = userSalary;
}



public Integer getUserAge() {
	return userAge;
}

public void setUserAge(Integer userAge) {
	this.userAge = userAge;
}



@Override
public String toString() {
	return "User [userNo=" + userNo + ", userName=" + userName + ", deptNo=" + deptNo + ", userSalary=" + userSalary
			+ ", userAge=" + userAge + "]";
}
  

  
}
