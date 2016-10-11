package com.atm.charonline.recuit.common;
/*
 * 这个是放所有需要连接后台服务器的地址，不同的功能对应着不同的地址。
 * 2015-7-28-郑
 * */
public class UriAPI {
	public final static String USER_REGISTER="http://139.129.131.179/ATM/user_register.action";//192.168.191.1：9999  192.168.199.205
	public  final static  String USER_LOGIN="http://139.129.131.179/ATM/user_loginConfirm.action";
	public final static String subURL = "http://139.129.131.179/ATM/";
	public final static String CONFIRM_IDENTITY="http://139.129.131.179/ATM/user_userConfirm.action"; 
	public final static String REQUEST_CONFIG=""; 
	public final static String CHECK_EMAIL="http://139.129.131.179/ATM/user_confirmRegisterEmail.action";
}
