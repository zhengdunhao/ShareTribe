package com.atm.chatonline.usercenter.bean;

import java.io.Serializable;
import java.util.List;


public class LabelData implements Serializable{

	private static final long serialVersionUID = 3276176012014418576L;
	private String tip;
	private List<String> hotTag;
	private List<String> userTag;
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public List<String> getHotLabel() {
		return hotTag;
	}
	public void setHotLabel(List<String> hotLabel) {
		this.hotTag = hotLabel;
	}
	public List<String> getUserTag() {
		return userTag;
	}
	public void setUserTag(List<String> userTag) {
		this.userTag = userTag;
	}
	
}
