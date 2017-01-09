package com.atm.chatonline.bbs.bean;

import java.util.List;


public class Data {
	private String tip ;
	private List<BBS> bbs;
	private List<Department> department;
	transient private List<User> user;
	
	
	public List<User> getUser() {
		return user;
	}
	public void setUser(List<User> user) {
		this.user = user;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public List<BBS> getBbs() {
		return bbs;
	}
	public void setBbs(List<BBS> bbs) {
		this.bbs = bbs;
	}
	public List<Department> getDepartment() {
		return department;
	}
	public void setDepartment(List<Department> department) {
		this.department = department;
	}
	
}
