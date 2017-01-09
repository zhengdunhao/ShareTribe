package com.atm.chatonline.chat.info;

public class MoreChoose {

	private int imageID;
	private String name;
	
	public MoreChoose(String name,int imageID){
		this.name=name;
		this.imageID=imageID;
	}
	
	public int getImageID() {
		return imageID;
	}
	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
