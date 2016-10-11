package com.atm.chatonline.usermsg.bean;

import java.util.List;

public class ReplyMessageData {

	private int type;
	private List<ReplyMessage> message;
	private String userId;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<ReplyMessage> getApplyMessage() {
		return message;
	}
	public void setApplyMessage(List<ReplyMessage> message) {
		this.message = message;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
