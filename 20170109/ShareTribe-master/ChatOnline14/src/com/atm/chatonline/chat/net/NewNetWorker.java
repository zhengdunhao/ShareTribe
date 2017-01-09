package com.atm.chatonline.chat.net;

/**
 * 本次项目以NewNetWok01 为准
 * */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.activity.mainview.MainView;
import com.atm.chatonline.bbs.commom.NewMessage;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SharedPreferenceUtils;
import com.atm.chatonline.chat.info.ChatMessage;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.info.Group;
import com.atm.chatonline.chat.info.GroupChatMessage;
import com.atm.chatonline.chat.ui.GroupChatActivity;
import com.atm.chatonline.chat.ui.PersonChatActivity;
import com.atm.chatonline.chat.ui.ReceiveInfoListener;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.example.studentsystem01.R;

public class NewNetWorker extends Thread {

	private String tag = "NewNetWorker01";

	private static String IP = "139.129.131.179";// ccy:"192.168.191.1"
													// cmcc"192.168.191.9"
													// sheldon"192.168.199.205"
													// 公网ip：139.129.131.179
	private static int PORT = 23457;

	protected Selector selector = null;
	public SocketChannel socketChannel = null;
	protected NewNetWorkSend sendMsg;
	private Charset charset = Charset.forName("UTF-8");

	// private ByteBuffer sendBuffer;
	private ByteBuffer receiveBuffer;
	private int size;
	private byte[] receiveBytes;

	protected static final int CONNECT_TIMEOUT = 10000;
	protected static final int READ_TIMEOUT = 10000;
	protected static final int RECONNECT_TIME = 120000;
	protected static final int RECONNECT_TIME_SECOND = RECONNECT_TIME / 1000;

	Map<String, ReceiveInfoListener> listeners = new HashMap<String, ReceiveInfoListener>();

	protected final byte connect = 1;
	protected final byte running = 2;
	protected byte state = running;
	protected boolean isWork = true;

	public NewNetWorker() {
		connect();
	}

	public void run() {
		try {
			int i = 0;
			Log.i(tag, "i:" + i++);
			while (isWork) {
				switch (state) {
				case running:
					receiveMsg();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 连接服务器

	void connect() {
		try {
			// Log.i(tag, "NewNetWorker01------即将远程连接服务器");
			// selector = Selector.open();
			// Log.i(tag, "NewNetWorker01------Selector.open()");
			// InetSocketAddress isa = new InetSocketAddress(IP, PORT);
			// Log.i(tag, "NewNetWorker01------ip="+IP+" PORT ="+PORT);
			// socketChannel = SocketChannel.open(isa);
			// Log.i(tag, "NewNetWorker01------向服务器发送IP和端口号,IP:" + IP + "、PORT:"
			// + PORT);
			// socketChannel.configureBlocking(false);
			// socketChannel.register(selector, SelectionKey.OP_READ);
			// sendMsg=new NewNetWorkSend(socketChannel);
			// Log.i(tag, "NewNetWorker01------服务器连接成功");
			Log.i(tag, "NewNetWorker01------即将远程连接服务器");
			selector = Selector.open();
			Log.i(tag,
					"NewNetWorker01------Selector.open(),selector is be open "
							+ selector.isOpen());// selector.isopen判断selector是否打开
			InetSocketAddress isa = new InetSocketAddress(IP, PORT);
			Log.i(tag, "NewNetWorker01------ip=" + IP + " PORT =" + PORT);
			socketChannel = SocketChannel.open(isa);
			Log.i(tag, "if the connection is initiated but not finish "
					+ socketChannel.isConnectionPending());
			Log.i(tag, "NewNetWorker01------向服务器发送IP和端口号,IP:" + IP + "、PORT:"
					+ PORT);
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			Log.i(tag, "the channel is registered at least one slector "
					+ socketChannel.isRegistered());
			sendMsg = new NewNetWorkSend(socketChannel);
			Log.i(tag, "NewNetWorker01------服务器连接成功");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addReceiveInfoListener(String state,
			ReceiveInfoListener listener) {
		listeners.put(state, listener);
	}
	
	public void removeReceiveInfoListener(){
		for (Iterator<Entry<String, ReceiveInfoListener>> it = listeners.entrySet().iterator(); it.hasNext();){
		    Entry<String, ReceiveInfoListener> item = it.next();
		    
		    it.remove();
		}
		for (Entry<String, ReceiveInfoListener> item : listeners.entrySet()){
		    System.out.println(item.getKey());
		}
	}

	public boolean isChatting(String state, Object o) {
		LogUtil.p(tag, "NewNetWorker01中的listener数为：" + listeners.size());
		Log.i(tag, state);
		// 现阶段只有ChatActivity注册了ReceiveInfoListener,所以listeners里只有一个ReceiveInfoListener
		// 通过state判断具体是哪个listener
		ReceiveInfoListener listener = listeners.get(state);
		Log.i(tag, "NewNetWorker01中的listener为：" + listener.toString());
		boolean result = listener.isChatting(o);
		Log.i(tag, "NewNetWorker01------receive(ChatMessage message)-result："
				+ result);
		return result;
	}

	/**
	 * 侦听服务器的反应
	 */
	void receiveMsg() {
		SelectionKey key = null;
		try {
			while (selector.select() > 0) {
				Log.i(tag, "NewNetWorker01------selector检测到有管道过来");
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					key = iterator.next();
					iterator.remove();
					handleKey(key);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 处理请求

	private void handleKey(SelectionKey selectionKey) throws IOException {
		try {
			Log.i(tag, "NewNetWorker01------要对收到的key进行处理");
			int tip = 0;
			if (selectionKey.isReadable()) {
				tip = getPutInt();

				Log.i(tag, "NewNetWorker01------从服务器得到请求是：" + tip);
				switch (tip) {
				case Config.RESULT_LOGIN:
					Log.i(tag,
							"NewNetWorker01------从服务器得到请求是：Config.RESULT_LOGIN");
					handleLogin();
					break;
				case Config.REQUEST_BE_OFF:
					Log.i(tag, "NewNetWorker01--->>>>重复登录时，强制退出handleExit");
					hanldleExit();
					break;
				case Config.MESSAGE_FROM:
					Log.i(tag,
							"NewNetWorker01------从服务器得到请求是：Config.MESSAGE_FROM");
					handleReceive();
					break;
				case Config.CROWD_MESSAGE_FROM:
					Log.i(tag,
							"NewNetWorker01------从服务器得到请求是：Config.CROWD_MESSAGE_FROM");
					handGrowdMessage();
					break;
				case Config.CROWD_RESULT_CREATE:
					Log.i(tag,
							"NewNetWorker01------从服务器得到请求是：Config.CROWD_RESULT_CREATE");
					handCreateGroupResult();
					break;
				case Config.CROWD_RESULT_FIND:
					Log.i(tag, "NewNetWorker01------Config.CROWD_RESULT_FIND");
					try {
						handleCrowdResultFind();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Config.USER_RESULT_FIND:// 用户搜索结果
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_FIND");
					handleFindUser();
					break;

				case Config.CROWD_RESULT_FOUND:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.CROWD_RESULT_FOUND");
					handleFoundHobbyGroup();
					break;

				case Config.USER_RESULT_FOUND:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_FOUND");
					handleFoundHobbyUser();
					break;
				case Config.USER_RESULT_ADDATTENT:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_ADDATTENT");
					handleReqAttention();
					break;
				case Config.USER_RESULT_CANCELATTENT:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_CANCELATTENT");
					handleReqCanncel();
					break;
				case Config.USER_RESULT_GETINFO:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_GETINFO");
					handlePersonINFO();
					break;
				case Config.CROWD_RESULT_GETINFO:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.CROWD_RESULT_GETINFO");
					handleLookGroupData();
					break;
				case Config.CROWD_RESULT_MY:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.CROWD_RESULT_MY");
					handleFindMyGroup();
					break;
				case Config.USER_RESULT_GETATTENT:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_GETATTENT");

					handleMyAttention();
					break;
				case Config.USER_RESULT_OATTENT:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_OATTENT");
					handleOtherAttention();
					break;
				case Config.USER_GET_ATTENT:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_GET_ATTENT");
					break;
				case Config.USER_RESULT_OFANS:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_OFANS");
					handleOtherAttention();// 虽然方法名是处理别人的关注，但处理别人的粉丝也是可以的，都是查看
					break;
				case Config.USER_RESULT_GETHEAD:
					Log.i(tag,
							"NewNetWorker01---从服务器得到请求是：Config.USER_RESULT_GETHEAD");
					handleUserHead();
					break;
				case Config.MY_MESSAGE:
					Log.i(tag, "NewNetWorker01---从服务器得到请求是：Config.MY_MESSAGE");
					handleMyMessage();
					break;
				case Config.ISHAS_NEW_MYMESSAGE:
					Log.i(tag, "NewNetWorker01---从服务器得到请求是：Config.ISHASMYMESSAGE");
					handleNewMessage();
					break;

				case -100:
					Log.i(tag, "NewNetWorker01------从服务器得到请求是：请求重新连接服务器");
					connect();
					String userID = BaseActivity.getSelf().getUserID();
					String pwd = BaseActivity.getSelf().getPwd();
					Log.i(tag, "从服务器得到请求是：请求重新连接服务器，userID:" + userID + "、pwd:"
							+ pwd);
					// reqLogin(userID,pwd);
					if (sendMsg == null) {
						Log.i(tag, "sendmsg为null");
					}
					sendMsg.login(userID, pwd);
					break;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 重复登录，强制已经登录的用户退出
	 */
	private void hanldleExit() {
		Log.i(tag, "即将跳转到登录界面");

		Message msg = new Message();
		msg.what = 22;
		BaseActivity.sendMessage(msg);
		try {
			socketChannel.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	// 处理登入请求

	public void handleLogin() {
		try {
			int result = getPutInt();
			Message msg = new Message();
			Log.i(tag, "handleLogin-result:" + result);
			if (result == Config.SUCCESS) {
				Log.i(tag, "handleLogin()----Config.SUCCESS");
				msg.what = Config.LOGIN_SUCCESS;
				Log.i(tag, "handleLogin()--success");

			} else if (result == Config.FAILED) {
				msg.what = Config.FAILED;
				Log.i(tag, "handleLogin()--falied");
			} else {
				msg.what = Config.USER_LOGIN_ALREADY;
			}
			BaseActivity.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * void reqLogin(String userID, String pwd) { Log.i(tag,
	 * "NewNetWorker01---即将发送登录请求"); // String filepath =
	 * Environment.getExternalStorageDirectory()+"/woliao/head/1.jpg"; // String
	 * filename = "1.jpg"; try { // byte[] bytes = makeFileToByte(filepath); //
	 * Log.i(tag, "NewNetWorker01---文件已被转化成bytes"); // ByteBuffer buffer =
	 * ByteBuffer
	 * .allocateDirect(20+userID.getBytes().length+pwd.getBytes().length // +
	 * filename.getBytes().length + bytes.length);
	 * 
	 * sendBuffer =
	 * ByteBuffer.allocateDirect(12+userID.getBytes().length+pwd.getBytes
	 * ().length); sendBuffer.putInt(Config.REQUEST_LOGIN);
	 * sendBuffer.putInt(userID.getBytes().length);
	 * sendBuffer.put(userID.getBytes());
	 * sendBuffer.putInt(pwd.getBytes().length); sendBuffer.put(pwd.getBytes());
	 * // buffer.putInt(filename.getBytes().length); //
	 * buffer.put(filename.getBytes()); // buffer.putInt(bytes.length); //
	 * buffer.put(ByteBuffer.wrap(bytes)); sendBuffer.flip();
	 * socketChannel.write(sendBuffer); sendBuffer.clear(); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 */

	/**
	 * 发送文本消息
	 * 
	 * @param userID
	 * @param friendID
	 * @param time
	 * @param content
	 * @return
	 */

	/*
	 * public boolean sendText(String userID,String friendID,String time,String
	 * content){ boolean result = true; Log.i(tag,
	 * "NewNetWorker01---告诉服务器客户端要发送信息"); try { sendBuffer =
	 * ByteBuffer.allocateDirect
	 * (24+userID.getBytes().length+friendID.getBytes().length
	 * +time.getBytes().length+content.getBytes().length);
	 * sendBuffer.putInt(Config.MESSAGE_TO);
	 * sendBuffer.putInt(Config.MESSAGE_TYPE_TXT); Log.i(tag,
	 * "NewNetWorker01---发送Config.MESSAGE_TO和Config.MESSAGE_TYPE_TXT请求");
	 * sendBuffer.putInt(userID.getBytes().length);
	 * sendBuffer.put(userID.getBytes());
	 * sendBuffer.putInt(friendID.getBytes().length);
	 * sendBuffer.put(friendID.getBytes());
	 * sendBuffer.putInt(time.getBytes().length);
	 * sendBuffer.put(time.getBytes());
	 * sendBuffer.putInt(content.getBytes().length);
	 * sendBuffer.put(content.getBytes()); sendBuffer.flip();
	 * socketChannel.write(sendBuffer); sendBuffer.clear(); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); return
	 * result=false; } Log.i(tag, "NewNetWorker01---客户端发送短信成功"); return result;
	 * }
	 */
	/**
	 * 处理私聊消息
	 * 
	 * @throws IOException
	 */
	private void handleReceive() throws IOException {
		try {
			int type = getPutInt();// 消息類型
			if (type == Config.MESSAGE_TEXT) {
				// 发信人
				String friendID = getString();
				LogUtil.p(tag, "friendID:" + friendID);
				// 发信人名字
				String nickName = getString();
				LogUtil.p(tag, "nickName:" + nickName);
				// 收信人，即目前登入的用户
				String userID = getString();
				LogUtil.p(tag, "userID:" + userID);
				String time = getString();
				String content = getString();
				Log.i(tag, "接收内容:" + content);
				byte[] headImg = getFileBytes(socketChannel);
				Bitmap bm = FileUtil.ByteToBitmap(headImg);
				Log.i(tag, "handleReceiveText()---friendID:" + friendID);
				// 保存到数据库
				File file = FileUtil.createFriendFile(userID, friendID);
				Log.i(tag, "头像保存路径》》》》》》" + file.getAbsolutePath());
				FileUtil.saveBitmap(file, bm);

				Log.i(tag, "NewNetWorker01-----handleReceiveText()收到一下这些东西");
				Log.i(tag, "NewNetWorker01---friendID:" + friendID
						+ " 、userID:" + userID + "、time:" + time + "、content："
						+ content);
				Log.i(tag, "first nickname is " + nickName);
				ChatMessage chatMessage = new ChatMessage(userID, friendID,
						nickName, Config.MESSAGE_FROM, Config.MESSAGE_TEXT,
						time, content, 0);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				Log.i(tag, "nickName is " + chatMessage.getNickName());
				bundle.putSerializable("chatMessage", chatMessage);
				msg.setData(bundle);
				BaseActivity.getFriend().setFriendID(friendID);
				BaseActivity.getFriend().setNickName(nickName);
				BaseActivity.getFriend().setBm(bm);
				Log.i(tag, "看看queue的大小:" + BaseActivity.queue.size());
				BaseActivity nowActivity = BaseActivity.getNowActivity();
				if (nowActivity instanceof PersonChatActivity) {
					if (isChatting("P", chatMessage)) {
						Log.i(tag,
								"NewNetWorker01-- 用户和正在和消息的发送者聊天，所以直接将消息发送到聊天界面中--");
						msg.what = Config.MESSAGE_FROM;
						BaseActivity.sendMessage(msg);

					} else {
						msg.what = Config.SEND_NOTIFICATION;
						Log.i(tag, "-- 用户和别的好友正在聊天，所以状态栏发出来消息通知--");
						BaseActivity.sendMessage(msg);
					}
				} else {// else if(nowActivity instanceof ChatMainActivity)
					Log.i(tag, "用户不在聊天界面");
					msg.what = Config.SEND_NOTIFICATION;

					BaseActivity.saveToDB(chatMessage);
					BaseActivity.sendMessage(msg);
					// 需要给当前活动不是PersonChatActivity的类里添加sendNotifycation()方法

				}

			} else if (type == Config.MESSAGE_IMG) {
				LogUtil.p(tag, "收到的是Config.message_img");
				// 发信人
				String friendID = getString();
				LogUtil.p(tag, "friendID:" + friendID);
				// 发信人名字
				String nickName = getString();
				LogUtil.p(tag, "nickName:" + nickName);
				// 收信人，即目前登入的用户
				String userID = getString();
				LogUtil.p(tag, "userID:" + userID);
				String time = getString();
				String content = getString();
				Log.i(tag, "接收内容:" + content);
				byte[] headImg = getFileBytes(socketChannel);
				LogUtil.p(tag, "头像收完");
				Bitmap bm = FileUtil.ByteToBitmap(headImg);
				Log.i(tag, "handleReceiveImg()---friendID:" + friendID);
				// 保存到数据库
				File file = FileUtil.createFriendFile(userID, friendID);
				Log.i(tag, "头像保存路径》》》》》》" + file.getAbsolutePath());
				FileUtil.saveBitmap(file, bm);
				byte[] chatImg = getFileBytes(socketChannel);
				LogUtil.p(tag, "图片收完");
				Bitmap bm2 = FileUtil.ByteToBitmap(chatImg);
				File fileImg = FileUtil.createFromImg(friendID, content);
				FileUtil.saveBitmap(fileImg, bm2);
				content = fileImg.getAbsolutePath();
				LogUtil.p(tag, "图片存档后的路径名:" + content);
				ChatMessage chatMessage = new ChatMessage(userID, friendID,
						nickName, Config.MESSAGE_FROM, Config.MESSAGE_IMG,
						time, content, 0);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				Log.i(tag, "nickName is " + chatMessage.getNickName());
				bundle.putSerializable("chatMessage", chatMessage);
				msg.setData(bundle);
				BaseActivity.getFriend().setFriendID(friendID);
				BaseActivity.getFriend().setNickName(nickName);
				BaseActivity.getFriend().setBm(bm);
				Log.i(tag, "看看queue的大小:" + BaseActivity.queue.size());
				BaseActivity nowActivity = BaseActivity.getNowActivity();
				if (nowActivity instanceof PersonChatActivity) {
					if (isChatting("P", chatMessage)) {
						Log.i(tag,
								"NewNetWorker01-- 用户和正在和消息的发送者聊天，所以直接将消息发送到聊天界面中--");
						msg.what = Config.MESSAGE_FROM;
						BaseActivity.sendMessage(msg);

					} else {
						msg.what = Config.SEND_NOTIFICATION;
						Log.i(tag, "-- 用户和别的好友正在聊天，所以状态栏发出来消息通知--");
						BaseActivity.sendMessage(msg);
					}
				} else {// else if(nowActivity instanceof ChatMainActivity)
					Log.i(tag, "用户不在聊天界面");
					msg.what = Config.SEND_NOTIFICATION;

					BaseActivity.saveToDB(chatMessage);
					BaseActivity.sendMessage(msg);
					// 需要给当前活动不是PersonChatActivity的类里添加sendNotifycation()方法

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** --以下是接收消息-- **/
	/**
	 * 处理群聊消息
	 */
	private void handGrowdMessage() {
		try {
			int messageType = getPutInt();// 消息类型
			String senderId = getString();
			int groupId = getPutInt();
			String time = getString();
			String content;

			Log.i(tag, "-----------群聊" + groupId + "------------------");
			Log.i(tag, ">>>>>>>>>>>time:" + time);
			Log.i(tag, ">>>>>>>>>>>userId:" + senderId);

			if (messageType == Config.CROWD_MESSAGE_TEXT) {// 文本消息
				content = getString();// 文本内容
				GroupChatMessage groupChatMessage = new GroupChatMessage(
						GroupChatMessage.MESSAGE_FROM, content,
						R.drawable.xiaohei, time, 0);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putSerializable("groupChatMessage", groupChatMessage);
				msg.setData(bundle);

				BaseActivity nowActivity = BaseActivity.getNowActivity();
				if (nowActivity instanceof GroupChatActivity) {
					if (isChatting("G", groupChatMessage)) {
						Log.i(tag,
								"NewNetWorker01-- 用户和正在群里聊天，所以直接将消息发送到聊天界面中--");
						msg.what = Config.CROWD_MESSAGE_FROM;
						BaseActivity.sendMessage(msg);
					}
				}
				Log.i(tag, "消息已接收...");
				Log.i(tag, "content:" + content);
			} else if (messageType == Config.CROWD_MESSAGE_IMG) {
				// TODO 群聊图片图片的处理
				String filename = getString();
				int pathLength = getPutInt();
				// byte[] bytes = makeFileToByte(filename);
				// content = saveFile(filename, senderId, bytes);
				// Log.i(tag, "content:" + content);
			}
			Log.i(tag, "-------------end---------------");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(tag, "get error");
		}
	}

	public void handCreateGroupResult() {// ***
		boolean flag = false;
		Message msg = new Message();
		Bundle bundle = new Bundle();
		int result = getPutInt();
		if (result == Config.SUCCESS) {
			int groupId = getPutInt();
			flag = true;
			Log.i(tag, "groupId= " + groupId);
			bundle.putInt("groupId", groupId);
			bundle.putBoolean("flag", flag);
		} else if (result == Config.FAILED) {
			flag = false;
			bundle.putBoolean("flag", flag);
		}
		msg.setData(bundle);
		BaseActivity.sendMessage(msg);
	}

	// 查找群列表结果
	private void handleFindMyGroup() throws IOException {
		Bundle bundle = new Bundle();
		Message msg = new Message();
		msg.what = 0;// 0表示这是群列表
		Log.i(tag, "即将接收到FindMyGroup result");
		int result = getPutInt();
		if (result == Config.SUCCESS) {
			Log.i(tag, "查询成功");
			List<Group> groupList = new ArrayList<Group>();
			ArrayList list = new ArrayList();
			int listNum = getPutInt();
			Log.i(tag, "listNum:" + listNum);
			for (int i = 0; i < listNum; i++) {
				int groupID = getPutInt();
				Log.i(tag, "groupID" + groupID);
				String groupName = getString();
				Log.i(tag, "groupName" + groupName);
				String groupNum = getString();
				Log.i(tag, "groupNum" + groupNum);
				byte[] groupImg = getFileBytes(socketChannel);
				Bitmap bm = FileUtil.ByteToBitmap(groupImg);
				// Bitmap bm = BitmapFactory.decodeByteArray(groupImg, 0,
				// groupImg.length);
				Group myGroup = new Group(groupID + "", groupName, groupNum, bm);
				if (bm == null) {
					Log.i(tag, "bm is null");
				} else {
					Log.i(tag, "bm is not null");
				}
				groupList.add(myGroup);
			}
			if (groupList.size() != 0) {
				Log.i(tag, "从服务器接收的群列表为" + groupList.size());
			}
			list.add(groupList);
			bundle.putParcelableArrayList("groupList", list);
		}
		bundle.putInt("result", result);
		msg.setData(bundle);
		BaseActivity.sendMessage(msg);
	}

	private void handleCrowdResultFind() throws IOException {
		int result = getPutInt();//
		Message msg = new Message();
		if (result == Config.SUCCESS) {
			Log.i(tag, "handleCrowdResultFind()--result==Config.SUCCESS");
			List<Group> groupList = new ArrayList<Group>();
			ArrayList list = new ArrayList();
			int listNum = getPutInt();
			Log.i(tag, "listNum:" + listNum);
			int j = 1;
			int z = 1;

			for (int i = 0; i < listNum; i++) {
				Log.i(tag, "第" + j++ + "次进来");
				int groupID = getPutInt();
				Log.i(tag, "--groupID:" + groupID);
				String groupName = getString();
				Log.i(tag, "--groupName:" + groupName);
				String groupNum = getString();
				Log.i(tag, "--groupNum:" + groupNum);

				byte[] groupImg = getFileBytes(socketChannel);
				BitmapFactory.Options options = new Options();
				options.inDither = false; // 不进行图片抖动处理；
				options.inPreferredConfig = null;// 设置最佳解码器解码
				options.inSampleSize = 4;
				Bitmap bm = BitmapFactory.decodeByteArray(groupImg, 0,
						groupImg.length, options);
				// Bitmap bm = BitmapFactory.decodeByteArray(groupImg, 0,
				// groupImg.length);
				Group searchGroup = new Group(groupID + "", groupName,
						groupNum, bm);
				groupList.add(searchGroup);
				Log.i(tag, "第" + z++ + "次读完");
			}

			if (groupList.size() != 0) {
				Log.i(tag, "从服务器接收的群列表不为空");
				Log.i(tag, "groupList的数量是:" + groupList.size());
			}
			list.add(groupList);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("groupList", list);

			msg.setData(bundle);
			msg.what = Config.CROWD_FIND_SUCCESS;

		} else if (result == Config.NOT_FOUND) {
			Log.i(tag, "handleCrowdResultFind()--result==Config.NOT_FOUND");
			msg.what = Config.NOT_FOUND;
		}
		BaseActivity.sendMessage(msg);

	}

	// 查看群资料的结果
	public void handleLookGroupData() throws IOException {
		List<Friend> list = new ArrayList<Friend>();
		ArrayList arrayList = new ArrayList();
		Message msg = new Message();
		Bundle bundle = new Bundle();
		int result = getPutInt();
		Log.i(tag, "准备获得结果");
		if (result == Config.SUCCESS) {
			bundle.putInt("result", result);
			// bundle.putString("groupName", getString());
			int menberCount = getPutInt();
			for (int i = 0; i < menberCount; i++) {
				Friend friend = new Friend();
				friend.setFriendID(getString());// 群成员id
				friend.setNickName(getString());// 昵称
				friend.setRole(getPutInt());// 群角色
				// int length=getPutInt();//图片长度
				byte[] imgArray = getFileBytes(socketChannel);
				Bitmap bm = FileUtil.ByteToBitmap(imgArray);
				friend.setBm(bm);// 群成员头像
				list.add(friend);
			}
			arrayList.add(list);
			bundle.putParcelableArrayList("arrayList", arrayList);

		} else if (result == Config.FAILED) {
			bundle.putInt("result", result);
		} else {
			bundle.putInt("result", result);
		}
		Log.i(tag, "已获得结果");
		msg.setData(bundle);
		BaseActivity.sendMessage(msg);
	}

	public void handleFindUser() throws IOException {
		try {
			int result = getPutInt();
			Message msg = new Message();
			if (result == Config.SUCCESS) {
				Log.i(tag, "handleFindUser()--result==Config.SUCCESS");
				List<Friend> friendList = new ArrayList<Friend>();
				ArrayList list = new ArrayList();
				int listNum = getPutInt();
				Log.i(tag, "搜到" + listNum + "条有关的用户记录");
				int j = 1;
				int z = 1;
				for (int i = 0; i < listNum; i++) {
					Log.i(tag, "第" + j++ + "次进来");
					String friendID = getString();
					Log.i(tag, "friendID: " + friendID);
					String friendNickName = getString();
					Log.i(tag, "friendNickName:" + friendNickName);
					String department = getString(); // 2015-8-23新加的，列表显示院系
					Log.i(tag, "department:" + department);
					String friendSex = getString();
					Log.i(tag, "friendSex:" + friendSex);
					int flag = getPutInt();
					Log.i(tag, "relationship:" + flag + ",但这是搜索列表，所以无作用");
					// byte[] friendImg = getFileBytes(socketChannel);
					// BitmapFactory.Options options = new Options();
					// options.inDither = false; //不进行图片抖动处理；
					// options.inPreferredConfig = null;//设置最佳解码器解码
					// options.inSampleSize = 2;
					// Bitmap bm = BitmapFactory.decodeByteArray(friendImg, 0,
					// friendImg.length, options);
					Log.i(tag, "我来看看图片");
					byte[] friendImg = getFileBytes(socketChannel);
					Bitmap bm = FileUtil.ByteToBitmap(friendImg);
					Friend friend = new Friend(friendID, friendNickName,
							department, friendSex, bm);
					friendList.add(friend);
					Log.i(tag, "第" + z++ + "次读完");
				}

				if (friendList.size() != 0) {
					Log.i(tag, "从服务器接收的群列表不为空");
					Log.i(tag, "friendList的数量是:" + friendList.size());
				}
				list.add(friendList);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("friendList", list);

				msg.setData(bundle);
				msg.what = Config.USER_FIND_SUCCESS;

			} else if (result == Config.NOT_FOUND) {
				msg.what = Config.FAILED;
			}
			BaseActivity.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 处理请求感兴趣群的结果
	public void handleFoundHobbyGroup() throws IOException {
		Log.i(tag, "进入handleFoundHobbyGroup()");
		int result = getPutInt();//
		Message msg = new Message();
		if (result == Config.SUCCESS) {
			Log.i(tag, "handleFoundHobbyGroup--result==Config.SUCCESS");
			List<Group> groupList = new ArrayList<Group>();
			ArrayList list = new ArrayList();
			int listNum = getPutInt();
			Log.i(tag, "listNum:" + listNum);
			int j = 1;
			int z = 1;

			for (int i = 0; i < listNum; i++) {
				Log.i(tag, "第" + j++ + "次进来");
				int groupID = getPutInt();
				Log.i(tag, "--groupID:" + groupID);
				String groupName = getString();
				Log.i(tag, "--groupName:" + groupName);
				String groupNum = getString();
				Log.i(tag, "--groupNum:" + groupNum);

				byte[] groupImg = getFileBytes(socketChannel);
				BitmapFactory.Options options = new Options();
				options.inDither = false; // 不进行图片抖动处理；
				options.inPreferredConfig = null;// 设置最佳解码器解码
				options.inSampleSize = 2;
				Bitmap bm = BitmapFactory.decodeByteArray(groupImg, 0,
						groupImg.length, options);
				// Bitmap bm = BitmapFactory.decodeByteArray(groupImg, 0,
				// groupImg.length);
				Group searchGroup = new Group(groupID + "", groupName,
						groupNum, bm);
				groupList.add(searchGroup);
				Log.i(tag, "第" + z++ + "次读完");
			}

			if (groupList.size() != 0) {
				Log.i(tag, "从服务器接收的群列表不为空");
				Log.i(tag, "groupList的数量是:" + groupList.size());
			}
			list.add(groupList);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("groupList", list);

			msg.setData(bundle);
			msg.what = Config.CROWD_FIND_SUCCESS;

		} else if (result == Config.NOT_FOUND) {
			Log.i(tag, "handleCrowdResultFind()--result==Config.NOT_FOUND");
			msg.what = Config.NOT_FOUND_HOBBY_GOURP;
		}
		BaseActivity.sendMessage(msg);
	}

	public void handleFoundHobbyUser() throws IOException {
		Log.i(tag, "进入handleFoundHobbyUser() ");
		try {
			int result = getPutInt();
			Message msg = new Message();
			if (result == Config.SUCCESS) {
				Log.i(tag, "handleFoundHobbyUser()--result==Config.SUCCESS");
				List<Friend> friendList = new ArrayList<Friend>();
				ArrayList list = new ArrayList();
				int listNum = getPutInt();
				Log.i(tag, "搜到" + listNum + "条有关的用户记录");
				int j = 1;
				int z = 1;
				for (int i = 0; i < listNum; i++) {
					Log.i(tag, "第" + j++ + "次进来");
					String friendID = getString();
					Log.i(tag, "friendID: " + friendID);
					String friendNickName = getString();
					Log.i(tag, "friendNickName:" + friendNickName);
					String department = getString(); // 2015-8-23新加的，列表显示院系
					Log.i(tag, "department:" + department);
					String friendSex = getString();
					Log.i(tag, "friendSex:" + friendSex);
					int flag = getPutInt();
					Log.i(tag, "relationship:" + flag + ",但这是搜索列表，所以无作用");
					byte[] friendImg = getFileBytes(socketChannel);
					BitmapFactory.Options options = new Options();
					options.inDither = false; // 不进行图片抖动处理；
					options.inPreferredConfig = null;// 设置最佳解码器解码
					options.inSampleSize = 2;
					Bitmap bm = BitmapFactory.decodeByteArray(friendImg, 0,
							friendImg.length, options);
					Friend friend = new Friend(friendID, friendNickName,
							department, friendSex, bm);
					friendList.add(friend);
					Log.i(tag, "第" + z++ + "次读完");
				}

				if (friendList.size() != 0) {
					Log.i(tag, "从服务器接收的群列表不为空");
					Log.i(tag, "friendList的数量是:" + friendList.size());
				}
				list.add(friendList);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("friendList", list);

				msg.setData(bundle);
				msg.what = Config.USER_FIND_SUCCESS;

			} else if (result == Config.NOT_FOUND) {
				msg.what = Config.NOT_FOUND_HOBBY_USER;
			}
			BaseActivity.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handlePersonINFO() throws IOException {
		try {
			int result = getPutInt();
			Message msg = new Message();
			if (result == Config.SUCCESS) {
				Log.i(tag, "handlePersonINFO()--result==Config.SUCCESS");
				List<Friend> friendINFOList = new ArrayList<Friend>();
				ArrayList list = new ArrayList();
				String friendID = getString();
				Log.i(tag, "friendID:" + friendID);
				String nickName = getString();
				Log.i(tag, "nickName:" + nickName);
				String sex = getString();
				Log.i(tag, "sex:" + sex);
				String attentions = getString();
				Log.i(tag, "attentions:" + attentions);
				String followers = getString();
				Log.i(tag, "followers:" + followers);
				String signature = getString();
				Log.i(tag, "signature:" + signature);
				String school = getString();
				Log.i(tag, "school:" + school);
				String department = getString();
				Log.i(tag, "department:" + department);
				int relationship = getPutInt();
				Log.i(tag, "relationship:" + relationship);
				String publishNoteNum = getString();
				Log.i(tag, "publishNoteNum:" + publishNoteNum);
				String publishEssayId = getString();
				Log.i(tag, "publishEssayId:" + publishEssayId);
				String publishNoteTitle = getString();
				Log.i(tag, "publishNoteTitle:" + publishNoteTitle);
				String publishNoteDetail = getString();
				Log.i(tag, "publishNoteDetail:" + publishNoteDetail);
				String collectNoteNum = getString();
				Log.i(tag, "collectNoteNum:" + collectNoteNum);
				String collectEssayId = getString();
				Log.i(tag, "collectEssayId:" + collectEssayId);
				String collectNoteTitle = getString();
				Log.i(tag, "collectNoteTitle:" + collectNoteTitle);
				String collectNoteDetail = getString();
				Log.i(tag, "collectNoteDetail:" + collectNoteDetail);
				byte[] friendImg = getFileBytes(socketChannel);
				BitmapFactory.Options options = new Options();
				options.inDither = false; // 不进行图片抖动处理；
				options.inPreferredConfig = null;// 设置最佳解码器解码
				options.inSampleSize = 2;
				Bitmap bm = BitmapFactory.decodeByteArray(friendImg, 0,
						friendImg.length, options);
				Friend friend = new Friend(friendID, nickName, sex, attentions,
						followers, signature, school, department, relationship,
						publishNoteNum,publishEssayId, publishNoteTitle, 
						publishNoteDetail,collectNoteNum, collectEssayId,collectNoteTitle, collectNoteDetail, bm);
				friendINFOList.add(friend);
				list.add(friendINFOList);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("friendINFOList", list);
				msg.setData(bundle);
				Log.i(tag, "relationship:" + relationship);
				msg.what = Config.SUCCESS;
			} else if (result == Config.FAILED) {
				Log.i(tag, "handlePersonINFO()--result==Config.SUCCESS");
				msg.what = Config.FAILED;
			}
			BaseActivity.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理服务器发送过来的我的消息
	 */
	private void handleMyMessage() {
		try {
			// Message msg=new Message();
			// System.out.println("获取到发送过来的消息");
			String json = getString();
			System.out.println("获取到发送过来的消息" + json.toString());
			Bundle bundle = new Bundle();
			bundle.putString("MyMessage", json);
			Message msg = new Message();
			msg.setData(bundle);
			BaseActivity.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// BaseActivity.sendMessage(msg);
	}
	
	/**
	 * 处理获取新消息
	 */
	private void handleNewMessage(){
		try{
		String json = getString();
		LogUtil.i("json:"+json.toString());
		System.out.println("happy获取到发送过来的消息" + json.toString());
		
		JSONObject obj=new JSONObject(json);
		NewMessage newMessage=SharedPreferenceUtils.getInstance().getNewMessage(BaseActivity.getNowActivity().getApplicationContext());
		if(newMessage==null){
			newMessage=new NewMessage();
		}
		int sum=newMessage.getSum();
		for(int i=0;i<3;i++){
			if(obj.has(i+"")){
				sum=sum+obj.getInt(i+"");
				LogUtil.i("sum="+sum);
				if((i+"").equals("0")){
					newMessage.setApplySum(newMessage.getApplySum()+ obj.getInt(i+""));
				}
				if((i+"").equals("1")){
					newMessage.setReplySum(newMessage.getReplySum()+ obj.getInt(i+""));
				}
				if((i+"").equals("2")){
					newMessage.setSystemSum(newMessage.getSystemSum()+ obj.getInt(i+""));
				}
			}
		}
		newMessage.setSum(sum);
		SharedPreferenceUtils.getInstance().saveNewMessage(newMessage, BaseActivity.getNowActivity().getApplicationContext());
		Bundle bundle = new Bundle();
		bundle.putSerializable("NewMessage", newMessage);
		Message msg = new Message();
		msg.what=Config.ISHAS_NEW_MYMESSAGE;
		msg.setData(bundle);
		System.out.println(BaseActivity.getNowActivity());
		BaseActivity.sendMessage(msg);	
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("获取新消息失败");
		}
		
	}

	// 处理关注
	public void handleReqAttention() {
		try {
			int result = getPutInt();
			if (result == Config.SUCCESS) {
				Log.i(tag, "请求关注成功");
			} else if (result == Config.FAILED) {
				Log.i(tag, "请求关注失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 处理取消关注
	public void handleReqCanncel() {
		try {
			int result = getPutInt();
			if (result == Config.SUCCESS) {
				Log.i(tag, "请求取消关注成功");
			} else if (result == Config.FAILED) {
				Log.i(tag, "请求取消关注失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleOtherAttention() throws IOException {
		try {
			Log.i(tag, "进入handleOtherAttention() ");
			int result = getPutInt();
			Message msg = new Message();
			if (result == Config.SUCCESS) {
				Log.i(tag, "handleOtherAttention()--result==Config.SUCCESS");
				List<Friend> friendList = new ArrayList<Friend>();
				ArrayList list = new ArrayList();
				String userID = getString();
				Log.i(tag, "userID:" + userID);
				// 当前查看某个人的关注列表的ID
				String friendID = getString();
				Log.i(tag, "friendID:" + friendID);
				int listNum = getPutInt();
				Log.i(tag, "搜到" + listNum + "条有关的用户记录");
				int j = 1;
				int z = 1;
				for (int i = 0; i < listNum; i++) {
					Log.i(tag, "第" + j++ + "次进来");
					// 某个人它的关注列表里每个人的ID
					String otherID = getString();
					Log.i(tag, "otherID: " + otherID);
					String friendNickName = getString();
					Log.i(tag, "friendNickName:" + friendNickName);
					String department = getString(); // 2015-8-23新加的，列表显示院系
					Log.i(tag, "department:" + department);
					String friendSex = getString();
					Log.i(tag, "friendSex:" + friendSex);
					int relationship = getPutInt();
					Log.i(tag, "relationship:" + relationship + "");
					byte[] friendImg = getFileBytes(socketChannel);
					BitmapFactory.Options options = new Options();
					options.inDither = false; // 不进行图片抖动处理；
					options.inPreferredConfig = null;// 设置最佳解码器解码
					options.inSampleSize = 2;
					Bitmap bm = BitmapFactory.decodeByteArray(friendImg, 0,
							friendImg.length, options);
					Friend friend = new Friend(userID, friendID, otherID,
							friendNickName, department, relationship, bm);
					friendList.add(friend);
					Log.i(tag, "第" + z++ + "次读完");
				}

				if (friendList.size() != 0) {
					Log.i(tag, "从服务器接收的群列表不为空");
					Log.i(tag, "friendList的数量是:" + friendList.size());
				}
				list.add(friendList);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("attentionList", list);

				msg.setData(bundle);
				msg.what = Config.SUCCESS;

			} else if (result == Config.NOT_FOUND) {
				msg.what = Config.NOT_FOUND_HOBBY_USER;
			}
			BaseActivity.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleMyAttention() throws IOException {
		try {
			Log.i(tag, "进入handleMyAttention() ");
			try {
				int result = getPutInt();
				Message msg = new Message();
				if (result == Config.SUCCESS) {
					Log.i(tag, "handleMyAttention()--result==Config.SUCCESS");
					List<Friend> friendList = new ArrayList<Friend>();
					ArrayList list = new ArrayList();
					int listNum = getPutInt();
					Log.i(tag, "搜到" + listNum + "条有关的用户记录");
					int j = 1;
					int z = 1;
					for (int i = 0; i < listNum; i++) {
						Log.i(tag, "第" + j++ + "次进来");
						// 某个人它的关注列表里每个人的ID
						String friendID = getString();
						Log.i(tag, "friendID: " + friendID);
						String friendNickName = getString();
						Log.i(tag, "friendNickName:" + friendNickName);
						String department = getString(); // 2015-8-23新加的，列表显示院系
						Log.i(tag, "department:" + department);
						String friendSex = getString();
						Log.i(tag, "friendSex:" + friendSex);
						int relationship = getPutInt();
						Log.i(tag, "relationship:" + relationship + "");
						byte[] friendImg = getFileBytes(socketChannel);
						BitmapFactory.Options options = new Options();
						options.inDither = false; // 不进行图片抖动处理；
						options.inPreferredConfig = null;// 设置最佳解码器解码
						options.inSampleSize = 2;
						// Bitmap bm = BitmapFactory.decodeStream(is,
						// outPadding,
						// opts)
						Bitmap bm = BitmapFactory.decodeByteArray(friendImg, 0,
								friendImg.length, options);
						Friend friend = new Friend(friendID, friendNickName,
								department, relationship, bm);
						friendList.add(friend);
						Log.i(tag, "第" + z++ + "次读完");
					}

					if (friendList.size() != 0) {
						Log.i(tag, "从服务器接收的群列表不为空");
						Log.i(tag, "friendList的数量是:" + friendList.size());
					}
					list.add(friendList);
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("myAttentionList", list);

					msg.setData(bundle);
					msg.what = Config.USER_ATTENTION_LIST_SUCCESS;

				} else if (result == Config.NOT_FOUND) {
					msg.what = Config.NOT_FOUND_HOBBY_USER;
				}
				BaseActivity.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 处理服务器发来的用户头像
	public void handleUserHead() throws IOException {
		try {
			Log.i(tag, "进入handleUserHead() ");
			int result = getPutInt();
			if (result == Config.SUCCESS) {
				Log.i(tag, "handleUserHead()--result==Config.SUCCESS");
				String userID = getString();
				Log.i(tag, "userID:" + userID);
				byte[] UserImg = getFileBytes(socketChannel);
				BitmapFactory.Options options = new Options();
				options.inDither = false; // 不进行图片抖动处理；
				options.inPreferredConfig = null;// 设置最佳解码器解码
				options.inSampleSize = 2;
				Bitmap bm = BitmapFactory.decodeByteArray(UserImg, 0,
						UserImg.length, options);
				File file = FileUtil.createUserHead(userID);
				FileUtil.saveBitmap(file, bm);
				Log.i(tag, "头像发送完毕");
			} else {
				Log.i(tag, "handleUserHead()--result==Config.FAILED");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] getFileBytes(SocketChannel socketChannel) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int length = getPutInt();
		int size = 0;
		byte[] bytes = null;
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		int i = 0;
		while (length > 0) {
			if (length < 1024) {
				buffer = ByteBuffer.allocateDirect(length);
			}
			size = socketChannel.read(buffer);
			buffer.flip();
			bytes = new byte[size];
			buffer.get(bytes);
			baos.write(bytes);
			buffer.clear();
			// Log.i(tag, "getFileBytes--"+i+++"");
			length = length - size;
			// Log.i(tag, ""+length);
		}
		return baos.toByteArray();

	}

	/**
	 * 获取文件的字节数组
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] getFileBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 文件可能会很大
		receiveBuffer = ByteBuffer.allocate(1024);

		int i = 0;
		while ((size = socketChannel.read(receiveBuffer)) > 0) {
			receiveBuffer.flip();
			receiveBytes = new byte[size];
			receiveBuffer.get(receiveBytes);
			baos.write(receiveBytes);
			receiveBuffer.clear();
		}
		return baos.toByteArray();
	}

	/**
	 * 将获取从服务器获取到的图片文件保存到本地
	 */
	public String saveFile(String filename, String userId, byte[] filebytes) {
		return "";
	}

	public int getPutInt() {
		try {
			Log.i(tag, "1");
			receiveBuffer = ByteBuffer.allocateDirect(4);
			Log.i(tag, "2");
			int size = socketChannel.read(receiveBuffer);
			Log.i(tag, "size =" + size);
			Log.i(tag, "3");
			if (size >= 0) {
				receiveBuffer.flip();
				int putIntNum = receiveBuffer.getInt();
				receiveBuffer.clear();

				return putIntNum;
			}
			Log.i(tag, "NewNetWorker01---与服务器发生掉线");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(tag, "NewNetWorker01---getPutInt()失败");
		}
		return -100;
	}

	/***
	 * 获取字符串
	 * 
	 * @return
	 */
	public String getString() {
		try {

			int putInt=getPutInt();
			System.out.println("putInt="+putInt);
			receiveBuffer = ByteBuffer.allocateDirect(putInt);
			while(putInt > 0){
				int size = socketChannel.read(receiveBuffer);
				System.out.println("["+putInt+":"+size+"]");
				putInt -= size;
			}
			System.out.println("size="+size);
			receiveBytes = new byte[receiveBuffer.capacity()];
			
			if (size >= 0) {
				receiveBuffer.flip();
				receiveBuffer.get(receiveBytes);
				receiveBuffer.clear();
				Log.i(tag, "NewNetWorker01---getString()信息成功");
			}
			String str=new String(receiveBytes, "GBK");
			System.out.println("[str]:"+str);
			return str;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NewNetWorker01---getString()--错误";
		}
	}

}
