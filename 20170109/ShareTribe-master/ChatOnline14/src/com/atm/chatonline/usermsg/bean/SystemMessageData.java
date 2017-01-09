package com.atm.chatonline.usermsg.bean;

import java.io.Serializable;
import java.util.List;

public class SystemMessageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 31132399988531299L;
	private int type;
	private List<Notification> message;
	private String userId;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<Notification> getMessage() {
		return message;
	}
	public void setMessage(List<Notification> message) {
		this.message = message;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
