package com.atm.chatonline.chat.net;

import java.io.IOException;
import java.util.Map;

import android.util.Log;

import com.atm.chatonline.chat.ui.ReceiveInfoListener;



public class Communication {

	public NewNetWorker newNetWorker01;
	private static Communication instance;
	private String tag="Communication";
	public Communication() {
		newNetWorker01 = new NewNetWorker();
		newNetWorker01.start();
	}

	public static Communication newInstance() {
		if (instance == null)
			instance = new Communication();
			Log.i(">>>>>", "666");
		return instance;
	}
	//添加监听
	public void addReceiveInfoListener(String state,
			ReceiveInfoListener listener) {
		newNetWorker01.addReceiveInfoListener(state, listener);
	}
	
	public void removeReceiveInfoListener(){
		newNetWorker01.removeReceiveInfoListener();
	}
	
//	//注销监听
//	public void unRegisterReceiveInfoListener(String state){
//		if(newNetWorker01.listeners.containsKey(state)){
//			newNetWorker01.listeners.remove(state);
//		}
//	}
	//登录请求
	public void reqLogin(String userID, String pwd) {
		if(newNetWorker01.sendMsg==null){
			Log.i("communication", "senmsg is null");
		}else{
			Log.i("communication", "senmsg is not null");
		}
		Log.i(tag, "userID:"+userID+"、pwd："+pwd);
		newNetWorker01.sendMsg.login(userID, pwd);
	}

	// 请求推送感兴趣的群
	public void reqHobbyGroup(String userId) {
		newNetWorker01.sendMsg.reqHobbyGroup(userId);
	}

	// 请求推送感兴趣的用户
	public void reqHobbyUser(String userId) {
		newNetWorker01.sendMsg.reqHobbyUser(userId);
	}

	// 请求查看朋友信息
	public void reqPersonINFO(String friendID) {
		newNetWorker01.sendMsg.reqPersonINFO(friendID);
	}

	// 请求关注
	public void reqAttention(String userID, String friendID) {
		newNetWorker01.sendMsg.reqAttention(userID, friendID);
	}

	// 请求取消关注
	public void reqCanncel(String userID, String friendID) {
		newNetWorker01.sendMsg.reqCanncel(userID, friendID);
	}
	//发送私信文本
	public boolean sendText(String userID, String friendID, String time,
			String content) {
		return newNetWorker01.sendMsg.sendText(userID, friendID, time, content);
	}
//发送群聊文本
	public boolean sendGroupText(String userId, String groupId, String content) {

		return newNetWorker01.sendMsg.sendCrowdMessage(userId,
				Integer.parseInt(groupId), content);
	}
//发送创建群聊请求
	public void sendCreateGroupMsg(String userId, String groupName,
			String groupDesc, String groupLabel, int groupProperty,
			String groupHeadImg, String path) throws IOException {// ***
		newNetWorker01.sendMsg.sendCreateGroup(userId, groupName, groupDesc,
				groupLabel, groupProperty, groupHeadImg, path);
	}
//发送查看群资料请求
	public void sendLookGroupData(int groupId) {
		newNetWorker01.sendMsg.sendLookGroupData(groupId);
	}
//查找群请求
	public void findCrowd(String condition, String userId) {
		newNetWorker01.sendMsg.findCrowd(condition, userId);
	}
//发送查找自己群的请求
	public void sendFindMyGroup(String userId) {
		newNetWorker01.sendMsg.sendFindMyGroup(userId);
	}
	//查找好友列表请求
	public void findUser(String condition,String userId) {
		newNetWorker01.sendMsg.findUser(condition,userId);
	}
	
	//获取私聊的离线消息
	public void getOfflineMessage(String userId){
		newNetWorker01.sendMsg.getOfflineMessage(userId);
	}
	
	//发送退出退出程序的命令给服务器
	public void exit(String userId){
		newNetWorker01.sendMsg.exit(userId);
		
	}
	//关闭socketchannel
	
	public void shutDownSocketChannel(){
		newNetWorker01.sendMsg.close();
		
	}
	
	//打开socketchannel
	public void openSocketChannel(){
//		newNetWorker01.connect();
//		newNetWorker01.interrupt();
//		newNetWorker01.start();
		newNetWorker01 = new NewNetWorker();
		newNetWorker01.start();
	}
	
	//请求获取别人的关注列表
	public void reqOtherAttentionList(String friendID){
		newNetWorker01.sendMsg.reqOtherAttentionList(friendID);
	}
	//请求获取我的关注列表
	public void reqMyAttentionList(){
		newNetWorker01.sendMsg.reqMyAttentionList();
	}
	//请求获取别人的粉丝列表
	public void reqOtherFollowerList(String friendID){
		newNetWorker01.sendMsg.reqOtherFollowerList(friendID);
	}
	
	////获取用户的头像
	public void reqUserHead(String userID){
		newNetWorker01.sendMsg.reqUserHead(userID);
	}
	
	//私聊发送图片
	public boolean sendImg(String userID,String friendID,String time,String filePath){
		return newNetWorker01.sendMsg.sendImg(userID,friendID,time,filePath);
	}
	
	//发送获取我的消息的请求
	public boolean reqMyMsg(String userId,int type){
		return newNetWorker01.sendMsg.getMyMessage(userId, type);
	}
	
	//
	public void sendIsHasNewMsg(String userId){
		newNetWorker01.sendMsg.sendIsHasNewMsg(userId);
	}
}
