package com.pojo;

//测试用的员工类
public class Staff {
   
	public String staffNo;
	public String staffName;
	public String deptNo;
	public Integer salary;
	public Staff(String staffNo, String staffName, String deptNo, Integer salary) {
		super();
		this.staffNo = staffNo;
		this.staffName = staffName;
		this.deptNo = deptNo;
		this.salary = salary;
	}
	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public Integer getSalary() {
		return salary;
	}
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	
	
	
}
