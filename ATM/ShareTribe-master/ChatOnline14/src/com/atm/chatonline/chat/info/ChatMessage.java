package com.atm.chatonline.chat.info;

import java.io.Serializable;

public class ChatMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String selfID;
	private String friendID;
	private String nickName;
	private int direction;
	private int type;
	private String time;
	private String content;
	private int showTime=0;
	
	
	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public ChatMessage(String selfID,String friendID,String nickName,int direction,int type,
			String time,String content,int showTime){
		this.selfID=selfID;
		this.friendID=friendID;
		this.nickName=nickName;
		this.direction=direction;
		this.type=type;
		this.time=time;
		this.content=content;
		this.showTime=showTime;
	}
	

	public int getShowTime() {
		return showTime;
	}


	public void setShowTime(int showTime) {
		this.showTime = showTime;
	}


	public ChatMessage(){
		
	}

	public String getSelfID() {
		return selfID;
	}

	public void setSelfID(String selfID) {
		this.selfID = selfID;
	}

	public String getFriendID() {
		return friendID;
	}

	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
}
