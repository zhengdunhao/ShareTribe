package com.atm.chatonline.chat.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.atm.charonline.bbs.util.MD5Encrypt;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;

public class NewNetWorkSend {

	private static String TAG = "ClientSendThread";
	static final int port = 23457;
	private Charset charset = Charset.forName("GBK");
	private SocketChannel socketChannel = null;
	private ByteBuffer buffer = null;

	public NewNetWorkSend(SocketChannel sc) {
		this.socketChannel = sc;
	}

	/**
	 * 登录
	 * 
	 * @param userId
	 * @param userPwd
	 */
	public void login(String userId, String userPwd) {
		try {
			String userPwdMD5 = MD5Encrypt.stringMD5(userPwd);
			Log.i(TAG, "向服务器发送登录请求");
			buffer = ByteBuffer.allocate(12 + userId.getBytes().length
					+ userPwdMD5.getBytes().length);
			buffer.putInt(Config.REQUEST_LOGIN);
			buffer.putInt(userId.getBytes().length);
			buffer.put(userId.getBytes());
			buffer.putInt(userPwdMD5.getBytes().length);
			buffer.put(userPwdMD5.getBytes());
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * // 发送文本私信消息 public void sendMessage(String userId, String friendId,
	 * String content) {
	 * 
	 * buffer = ByteBuffer.allocateDirect(16 + userId.getBytes().length +
	 * friendId.getBytes().length + content.getBytes().length); // 写进buffer缓冲区
	 * buffer.putInt(Config.MESSAGE_TO); put(userId); put(friendId);
	 * put(content);
	 * 
	 * writeBuffer();
	 * 
	 * }
	 */

	/**
	 * 发送私聊文本消息
	 * 
	 * @param userID
	 * @param friendID
	 * @param time
	 * @param content
	 * @return
	 */

	public boolean sendText(String userID, String friendID, String time,
			String content) {
		boolean result = true;
		try {

			Log.i(TAG, "NewNetWorker01---告诉服务器客户端要发送信息");
			buffer = ByteBuffer.allocateDirect(24 + userID.getBytes().length
					+ friendID.getBytes().length + time.getBytes().length
					+ content.getBytes().length);
			buffer.putInt(Config.MESSAGE_TO);
			buffer.putInt(Config.MESSAGE_TEXT);
			Log.i(TAG,
					"NewNetWorker01---发送Config.MESSAGE_TO和Config.MESSAGE_TYPE_TXT请求");
			buffer.putInt(userID.getBytes().length);
			buffer.put(userID.getBytes());
			buffer.putInt(friendID.getBytes().length);
			buffer.put(friendID.getBytes());
			buffer.putInt(time.getBytes().length);
			buffer.put(time.getBytes());
			buffer.putInt(content.getBytes().length);
			buffer.put(content.getBytes());
			writeBuffer();
			/*
			 * buffer.flip(); socketChannel.write(buffer); buffer.clear();
			 */
			Log.i(TAG, "NewNetWorker01---客户端发送短信成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送私聊图片
	 * 
	 */
	public boolean sendImg(String userID, String friendID, String time,
			String filePath) {
		boolean result = true;
		try {
			byte[] imgByte = makeFileToByte(filePath);
			LogUtil.p(TAG, "NewNetWorker01---告诉服务器客户端要发送图片");
			buffer = ByteBuffer.allocateDirect(28 + userID.getBytes().length
					+ friendID.getBytes().length + time.getBytes().length
					+ getFileName(filePath).getBytes().length + imgByte.length);
			buffer.putInt(Config.MESSAGE_TO);
			buffer.putInt(Config.MESSAGE_IMG);
			Log.i(TAG,
					"NewNetWorker01---发送Config.MESSAGE_TO和Config.MESSAGE_IMG请求");
			buffer.putInt(userID.getBytes().length);
			buffer.put(userID.getBytes());
			buffer.putInt(friendID.getBytes().length);
			buffer.put(friendID.getBytes());
			buffer.putInt(time.getBytes().length);
			buffer.put(time.getBytes());
			buffer.putInt(getFileName(filePath).getBytes().length);
			buffer.put(getFileName(filePath).getBytes());
			LogUtil.p(TAG, "filename:" + getFileName(filePath));
			buffer.putInt(imgByte.length);
			buffer.put(imgByte);
			writeBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 发送文本群消息
	 * 
	 * @param userId
	 * @param crowdId
	 * @param content
	 */
	public boolean sendCrowdMessage(String userId, int groupId, String content) {
		boolean result = true;
		try {
			buffer = ByteBuffer.allocateDirect(20 + userId.getBytes().length
					+ content.getBytes().length);

			buffer.putInt(Config.CROWD_MESSAGE_TO);// 群聊消息
			buffer.putInt(Config.CROWD_MESSAGE_TEXT);// 文本消息
			put(userId);
			buffer.putInt(groupId);
			put(content);
			writeBuffer();
		} catch (Exception e) {
			result = false;
			return result;
		}
		return result;
	}

	// 新建群//****
	public void sendCreateGroup(String userId, String groupName,
			String groupDesc, String groupLabel, int groupProperty,
			String groupHeadImg, String path) throws IOException {
		// String path="sdcard/create_group_headimg.png";
		byte[] bytes = makeFileToByte(path);

		try {
			buffer = ByteBuffer.allocate(36 + userId.getBytes().length
					+ groupName.getBytes().length + groupDesc.getBytes().length
					+ groupLabel.getBytes().length
					+ groupHeadImg.getBytes().length + bytes.length);
			buffer.putInt(Config.CROWD_CREATE);
			put(userId);
			put(groupName);
			put(groupDesc);
			put(groupLabel);
			buffer.putInt(groupProperty);
			put(groupHeadImg);
			buffer.putInt(bytes.length);
			Log.i("888888888888", "img size=" + bytes.length);
			buffer.put(bytes);
			writeBuffer();
		} catch (Exception e) {
			Log.i(TAG, "创建群不成功");
		}
	}

	// 请求推送可能感兴趣的趣
	public void reqHobbyGroup(String userID) {
		// String userID = WoliaoBaseActivity.getSelf().getUserID();
		Log.i(TAG, "reqHobbyGroup()-userID:" + userID);
		buffer = ByteBuffer.allocateDirect(8 + userID.getBytes().length);
		buffer.putInt(Config.CROWD_FOUND);
		put(userID);
		writeBuffer();
		Log.i(TAG, "reqHobbyGroup()以发送请求:Config.CROWD_FOUND");
	}

	public void reqHobbyUser(String userID) {
		try {
			// String userID = WoliaoBaseActivity.getSelf().getUserID();
			Log.i(TAG, "reqHobbyUser()-userID:" + userID);
			buffer = ByteBuffer.allocateDirect(8 + userID.getBytes().length);
			buffer.putInt(Config.USER_FOUND);
			put(userID);
			writeBuffer();
			Log.i(TAG, "reqHobbyUser()以发送请求:Config.USER_FOUND");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查看群资料
	public void sendLookGroupData(int groupId) {
		buffer = ByteBuffer.allocate(8);
		buffer.putInt(Config.CROWD_GET_INFO);
		buffer.putInt(groupId);
		writeBuffer();
	}

	// 我的群
	public void sendFindMyGroup(String userId) {
		Log.i("send ", "sendFindMyGroup 调用");
		userId = BaseActivity.getSelf().getUserID();
		buffer = ByteBuffer.allocate(10 + userId.getBytes().length);
		buffer.putInt(Config.CROWD_MY);
		put(userId);
		writeBuffer();
	}

	// 查找群
	public void findCrowd(String condition, String userId) {
		// String userID = WoliaoBaseActivity.getSelf().getUserID();

		if (BaseActivity.getSelf() == null) {
			Log.i("2", "user is null");
		} else {
			Log.i("2", "user is not null");
		}
		Log.i(TAG, "findCrowd-userID:" + userId);
		buffer = ByteBuffer.allocate(12 + userId.getBytes().length
				+ condition.getBytes().length);
		buffer.putInt(Config.CROWD_FIND);
		put(userId);
		put(condition);
		writeBuffer();
	}

	// 查询好友列表
	public void findFriendList(String condition, String userId) {
		try {
			Log.i("send ", "findFriendList 调用");
			buffer = ByteBuffer.allocate(8 + userId.getBytes().length);
			buffer.putInt(Config.USER_GET_ATTENT);
			put(userId);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查找用户
	public void findUser(String condition, String userID) {
		try {
			// String userID = WoliaoBaseActivity.getSelf().getUserID();
			Log.i(TAG, "findUser-userID:" + userID);
			Log.i(TAG, "condition:" + condition);
			buffer = ByteBuffer.allocate(12 + userID.getBytes().length
					+ condition.getBytes().length);
			buffer.putInt(Config.USER_FIND);
			put(userID);
			// put(condition);
			buffer.putInt(condition.getBytes().length);
			buffer.put(condition.getBytes());
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 邀请进群
	public void intiveToCrowd() {
		String senderId = null;
		int crowdId = 0;
		String friendId = null;
		buffer = ByteBuffer.allocate(20 + senderId.getBytes().length
				+ friendId.getBytes().length);
		buffer.putInt(Config.CROWD_INTIVE);
		put(senderId);
		buffer.putInt(crowdId);
		put(friendId);
		writeBuffer();
	}

	// 申请退出
	public synchronized void exit(String userId) {
		try {
			Log.i("networksend", "已发送申请退出参数");
			buffer = ByteBuffer.allocateDirect(8 + userId.getBytes().length);
			buffer.putInt(Config.REQUEST_EXIT);
			put(userId);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 是否在线
	public void lookOnline() {

	}

	// 发送获取私聊离线消息的请求
	public void getOfflineMessage(String userId) {
		try {
			Log.i(TAG, "findOfflineMessage()---userId" + userId);
			buffer = ByteBuffer.allocateDirect(8 + userId.getBytes().length);
			buffer.putInt(Config.MESSAGE_OFFLINE);
			put(userId);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送请求获取我的消息
	public boolean getMyMessage(String userId, int type) {
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			json.put("type", type);
		} catch (JSONException e) {
			System.out.println("发送获取我的消息请求时，json 构造异常");
			return false;
		}
		buffer = ByteBuffer
				.allocateDirect(8 + json.toString().getBytes().length);
		buffer.putInt(Config.MY_MESSAGE);
		System.out.println(json.toString());
		put(json.toString());
		writeBuffer();
		return true;
	}
	
	public void sendIsHasNewMsg(String userId){
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			LogUtil.i("sendHasNewMessage2");
		} catch (JSONException e) {
			System.out.println("发送获取我的消息请求时，json 构造异常");
		}
		buffer = ByteBuffer
				.allocateDirect(8 + json.toString().getBytes().length);
		buffer.putInt(Config.ISHAS_NEW_MYMESSAGE);
		
		System.out.println("hasNewMessage3json:"+json.toString());
		put(json.toString());
		writeBuffer();
	}

	// 请求查看朋友信息
	public void reqPersonINFO(String friendID) {
		try {
			String userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "reqPersonMessage()---friendID" + friendID);
			buffer = ByteBuffer.allocateDirect(12 + friendID.getBytes().length
					+ userID.getBytes().length);
			buffer.putInt(Config.USER_GET_INFO);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 请求关注
	public void reqAttention(String userID, String friendID) {
		try {
			Log.i(TAG, "用户:" + userID + "要关注friendID为:" + friendID);
			buffer = ByteBuffer.allocateDirect(12 + userID.getBytes().length
					+ friendID.getBytes().length);
			buffer.putInt(Config.USER_ADD_ATTENT);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 请求取消关注
	public void reqCanncel(String userID, String friendID) {
		try {
			Log.i(TAG, "用户:" + userID + "取消关注friendID为:" + friendID);
			buffer = ByteBuffer.allocateDirect(12 + userID.getBytes().length
					+ friendID.getBytes().length);
			buffer.putInt(Config.USER_CANCEL_ATTENT);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 请求获取别人的关注列表
	public void reqOtherAttentionList(String friendID) {
		try {
			String userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "用户:" + userID + "查看friendID为:" + friendID + "的关注列表");
			buffer = ByteBuffer.allocateDirect(12 + userID.getBytes().length
					+ friendID.getBytes().length);
			buffer.putInt(Config.USER_OTHER_ATTENT);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 请求获取别人的粉丝列表

	public void reqOtherFollowerList(String friendID) {
		try {
			String userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "用户:" + userID + "查看friendID为:" + friendID + "的粉丝列表");
			buffer = ByteBuffer.allocateDirect(12 + userID.getBytes().length
					+ friendID.getBytes().length);
			buffer.putInt(Config.USER_OTHER_FANS);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取关注列表
	public void reqMyAttentionList() {
		try {
			String userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "获取userID:" + userID + "的关注列表");
			buffer = ByteBuffer.allocateDirect(8 + userID.getBytes().length);
			buffer.putInt(Config.USER_GET_ATTENT);
			put(userID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取用户的头像
	public void reqUserHead(String userID) {
		try {
			userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "获取userID:" + userID + "的用户头像");
			buffer = ByteBuffer.allocateDirect(8 + userID.getBytes().length);
			buffer.putInt(Config.USER_GET_HEAD);
			put(userID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在buffer中放字符串的字节数组长度以及该字节数组
	 * 
	 * @param string
	 */
	public void put(String string) {
		// buffer.putInt(string.getBytes().length);
		// buffer.put(string.getBytes());
		try {
			buffer.putInt(string.getBytes("GBK").length);
			Log.i(TAG, "condition---放进去:" + string.getBytes("GBK"));
			buffer.put(string.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 对将buffer写入socketchannel.
	 */
	public void writeBuffer() {

		buffer.flip();// 对buffer进行转换
		while (buffer.hasRemaining()) {
			try {

				socketChannel.write(buffer);

				Log.i(TAG, "写入成功");
			} catch (Exception e) {
				Log.i(TAG, "写入失败");
				e.printStackTrace();
			}
		}
		buffer.clear();
	}

	private String getFileName(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.mkdirs();
		}
		return file.getName();
	}

	/**
	 * 从本地获取图片字节数组
	 * 
	 * @param fileFath
	 * @return
	 * @throws IOException
	 */
	private byte[] makeFileToByte(String fileFath) throws IOException {
		File file = new File(fileFath);

		if (file.exists()) {
			Log.i(TAG, "NewNetWorker01---文件存在");
		}
		Log.i(TAG, "NewNetWorker01---找到了图片的路径");
		FileInputStream fis = new FileInputStream(file);
		Log.i(TAG, "NewNetWorker01---成功加载了图片");
		int length = (int) file.length();
		byte[] bytes = new byte[length];
		int temp = 0;
		int index = 0;
		while (true) {
			index = fis.read(bytes, temp, length - temp);
			if (index <= 0)
				break;
			temp += index;
		}
		fis.close();
		return bytes;
	}

	// 关闭socketchannle
	public void close() {
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			Log.i(TAG, "socketchannel 关闭异常");
			e.printStackTrace();
		}
	}
}
