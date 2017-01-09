package com.atm.chatonline.bbs.util;
/**
 * 该工具类用于接收教师注册界面传来的信息，向服务端传递信息
 * 2015.7.24，atm--李
 * 
 * 我做了一些修改，ConToServer t=new ConToServer(sendJsonarray,UriAPI.USER_REGISTER);
 * -郑
 */

import org.json.JSONArray;
import org.json.JSONException;

import com.atm.chatonline.bbs.commom.UriAPI;




public class SendRegisterTeacher {
	private String respCode;
	private String userName,pwd,comfirmPwd,
	userSchool,userDept,userEmail,flag,schoolNum,schoolPwd;
	private int comfirmNumber;
	ChangeJson changeJson;
	//封装老师邮箱的验证信息
	public SendRegisterTeacher(String userEmail) {
		super();
		this.userEmail = userEmail;
	}
	//封装老师注册的信息
	public SendRegisterTeacher(String flag,String userName, String pwd, 
			String userSchool, String userDept,
			String userEmail) {
		super();
		this.flag = flag;
		this.userName = userName;
		this.pwd = pwd;
		this.userSchool = userSchool;
		this.userDept = userDept;
		this.userEmail = userEmail;
	}
	//封装老师验证的信息
	public SendRegisterTeacher(String flag,String userName, String schoolNum,String schoolPwd){
		super();
		this.flag = flag;
		this.userName = userName;
		this.schoolNum = schoolNum;
		this.schoolPwd = schoolPwd;
	}
	//true为注册成功，false为注册失败
	public String checkRegister() throws InterruptedException
	{
		JSONArray sendJsonarray=new ChangeJson(flag,userName,pwd,userSchool,userDept,userEmail).returnTeacherJsonarray();
		ConToServer t=new ConToServer(sendJsonarray,UriAPI.USER_REGISTER);
		t.run();
		respCode=t.returnRespCode();
		return respCode;
	}
	//返回邮箱是否有效的字符串（通过创建线程与服务器通信，）
	public String checkEmail()
	{
		try {
			
			JSONArray sendJsonArray=new ChangeJson(userEmail).returnEmailarray();
			ConToServer t=new ConToServer(sendJsonArray,UriAPI.CHECK_EMAIL);
			t.join();
			t.run();
			respCode=t.returnRespCode();
			changeJson=t.returnChangeJson();
			return respCode;
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		return respCode;
	}
	
	//返回教师的验证信息
	public String checkTeacher() throws InterruptedException{
			JSONArray sendJsonArray = new ChangeJson(flag,userName,schoolNum,schoolPwd).returnTeacher();
			ConToServer t = new ConToServer(sendJsonArray,UriAPI.CONFIRM_IDENTITY);
			t.join();
			t.run();
			respCode = t.returnRespCode();
			return respCode;
	}
	
	//请求告知服务器，用户不注册
	public void reqExitConfig()throws InterruptedException{
		JSONArray sendJsonArray = new ChangeJson(flag,userName,schoolNum,schoolPwd).returnExitConfig();
		ConToServer t = new ConToServer(sendJsonArray,UriAPI.REQUEST_CONFIG);
		t.join();
		t.run();
	}
	public ChangeJson getChangeJson(){
		return changeJson;
	}
}
