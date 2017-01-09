package com.atm.chatonline.bbs.util;

/**
 * 这里做了修改ConToServer t=new ConToServer(sendJsonArray,UriAPI.USER_LOGIN);
 * -郑
 * */
import org.json.JSONArray;

import com.atm.chatonline.bbs.commom.UriAPI;

public class SendLoginInfo{
	private String username,pwd;
	private String respCode;
	private ConToServer t;
	private String cookie;
	//自定义构造函数
	public SendLoginInfo(String username,String pwd)
	{
		this.username=username;
		this.pwd=pwd;
	}

	//登录验证，登陆成功,flag为true，否则为false
	public String checkLoginInfo() throws InterruptedException{
		
		JSONArray sendJsonArray=new ChangeJson(username,null).returnLoginJsonarray();
		t=new ConToServer(sendJsonArray,UriAPI.USER_LOGIN);
		t.join();
		t.run();
		cookie = t.getCookie();
		respCode=t.returnRespCode();
		return respCode;
	}
	public String getCookie() {
		return cookie;
	}
}
