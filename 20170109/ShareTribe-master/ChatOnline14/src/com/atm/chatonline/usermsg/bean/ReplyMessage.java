package com.atm.chatonline.usermsg.bean;

import java.io.Serializable;

public class ReplyMessage implements Serializable{

	private int messageId;
	private int type;
	private String userId;
	private ReplyMessageContent content;
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public ReplyMessageContent getContent() {
		return content;
	}
	public void setContent(ReplyMessageContent content) {
		this.content = content;
	}
	
	
	
	
}
