package com.atm.chatonline.bbs.util;

/**
 * 这里也是做了修改ConToServer t=new ConToServer(sendJsonArray,UriAPI.USER_REGISTER);
 * -郑
 * */
import org.json.JSONArray;
import org.json.JSONException;

import com.atm.chatonline.bbs.commom.UriAPI;



public class SendRegisterStudent {
		private String flag,userName,pwd,comfirmPwd,
		userSchool,userDept,userMajor,userEmail,enterSchoolTime,schoolNum,schoolPwd,schoolClass,stuName;
		ChangeJson changeJson;
		private static String respCode;
		
		public SendRegisterStudent(String userEmail) {
			super();
			this.userEmail = userEmail;
		}
		
		public SendRegisterStudent(String userName,String userEmail){
			super();
			this.userName = userName;
			this.userEmail = userEmail;
		}
		
		
		public SendRegisterStudent(String flag,String userName, String pwd,
				String userSchool, String userDept, String userMajor,
				String userEmail, String enterSchoolTime) {
			super();
			this.flag=flag;
			this.userName = userName;
			this.pwd = pwd;
			this.userSchool = userSchool;
			this.userDept = userDept;
			this.userMajor = userMajor;
			this.userEmail = userEmail;
			this.enterSchoolTime = enterSchoolTime;
			
		}
		
		public SendRegisterStudent(String flag,String userName, String schoolNum,String schoolPwd){
			super();
			this.flag = flag;
			this.userName = userName;
			this.schoolNum = schoolNum;
			this.schoolPwd = schoolPwd;
		}
		//毕业生验证信息
		public SendRegisterStudent(String flag,String userName,String schoolClass,String schoolNum,String stuName,String enterSchoolTime){
			super();
			this.flag = flag;
			this.userName = userName;
			this.schoolClass = schoolClass;
			this.schoolNum = schoolNum;
			this.stuName = stuName;
			this.enterSchoolTime = enterSchoolTime;
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
	
		
		//返回注册情况的编码串所对应的服务器端情况
		public String checkRegister() throws InterruptedException
		{
			JSONArray sendJsonArray=new ChangeJson(flag,userName,pwd
					,userSchool,userDept,userMajor,userEmail,enterSchoolTime).returnStudentJsonarray();
			ConToServer t=new ConToServer(sendJsonArray,UriAPI.USER_REGISTER);
			t.join();
			t.run();
			respCode=t.returnRespCode();
			return respCode;
		}
		
		//返回在校生的验证信息
		public String checkInternalStu() throws InterruptedException{
			JSONArray sendJsonArray = new ChangeJson(flag,userName,schoolNum,schoolPwd).returnInternalStudent();
			ConToServer t = new ConToServer(sendJsonArray,UriAPI.CONFIRM_IDENTITY);
			t.join();
			t.run();
			respCode = t.returnRespCode();
			return respCode;
		}
		
		public String checkGraduateStu()throws InterruptedException{
			JSONArray sendJsonArray = new ChangeJson(flag,userName,schoolClass,schoolNum,stuName,enterSchoolTime,"").returnGraduateStudent();
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
