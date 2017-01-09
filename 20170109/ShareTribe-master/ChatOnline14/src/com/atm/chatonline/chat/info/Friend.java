package com.atm.chatonline.chat.info;

import android.graphics.Bitmap;

public class Friend {

	private String friendID;
	private String nickName;
	private String sex;
	private Bitmap bm;
	private int imageID;
	private String attentions;
	private String followers;
	private String signature;
	private String school;
	private String department;
	private String publishNoteNum;
	private String publishNoteTitle;
	private String publishEssayId;
	private String publishNoteDetail;
	private String collectNoteNum;
	private String collectNoteTitle;
	private String collectEssayId;
	private String collectNoteDetail;
	private int Role;
	private int relationship;
	private String otherID;
	private String userID;
	
	public String getOtherID() {
		return otherID;
	}



	public void setOtherID(String otherID) {
		this.otherID = otherID;
	}



	public String getUserID() {
		return userID;
	}



	public void setUserID(String userID) {
		this.userID = userID;
	}



	public int getRelationship() {
		return relationship;
	}



	public void setRelationship(int relationship) {
		this.relationship = relationship;
	}



	public int getRole() {
		return Role;
	}



	public void setRole(int role) {
		Role = role;
	}

	//用于查看其他人的关注列表
	public Friend(String userID,String friendID,String otherID,String nickName,String department,int relationship,Bitmap bm){
		this.userID = userID;
		this.friendID = friendID;
		this.otherID = otherID;
		this.nickName = nickName;
		this.department = department;
		this.bm = bm;
		this.relationship = relationship;
	}
	
	//用于查看自己的关注列表
	public Friend(String friendID,String nickName,String department,int relationship,Bitmap bm){
		
		this.friendID = friendID;
		this.nickName = nickName;
		this.department = department;
		this.bm = bm;
		this.relationship = relationship;
	}

	public Friend(){}
	
	
	
	public Friend(String friendID, String nickName, Bitmap bm) {
		super();
		this.friendID = friendID;
		this.nickName = nickName;
		this.bm = bm;
	}



	public Friend(String friendID, String nickName, String sex,
			String attentions, String followers, String signature,
			String school, String department, int relationship,String publishNoteNum,String publishEssayId,
			String publishNoteTitle, String publishNoteDetail,
			String collectNoteNum, String collectEssayId,String collectNoteTitle,
			String collectNoteDetail, Bitmap bm) {
		super();
		this.friendID = friendID;
		this.nickName = nickName;
		this.sex = sex;
		this.attentions = attentions;
		this.followers = followers;
		this.signature = signature;
		this.school = school;
		this.department = department;
		this.relationship = relationship;
		this.publishNoteNum = publishNoteNum;
		this.publishEssayId = publishEssayId;
		this.publishNoteTitle = publishNoteTitle;
		this.publishNoteDetail = publishNoteDetail;
		this.collectNoteNum = collectNoteNum;
		this.collectEssayId = collectEssayId;
		this.collectNoteTitle = collectNoteTitle;
		this.collectNoteDetail = collectNoteDetail;
		this.bm = bm;
	}



	public Friend(String nickName, Bitmap bm) {
		super();
		this.nickName = nickName;
		this.bm = bm;
	}



	//这个可以用在搜索列表
	public Friend(String friendID,String nickName,String department,String sex,Bitmap bm){
		this.friendID = friendID;
		this.nickName = nickName;
		this.department = department;
		this.sex = sex;
		this.bm = bm;
	}
	
	public Friend(String friendID,int imageID){
		this.friendID=friendID;
		this.imageID=imageID;
	}
	
	public int getImageID() {
		return imageID;
	}



	public void setImageID(int imageID) {
		this.imageID = imageID;
	}



	public String getAttentions() {
		return attentions;
	}



	public void setAttentions(String attentions) {
		this.attentions = attentions;
	}



	public String getFollowers() {
		return followers;
	}



	public void setFollowers(String followers) {
		this.followers = followers;
	}



	public String getSignature() {
		return signature;
	}



	public void setSignature(String signature) {
		this.signature = signature;
	}



	public String getSchool() {
		return school;
	}



	public void setSchool(String school) {
		this.school = school;
	}



	public String getDepartment() {
		return department;
	}



	public void setDepartment(String department) {
		this.department = department;
	}



	public String getPublishNoteNum() {
		return publishNoteNum;
	}



	public void setPublishNoteNum(String publishNoteNum) {
		this.publishNoteNum = publishNoteNum;
	}



	public String getPublishNoteTitle() {
		return publishNoteTitle;
	}



	public void setPublishNoteTitle(String publishNoteTitle) {
		this.publishNoteTitle = publishNoteTitle;
	}



	public String getPublishNoteDetail() {
		return publishNoteDetail;
	}



	public void setPublishNoteDetail(String publishNoteDetail) {
		this.publishNoteDetail = publishNoteDetail;
	}



	public String getCollectNoteNum() {
		return collectNoteNum;
	}



	public void setCollectNoteNum(String collectNoteNum) {
		this.collectNoteNum = collectNoteNum;
	}



	public String getCollectNoteTitle() {
		return collectNoteTitle;
	}



	public void setCollectNoteTitle(String collectNoteTitle) {
		this.collectNoteTitle = collectNoteTitle;
	}



	public String getCollectNoteDetail() {
		return collectNoteDetail;
	}



	public void setCollectNoteDetail(String collectNoteDetail) {
		this.collectNoteDetail = collectNoteDetail;
	}



	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}



	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Bitmap getBm() {
		return bm;
	}

	public void setBm(Bitmap bm) {
		this.bm = bm;
	}

	
	
	
	
	
	public String getFriendID(){
		return friendID;
	}
	
	public int getImageId(){
		return imageID;
	}



	public String getPublishEssayId() {
		return publishEssayId;
	}



	public void setPublishEssayId(String publishEssayId) {
		this.publishEssayId = publishEssayId;
	}



	public String getCollectEssayId() {
		return collectEssayId;
	}



	public void setCollectEssayId(String collectEssayId) {
		this.collectEssayId = collectEssayId;
	}
}
