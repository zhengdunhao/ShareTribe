package com.atm.chatonline.bbs.util;

import org.json.JSONArray;

import com.atm.chatonline.bbs.commom.UriAPI;

public class SendRecommend {
	private String tag="SendRecommend";
	private String respCode;
	private String name,time,reason;
	ChangeJson changeJson;
	
	public SendRecommend(String name,String time,String reason){
		this.name = name;
		this.time = time;
		this.reason =reason;
	}
	
	//返回教师的验证信息
		public String returnRecommend1() throws InterruptedException{
				JSONArray sendJsonArray = new ChangeJson(name,time,reason).returnRecommend();
				ConToServer t = new ConToServer(sendJsonArray,UriAPI.Recommend);
				LogUtil.p(tag, "sendJsonArray:"+sendJsonArray);
				t.join();
				t.run();
				respCode = t.returnRespCode();
				return respCode;
		}
}
