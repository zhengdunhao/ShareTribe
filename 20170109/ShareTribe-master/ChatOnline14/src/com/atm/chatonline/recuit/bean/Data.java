package com.atm.chatonline.recuit.bean;

import java.util.List;

public class Data {
	private String tip ;
	private List<Recuit> recuit;
	private List<Apply> apply;
	
	public List<Apply> getApply() {
		return apply;
	}
	public void setApply(List<Apply> apply) {
		this.apply = apply;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public List<Recuit> getRecuit() {
		return recuit;
	}
	public void setRecuit(List<Recuit> recuit) {
		this.recuit = recuit;
	}
}
