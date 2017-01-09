package com.atm.chatonline.usercenter.util;

import com.atm.chatonline.bbs.util.BBSConnectNet;

public class ReceiveMyLabel extends BBSConnectNet{
	
	public ReceiveMyLabel(){}
	public ReceiveMyLabel(String relativePath) {
		super(relativePath);
	}

	@Override
	public void setCookie(String cookie) {
		// TODO 自动生成的方法存根
		super.setCookie(cookie);
	}

	@Override
	public void Connect(String URL, int METHOD) {
		// TODO 自动生成的方法存根
		super.Connect(URL, METHOD);
	}

	@Override
	public String getResponse() {
		// TODO 自动生成的方法存根
		return super.getResponse();
	}
	
	
	
}
