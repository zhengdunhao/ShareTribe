package com.atm.chatonline.bbs.util;
/**
 * 用于将学生和老师的注册信息封装为json数据格式，并返回一个jsonarray对象，以及对服务器返回的json
 * 数据进行解析，并返回一个boolean类型变量
 * 2015.7.25--李
 * 
 * 
 * 这里也做了修改，
 * private JSONArray jsonArr=new JSONArray();//teacherJsonarr,loginJsonarr,emailJsonarr;
	private JSONObject jsonObj=new JSONObject();//teacherJsonobj,loginJsonobj,emailJsonobj;
	-郑
 */
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ChangeJson {
	private String userName,pwd,comfirmPwd,
	userSchool,userDept,userMajor,userEmail,enterSchoolTime,Num,Pwd,schoolClass,stuName;
	private String flag;
	
	private String name,time,reason;
	private String tag = "ChangeJson";
	protected String emailNum;
	private JSONArray jsonArr=new JSONArray();//teacherJsonarr,loginJsonarr,emailJsonarr;
	private JSONObject jsonObj=new JSONObject();//teacherJsonobj,loginJsonobj,emailJsonobj;
	private JSONObject jsonObject;
	private HttpEntity entity;
	private String resStatus;
	public ChangeJson()
	{}
	
	
	public ChangeJson(String userEmail) {
		super();
		this.userEmail = userEmail;
//		emailJsonarr=new JSONArray();
//		emailJsonobj=new JSONObject();
	}

	public ChangeJson(String userName, String pwd) {
		super();
		this.userName = userName;
		this.pwd = pwd;
//		loginJsonarr=new JSONArray();
//		loginJsonobj=new JSONObject();
	}
	
	public ChangeJson(String name,String time,String reason){
		super();
		this.name = name;
		this.time = time;
		this.reason= reason;
	}

	public ChangeJson(String flag,String userName, String pwd, 
			String userSchool, String userDept, String userEmail) {
		super();
		this.flag =flag;
		this.userName = userName;
		this.pwd = pwd;
		this.userSchool = userSchool;
		this.userDept = userDept;
		this.userEmail = userEmail;
//		teacherJsonarr=new JSONArray();
//		teacherJsonobj=new JSONObject();
	}

	public ChangeJson(String flag,String userName, String pwd,
			String userSchool, String userDept, String userMajor,
			String userEmail, String enterSchoolTime) {
		super();
		this.flag = flag;
		this.userName = userName;
		this.pwd = pwd;
		this.userSchool = userSchool;
		this.userDept = userDept;
		this.userMajor = userMajor;
		this.userEmail = userEmail;
		this.enterSchoolTime = enterSchoolTime;
//		studentJsonarr=new JSONArray();
//		studentJsonobj=new JSONObject();
	}
	
	
	
	//封装在校生或老师的验证信息
	public ChangeJson(String flag,String userName, String Num,String Pwd){
		super();
		this.flag = flag;
		this.userName = userName;
		this.Num = Num;
		this.Pwd = Pwd;
	}
	//封装毕业生的验证信息
	public ChangeJson(String flag,String userName,String schoolClass, String Num,String stuName,String enterSchoolTime,String nothing){
		super();
		this.flag = flag;
		this.userName = userName;
		this.schoolClass =schoolClass;
		this.Num = Num;
		this.stuName =stuName;
		this.enterSchoolTime = enterSchoolTime;
	}
	
	//封装一个参数
	public JSONArray returnOneData(String key,String value){
		try {
			return new JSONArray().put(new JSONObject().putOpt(key, value));
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
		
	}
	
	public JSONArray returnTwoData(String firstKey,String firstValue,String secKey,String secValue){
		try {
			return new JSONArray().put(new JSONObject().put(firstKey, firstValue).put(secKey, secValue))
					;
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
		
	}
	
	//封装email信息
	public JSONArray returnEmailarray() throws JSONException
	{
		jsonObj.put("userEmail", userEmail);
		jsonArr.put(jsonObj);
		return jsonArr;
	}
	//封装登录信息
	public JSONArray returnLoginJsonarray()
	{
		
		try {
			jsonObj.put("userId", userName);
		/*	jsonObj.put("userPwd", pwd);*/	
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		jsonArr.put(jsonObj);
		
		return jsonArr;
	}
	//封装教师注册信息
	public JSONArray returnTeacherJsonarray()
	{
		
		try {
			jsonObj.put("flag", flag);
			jsonObj.put("userId", userName);
			jsonObj.put("userPwd", pwd);
			jsonObj.put("userSchool", userSchool);
			jsonObj.put("userDept", userDept);
			jsonObj.put("userEmail", userEmail);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		jsonArr.put(jsonObj);
		
	
	return jsonArr;
	}
	//封装学生注册信息
	public JSONArray returnStudentJsonarray()
	{
		
			try {
				jsonObj.put("flag", flag);
				jsonObj.put("userId", userName);
				jsonObj.put("userPwd", pwd);
				jsonObj.put("userSchool", userSchool);
				jsonObj.put("userDept", userDept);
				jsonObj.put("userMajor", userMajor);
				jsonObj.put("userEmail", userEmail);
				jsonObj.put("enterSchoolTime", enterSchoolTime);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			jsonArr.put(jsonObj);	
	
		return jsonArr;
	}
	//封装在校生验证信息
	public JSONArray returnInternalStudent(){
		try{
			jsonObj.put("flag", flag);//internalStu="1"
			jsonObj.put("userId", userName);
			jsonObj.put("Num", Num);
			jsonObj.put("Pwd", Pwd);
		}catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		jsonArr.put(jsonObj);
		return jsonArr;
	}
	//封装教师验证信息
	public JSONArray returnTeacher(){
		try{
			jsonObj.put("flag", flag);//internalStu="1"
			jsonObj.put("userId", userName);
			jsonObj.put("Num", Num);
			jsonObj.put("Pwd", Pwd);
		}catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		jsonArr.put(jsonObj);
		return jsonArr;
	}
	
	//封装毕业生验证信息
		public JSONArray returnGraduateStudent(){
			try{
				jsonObj.put("flag", flag);//internalStu="1"
				jsonObj.put("userId", userName);
				jsonObj.put("schoolClass", schoolClass);
				jsonObj.put("Num", Num);
				jsonObj.put("userName", stuName);
				jsonObj.put("enterSchoolTime", enterSchoolTime);
			}catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			jsonArr.put(jsonObj);
			return jsonArr;
		}
	//封装请求服务器删除学生的信息记录
	public JSONArray returnExitConfig(){
		try{
			jsonObj.put("userId", userName);
		}catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		jsonArr.put(jsonObj);
		return jsonArr;
	}
	//返回推荐校友
	public JSONArray returnRecommend(){
		try{
			jsonObj.put("name", name);
			jsonObj.put("time", time);
			jsonObj.put("reason", reason);
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}
		jsonArr.put(jsonObj);
		return jsonArr;
	}
	 //目前对于服务器端只返回一个参数有效
	public String returnResult(HttpEntity entity)
	{
		this.entity=entity;
		try {
			String out = EntityUtils.toString(entity, "UTF-8");
			Log.i(tag, "<----------------->");
			Log.i(tag, out);
			Log.i(tag, "<----------------->");
			JSONArray jsonarray=new JSONArray(out);
			for(int i=0;i<jsonarray.length();i++)
			{
				Log.i(tag, "!!!!!");
				jsonObject=jsonarray.getJSONObject(i);
				
				String result=jsonObject.getString("tip");
				Log.i(tag, "@@@@@@");
				if(jsonObject.length()>1){
					Log.i(tag, "######");
					emailNum = jsonObject.getString("captchas");//--------------------------------修改2015.9.9-郑
				}
				Log.i(tag, result);
				resStatus=result;
			}
			Log.i(tag, "resStatus:"+resStatus);
		} catch (ParseException e) {
			Log.i(tag, "1111111111111");
			e.printStackTrace();
			return "数据解析异常";
		} catch (IOException e) {
			Log.i(tag, "222222222222");
			e.printStackTrace();
			return "服务器无响应";
		} catch (JSONException e) {
			Log.i(tag, "33333333333");
			e.printStackTrace();
			return "服务器返回异常";
		}
		return resStatus;
	}
	
	


	public String getEmailNum() {
		return emailNum;
	}


	public void setEmailNum(String emailNum) {
		this.emailNum = emailNum;
	}
	
	public JSONObject getJSONObject(){
		return jsonObject;
	}
	
}
