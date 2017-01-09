package com.atm.chatonline.usermsg.bean;

import java.io.Serializable;
import java.util.List;

public class ReplyMessageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1771337879449918880L;
	private int type;
	private List<ReplyMessage> message;
	private String userId;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<ReplyMessage> getReplyMessage() {
		return message;
	}
	public void setReplyMessage(List<ReplyMessage> message) {
		this.message = message;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
