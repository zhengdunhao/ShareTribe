/**
 * 
 */
package com.atm.chatonline.bbs.bean;

import com.atm.chatonline.bbs.util.LogUtil;

import android.graphics.drawable.Drawable;

/**
 * @类 com.atm.charonline.bbs.bean ---user
 * @作用 用于搜索用户
 * @作者 陈小二
 * @时间 2015-11-2
 * 
 */
public class User {
	private String userId; //用户ID
	private String nickname;   //发布者昵称（15）
	private String sex;   //用户性别（15）
    private String headImagePath; //头像路径
	private String dName; //系别
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	private Drawable HeadImage;
	public Drawable getHeadImage() {
		return HeadImage;
	}
	public void setHeadImage(Drawable headImage) {
		HeadImage = headImage;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
		LogUtil.d(userId);
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadImagePath() {
		return headImagePath;
	}
	public void setHeadImagePath(String headImagePath) {
		this.headImagePath = headImagePath;
	}
}
