package com.atm.chatonline.usermsg.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Notification implements Serializable{

	private int messageId;
	private int type;
	private String userId;
	private NotificationContent content;
	
	public Notification(int messageId, int type, String userId,
			NotificationContent content) {
		super();
		this.messageId = messageId;
		this.type = type;
		this.userId = userId;
		this.content = content;
	}
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
	public NotificationContent getContent() {
		return content;
	}
	public void setContent(NotificationContent content) {
		this.content = content;
	}
	
}
