package com.atm.chatonline.chat.util;

/**
 * 规划整理Config.更加系统地枚举每一个可能事件 {聊天消息类型(1001-1110)} {基本类型操作(100-999)}
 * {群操作(1200-1399)} {用户操作(1400-1599)}{成功:1,失败:0} 每个50之后为预留账号 2000-3000墩豪装用
 * 3001-4000丹霞装用
 * 
 * @version 2.0
 * @author ye
 * @2015.8.18
 */
public interface Config {

	// * 有*字样表示名字有改动
	/**
	 * 聊天消息类型
	 */
	// 私聊(
	public static final int MESSAGE_TO = 1001;// 发送私聊消息
	public static final int MESSAGE_FROM = 1002;// 接收私聊消息
	public static final int MESSAGE_TEXT = 10003;// *私聊文本消息
	public static final int MESSAGE_IMG = 1004;// *私聊图片消息
	public static final int MESSAGE_SOUND = 1005;// * 群聊语音消息(未开发，敬请期待)
	public static final int MESSAGE_AUDIO = 1006;// *群聊视屏消息(未开发，敬请期待)
	public static final int MESSAGE_SUCCESS = 1007;// 消息发送成功
	public static final int MESSAGE_FAILED = 1008; // 消息发送失败
	public static final int MESSAGE_OFFLINE = 1009;// 获取离线消息

	// 群聊天
	public static final int CROWD_MESSAGE_TO = 1101; // 发送群聊消息
	public static final int CROWD_MESSAGE_FROM = 1102;// 接收群聊消息
	public static final int CROWD_MESSAGE_TEXT = 1103; // 群聊文本消息
	public static final int CROWD_MESSAGE_IMG = 1104;// 群聊图片消息
	public static final int CROWD_MESSAGE_SOUND = 1105;// 群聊语音消息(未开发，敬请期待)
	public static final int CROWD_MESSAGE_AUDIO = 1106;// 群聊视屏消息(未开发，敬请期待)
	public static final int CROWD_MESSAGE_SUCCESS = 1107;// 群聊消息发送成功
	public static final int CROWD_MESSAGE_FAILED = 1108; // 群聊消息发送失败

	/** 基本的请求类型 **/
	public static final int REQUEST_LOGIN = 100;// 登录
	public static final int REQUEST_REGIST = 101;// 注册
	public static final int REQUEST_EXIT = 102;
	public static final int REQUEST_BE_OFF = 103;
	public static final int REFRESH_UI = 99; // 更新UI视图 *敦豪的

	public static final int RESULT_LOGIN = 200;
	public static final int RESULT_REGIST = 201;
	public static final int RESULT_UPDATE_HEAD = 202;

	public static final int IMG_NEED_UPDATE = 600;
	public static final int IMG_NO_UPDATE = 601;

	/**
	 * 群的操作
	 */
	public static final int CROWD_CREATE = 1201;// 创建群聊
	public static final int CROWD_MY = 1202;// 查找我的群
	public static final int CROWD_FIND = 1203;// 查找群
	public static final int CROWD_INTIVE = 1204;// 邀请进群
	public static final int CROWD_APPLY = 1205;// 申请进群
	public static final int CROWD_GET_INFO = 1206;// 群信息
	public static final int CROWD_GET_HEAD = 1207;// 获取群头像
	public static final int CROWD_SET_INFO = 1208;// 群员已经在群中
	public static final int CROWD_SET_HEAD = 1209;// 设置群头像
	public static final int CROWD_FOUND = 1210;// found群 可能感兴趣的群或者热门群

	/**
	 * 群操作的回复
	 */
	public static final int CROWD_RESULT_CREATE = 1301; // 创建群聊结果
	public static final int CROWD_RESULT_MY = 1302;// 查找我的群的结果
	public static final int CROWD_RESULT_FIND = 1303;// 查找群的结果
	public static final int CROWD_RESULT_INTIVE = 1304; // 邀请结果
	public static final int CROWD_RESULT_APPLY = 1305;// 申请进群结果
	public static final int CROWD_RESULT_GETINFO = 1306;// 群消息
	public static final int CROWD_RESULT_GETHEAD = 1307;// 申请进群结果
	public static final int CROWD_RESULT_SETINFO = 1308;// 申请进群结果
	public static final int CROWD_RESULT_SETHEAD = 1309;// 申请进群结果
	public static final int CROWD_RESULT_FOUND = 1310;// 查找可能感兴趣群的结果

	public static final int CROWD_LIST = 1321; // 群列表
	public static final int CROWD_USER_ALREADY = 1322; // 群员已经在群中
	public static final int CROWD_FULL = 1323; // 群满人
	public static final int CROWD_NO_EXIST = 1324;// 群不存在

	/**
	 * 我的消息
	 */
	public static final int MY_MESSAGE = 1154; 
	public static final int ISHAS_NEW_MYMESSAGE = 1153; // 是否有新的我的消息
	
	public static final int CROWD_FIND_SUCCESS = 1350;// 查找群成功后，判断是否调用更新搜索群方法---郑
	/**
	 * 用户操作
	 */
	public static final int USER_LIST = 1401;// 用户列表
	public static final int USER_GET_ATTENT = 1402;// 我关注的
	public static final int USER_GET_ATTENTED = 1403;// 关注我的
	public static final int USER_GET_INFO = 1404;// 用户信息
	public static final int USER_GET_HEAD = 1405;// 用户头像
	public static final int USER_ADD_ATTENT = 1406;// 添加关注
	public static final int USER_CANCEL_ATTENT = 1407;// 取消关注
	public static final int USER_SET_INFO = 1408;// 修改资料
	public static final int USER_SET_HEAD = 1409;// 修改头像
	public static final int USER_FIND = 1410;// 查找用户

	public static final int USER_FIND_SUCCESS = 1450; // 查找用户成功后，判断是否调用更新搜索用户方法---郑
	public static final int USER_FOUND = 1411;// 查找可能感兴趣的用户
	public static final int USER_OTHER_ATTENT = 1412;// 别人关注的
	public static final int USER_OTHER_FANS = 1413;// 别人的粉丝

	// 用户操作结果
	public static final int USER_RESULT_LIST = 1501;// 用户列表
	public static final int USER_RESULT_GETATTENT = 1502;// 我关注的
	public static final int USER_RESULT_GETATTENTED = 1503;// 关注我的
	public static final int USER_RESULT_GETINFO = 1504;// 用户信息
	public static final int USER_RESULT_GETHEAD = 1505;// 用户头像
	public static final int USER_RESULT_ADDATTENT = 1506;// 添加关注
	public static final int USER_RESULT_CANCELATTENT = 1507;// 取消关注
	public static final int USER_RESULT_SETINFO = 1508;// 修改资料
	public static final int USER_RESULT_SETHEAD = 1509;// 修改头像
	public static final int USER_RESULT_FIND = 1510;// 查找用户
	public static final int USER_RESULT_FOUND = 1511;// 查找感兴趣的用户的结果
	public static final int USER_RESULT_OATTENT = 1512;// 获取别人的关注的结果
	public static final int USER_RESULT_OFANS = 1513;// 获取别人的粉丝的结果

	// 其他
	public static final int USER_NO_FOUND = 1520;// 用户没找到
	public static final int USER_LOGIN_ALREADY = 1521;// 用户已登录
	public static final int USER_NO_ONLINE = 1522; // 用户不在线

	// 墩豪
	public static final int NOT_FOUND_HOBBY_GOURP = 2000;// 没有找到感兴趣的群
	public static final int NOT_FOUND_HOBBY_USER = 2001;// 没有找到感兴趣的用户
	public static final int RELATIONSHIP_ATTENTION = 2002;// 关注标志
	public static final int RELATIONSHIP_FOLLOER = 2003;// 粉丝标志
	public static final int RELATIONSHIP_NO_MATTER = 2004;// 没有关系的标志
	public static final int USER_ATTENTION_LIST_SUCCESS = 2005; // 找到了我的关注列表
	public static final int USER_HEAD_SCUUESS = 2006;// 成功拿到用户的头像
	public static final int SEND_NOTIFICATION = 2007;// 发送消息通知
	public static final int REFRESH_TOP_NEWS = 2008;// 更新头条新闻
	public static final int LOGIN_SUCCESS = 2009;// 登入成功
	public static final int PERSONALMESSAGEACTIVITY = 2010;// 代表来自PersonalMessageActivity
	public static final int BBSPOSTDETAILVIEW = 2011;// 代表来自BBSPostDetailView
	public static final int USER_CENTER = 2012;// 代表来自User_Center
	public static final int CHANGE_PWD = 2013;//更改密码
	public static final int CHANGE_EMAIL = 2014;//换绑邮箱
	public static final int FEEDBACK_MESSAGE = 2015;//反馈消息
	

	// other
	public static final int USER_MOVE = 9998;// 移动用户
	public static final int MAP_INFO = 9999;
	/**
	 * 对许多操作的同意回复
	 */
	public static final int NOT_FOUND = 2;// 对搜索的操作的回复--没有找到
	public static final int SUCCESS = 1;
	public static final int FAILED = 0;
	public static final int AUTOLOGIN = 3;// 自动登录
	public static final int FIRSTLOGIN = 4;// 第一次登录
	public static final int BE_OFF_LOGIN = 5;// 下线之后，跳转到登录页面
	public static final int USED = 6;//表示已被使用

	// 陈丹霞
	public static final int MAINVIEW_UPDATEUI = 3001;// 主界面更新
	public static final int FULLTIME = 3002;// 全职的更新
	public static final int ALLKIND = 3003;// 全部职业的更新
	public static final int PARTTIME = 3004;// 兼职的更新
	public static final int INTERNSHIP = 3005;// 实习的更新
	public static final int SEARCHESSAY = 3006;// 查找帖子的更新
	public static final int SEARCHUSER = 3007;// 查找用户的更新
	public static final int LOGIN_AFTER_REGISTER=6;//注册之后再登录
}
