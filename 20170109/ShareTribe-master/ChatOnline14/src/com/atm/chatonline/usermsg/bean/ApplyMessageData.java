package com.atm.chatonline.usermsg.bean;

import java.io.Serializable;
import java.util.List;

public class ApplyMessageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1093654656449529135L;
	private int type;
	private List<ApplyMessage> message;
	private String userId;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<ApplyMessage> getApplyMessage() {
		return message;
	}
	public void setApplyMessage(List<ApplyMessage> message) {
		this.message = message;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
