package com.atm.chatonline.usermsg.bean;
/**
 * 平论消息实题
 */
import java.io.Serializable;

import android.graphics.Bitmap;

public class ReplyMessageContent implements Serializable{

	private String createTime;
	private String nickname;
	private String avatar;
	private int essayId;
	private String essayTitle;
	private String userId;
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getEssayId() {
		return essayId;
	}
	public void setEssayId(int essayId) {
		this.essayId = essayId;
	}
	public String getEssayTitle() {
		return essayTitle;
	}
	public void setEssayTitle(String essayTitle) {
		this.essayTitle = essayTitle;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	transient private Bitmap HeadImage;

	public Bitmap getHeadImage() {
		return HeadImage;
	}
	public void setHeadImage(Bitmap headImage) {
		HeadImage = headImage;
	}
	
}
