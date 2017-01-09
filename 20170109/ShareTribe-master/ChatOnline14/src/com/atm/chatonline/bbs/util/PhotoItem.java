package com.atm.chatonline.bbs.util;

import java.io.Serializable;
/**
 * 
 * @implements Serializable
 *
 */
public class PhotoItem implements Serializable {
	private static final long serialVersionUID = 8682674788506891598L;
	private int  photoID;
	private boolean select;
	private String path;
	private int flag = 0;//flag=1代表是“添加图片”
	public PhotoItem(int id,String path) {
		photoID = id;
		select = false;
		this.path=path;
	}
	
	public PhotoItem(int id,boolean select) {
		photoID = id;
		this.select = select;
	}
	public PhotoItem(int id,int flag){
		photoID = id;
		this.flag = flag;
	}
	
	
	
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPhotoID() {
		return photoID;
	}
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	@Override
	public String toString() {
		return "PhotoItem [photoID=" + photoID + ", select=" + select + "]";
	}
}
