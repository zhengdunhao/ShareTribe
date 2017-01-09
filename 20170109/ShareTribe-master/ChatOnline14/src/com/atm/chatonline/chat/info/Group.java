package com.atm.chatonline.chat.info;

import android.graphics.Bitmap;

public class Group {
	private String groupId;
	private String imgId;
	private int headId;
	private String groupDesc;
	private int groupPorperty;
	private String groupName;
	private String groupLabel;
	private Bitmap bm;
	private String peopleNum;
	
	public Group(){}
	
	
	public Group(String groupId,String groupName,String peopleNum,Bitmap bm){
		this.groupId = groupId;
		this.groupName = groupName;
		this.peopleNum = peopleNum;
		this.bm=bm;
	}
	
	public Group(String groupId, String imgId, String groupDesc,
			int groupPorperty, String groupName, String groupLabel) {
		super();
		this.groupId = groupId;
		this.imgId = imgId;
		this.groupDesc = groupDesc;
		this.groupPorperty = groupPorperty;
		this.groupName = groupName;
		this.groupLabel = groupLabel;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public int getGroupPorperty() {
		return groupPorperty;
	}
	public void setGroupPorperty(int groupPorperty) {
		this.groupPorperty = groupPorperty;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Group(String groupId, int headId) {
		super();
		this.groupId = groupId;
		this.headId = headId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getGroupLabel() {
		return groupLabel;
	}

	public void setGroupLabel(String groupLabel) {
		this.groupLabel = groupLabel;
	}

	public int getHeadId() {
		return headId;
	}

	public void setHeadId(int headId) {
		this.headId = headId;
	}

	public Bitmap getBm() {
		return bm;
	}

	public void setBm(Bitmap bm) {
		this.bm = bm;
	}
	public String getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(String peopleNum) {
		this.peopleNum = peopleNum;
	}
	
}
