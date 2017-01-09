package com.atm.chatonline.usercenter.bean;

import java.util.List;

import com.atm.chatonline.bbs.bean.BBS;

public class MyLabelOfBBS {
	private String tip ;
	private List<BBS> bbs;
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public List<BBS> getBbs() {
		return bbs;
	}
	public void setBbs(List<BBS> bbs) {
		this.bbs = bbs;
	}
	
}
