package com.atm.chatonline.bbs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类用来检查用户输入的信息是否合法
 * @author Jackbing
 *
 */
public class CkeckRegisterMessage {
	
	private boolean flag=false;
	private String userName,pwd,comfirmPwd,
	userEmail;
	private int errorCode;
	public CkeckRegisterMessage(){}
	
	public CkeckRegisterMessage(String userName, String pwd, String comfirmPwd
			) {
		super();
		this.userName = userName;
		this.pwd = pwd;
		this.comfirmPwd = comfirmPwd;
	}
	//利用正则表达式验证邮箱是否合法
	public boolean checkEmail(String userEmail)
	{
		this.userEmail=userEmail;
		String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(userEmail);
		return m.matches();
	}
	//判断用户名密码确认密码长度，用户名密码是否合法，密码和确认密码是否相同
	public int returnNumber()
	{
		//判断输入是否为空
		if(userName.equals("")&&!pwd.equals("")&&!comfirmPwd.equals("")&&!userEmail.equals(""))
		{
			errorCode=101;
		}else if(true)
		{
			
		}
		return errorCode;
	}
	
}
