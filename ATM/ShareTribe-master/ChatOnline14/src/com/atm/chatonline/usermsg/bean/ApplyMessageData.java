package com.atm.chatonline.usermsg.bean;

import java.util.List;

public class ApplyMessageData {

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
