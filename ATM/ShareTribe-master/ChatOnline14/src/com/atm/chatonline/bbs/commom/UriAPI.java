package com.atm.chatonline.bbs.commom;
/*
 * 这个是放所有需要连接后台服务器的地址，不同的功能对应着不同的地址。
 * 2015-7-28-郑
 * */
public class UriAPI {
	public final static String USER_REGISTER="http://139.129.131.179/ATM/user_register.action";//192.168.191.1：9999  192.168.199.205
	public  final static  String USER_LOGIN="http://139.129.131.179/ATM/user_loginConfirm.action";
	public final static String SUB_URL = "http://139.129.131.179/ATM/";
	public final static String CONFIRM_IDENTITY="http://139.129.131.179/ATM/user_userConfirm.action"; 
	public final static String REQUEST_CONFIG=""; 
	public final static String CHECK_EMAIL="http://139.129.131.179/ATM/user_confirmRegisterEmail.action";
	public final static String FIND_USERNAME="http://139.129.131.179/ATM/user_findUserId.action";//找回账户的url
	public final static String FORGET_PASSWORD="http://139.129.131.179/ATM/user_forgetPassword.action";//忘记密码的url
	public final static String UPDATE_PASSWORD="http://139.129.131.179/ATM/user_changePassword.action";
	public final  static String USER_APPEAL="http://139.129.131.179/ATM/appeal.do";//用户申诉
	public final static String CHANEG_USER_INFO="http://139.129.131.179/ATM/user/changeUserInfo.do";
	public final static String USER_CENTER="";
	public final static String BASIC_MESSAGE="http://139.129.131.179/ATM/user/userInfoJ.do";
	public final static String Recommend="http://139.129.131.179/ATM/RecommendSm/recommendSm.do";
	public final static String APP_ID="wx49dfff6ceb8cbbba";
}
//public class UriAPI {
//public final static String USER_REGISTER="http://192.168.191.1:8080/ATM/user_register.action";//192.168.191.1：9999  192.168.199.205
//public  final static  String USER_LOGIN="http://192.168.191.1:8080/ATM/user_loginConfirm.action";
//public final static String subURL = "http://192.168.191.1:8080/ATM/user_loginConfirm.action";
//public final static String CONFIRM_IDENTITY="http://192.168.191.1:8080/ATM/user_userConfirm.action"; 
//public final static String REQUEST_CONFIG=""; 
//public final static String CHECK_EMAIL="http://192.168.191.1:8080/ATM/user_confirmRegisterEmail.action";
//public final static String FIND_USERNAME="http://192.168.191.1:8080/ATM/user_findUserId.action";//找回账户的url
//public final static String FORGET_PASSWORD="http://192.168.191.1:8080/ATM/user_forgetPassword.action";//忘记密码的url
//public final static String UPDATE_PASSWORD="http://192.168.191.1:8080/ATM/user_changePassword.action";
//public final  static String USER_APPEAL="http://192.168.191.1:8080/ATM/appeal.do";//用户申诉
//
//}