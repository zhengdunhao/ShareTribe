package com.atm.chatonline.usermsg.bean;
/**
 * 实际上是@我的消息实体
 */
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

@SuppressWarnings("serial")
public class ApplyMessageContent implements Serializable{

	private int floorId;//评论楼层id
	private String replyTime;//评论时间
	private String replyId;//评论人的id
	private String nickname;//评论人昵称
	private String avatar;//评论人的头像路径
	private String essayId;//帖子id
	private String essayTitle;//帖子标题
	private String userId;//发帖人id
	public int getFloorId() {
		return floorId;
	}
	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
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
	public String getEssayId() {
		return essayId;
	}
	public void setEssayId(String essayId) {
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
