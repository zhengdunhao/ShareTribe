package com.atm.chatonline.chat.info;

import java.io.Serializable;

public class GroupChatMessage implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MESSAGE_FROM = 0;
	public static final int MESSAGE_TO = 1;
	private String senderId;
	private String groupId;
	 private int direction;
	 private String content;
	 private int headImg;
	 private String time;
	 private int type;
	 private int showTime=0;
	 
	 public GroupChatMessage(String senderId, String groupId, int direction,
			String content, int headImg, String time, int type,int showTime) {
		this(senderId,groupId,direction,content,headImg,time,showTime);
		
		this.type = type;
	}
	public GroupChatMessage(String senderId, String groupId, int direction,
			String content, int headImg, String time,int showTime) {
		 this(direction,content,headImg,time,showTime);
		this.senderId = senderId;
		this.groupId = groupId;
	}
	public GroupChatMessage(int direction, String content,int headImg,String time,int showTime) {
	  super();
	  this.direction = direction;
	  this.content = content;
	  this.headImg=headImg;
	  this.time=time;
	  this.showTime=showTime;
	 }
	
	 public int getShowTime() {
		return showTime;
	}
	public void setShowTime(int showTime) {
		this.showTime = showTime;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public GroupChatMessage(){}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getHeadImg() {
		return headImg;
	}

	public void setHeadImg(int headImg) {
		this.headImg = headImg;
	}

	public int getDirection() {
	  return direction;
	 }
	 public void setDirection(int direction) {
	  this.direction = direction;
	 }
	 public void setContent(String content) {
	  this.content = content;
	 }
	 public String getContent() {
	  return content;
	 }
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	 
	}