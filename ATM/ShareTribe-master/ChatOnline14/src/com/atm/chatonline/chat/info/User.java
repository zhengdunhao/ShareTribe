package com.atm.chatonline.chat.info;

public class User {
	private static String userID;
	private static String pwd;
	private static String head;
	
	private static String nickName="ShareTribe";
	
	

	public User(){}
	
	public User(String userID,String pwd){
		this.userID=userID;
		this.pwd=pwd;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public static String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}
	
	public static String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
