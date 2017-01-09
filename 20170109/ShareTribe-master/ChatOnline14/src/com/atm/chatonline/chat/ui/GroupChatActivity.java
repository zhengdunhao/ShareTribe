package com.atm.chatonline.chat.ui;
/**
 * 群聊的聊天界面
 * author--李
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.chat.adapter.GroupChatAdapter;
import com.atm.chatonline.chat.info.GroupChatMessage;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.TimeUtil;
import com.example.studentsystem01.R;



public class GroupChatActivity extends BaseActivity implements OnClickListener,ReceiveInfoListener{
	final String TAG="MultipleChatActivity";
	private String userId,groupId,content;
	private GroupChatAdapter historyAdapter;
	private List<GroupChatMessage> message=new ArrayList<GroupChatMessage>();
	private ListView chatHistory;
	private int[] headImg={R.drawable.me,R.drawable.xiaohei};
	private Button btnsubmit,onBack;
	private EditText editor;
	private Handler handler;
	private ImageButton chatHeadImg;
	private final static String STATE="G";
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_chat_view);
		con.addReceiveInfoListener(STATE,GroupChatActivity.this);//添加監聽消息的監聽器,判断当前用户是否在聊天界面
		initData();
		setAdapterForList();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				historyAdapter.notifyDataSetChanged();
			}
		};
	}
	private void initData() {
		Intent intent=getIntent();
		userId=intent.getStringExtra("userID");//获取userId和groupId
		groupId=intent.getStringExtra("groupID");
		Log.i("mutl**", userId);
		Log.i("mutl**", groupId);
		chatHistory=(ListView) findViewById(R.id.chat_view);
		editor=(EditText) findViewById(R.id.chat_editor);
		onBack=(Button) findViewById(R.id.single_chat_back);
		btnsubmit=(Button) findViewById(R.id.btn_submit);
		chatHeadImg=(ImageButton) findViewById(R.id.personal_message);
		chatHeadImg.setOnClickListener(this);
		btnsubmit.setOnClickListener(this);
		onBack.setOnClickListener(this);
	}
	/**
	 * 初始化聊天界面的Adapter，设置界面的标题，即在标题栏显示群id,通过查询本地数据库初始化消息
	 */
	public void setAdapterForList(){
		initTitleName();
		initMessage();
		historyAdapter=new GroupChatAdapter(this,message);
		chatHistory.setAdapter(historyAdapter);
	}
	/**
	 * 标题栏的群id
	 */
	public void initTitleName(){//初始化群名稱
		LayoutInflater inflater=getLayoutInflater();
		View layout=inflater.inflate(R.layout.multiple_chat_title, null);
		TextView groupName=(TextView) layout.findViewById(R.id.group_name);
		groupName.setText(groupId);
	}
	/**
	 * 初始化历史消息
	 */
	public void initMessage(){//初始化群消息
		Log.i(TAG, userId);
		Log.i(TAG, groupId);
		message=dbUtil.queryGroupChatMessage(userId, groupId);//查询本地记录
	}
	/**
	 * 点击发送按钮，标题栏的返回按钮，查看群资料的按钮的事件响应
	 */
	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.btn_submit){
			content=editor.getText().toString().trim();
			if(!content.equals("")){
				Log.i(TAG, "##### userId is"+userId);
				Log.i(TAG, "##### groupId is"+groupId);
				new Thread(runnable).start();
			}else{
				Toast.makeText(getApplicationContext(), "不能发送空消息", Toast.LENGTH_SHORT).show();
			}
			editor.setText("");
		}else if(id==R.id.personal_message){
			Intent intent=new Intent(this,GroupChatLookDataActivity.class);
			intent.putExtra("groupId", groupId);
			startActivity(intent);
			Log.i(TAG, "personal_message");
		}else if(id==R.id.single_chat_back){
			GroupChatActivity.this.onBackPressed();
		}
	}
	/**
	 * 更新聊天界面,添加一条自己发送的消息到界面且存入数据库
	 * @param type
	 * @param content
	 */
	public void sendMessage(int type,String content){
		String sendTime=TimeUtil.getAbsoluteTime();
		boolean result=false;
		if(type==Config.CROWD_MESSAGE_TEXT){
			Log.i(TAG, "PersonChatActivity----本次的短信类型是type:Config.MESSAGE_TYPE_TXT"+",内容是:"+content);
			Log.i("****", "userId is "+userId );
			Log.i("***", "userId is "+groupId );
			result=con.sendGroupText(userId,groupId,content);//發送消息
		}
		if(result){
			message.add(new GroupChatMessage(GroupChatMessage.MESSAGE_TO,content,headImg[0],sendTime,0));
			handler.sendEmptyMessage(1);
			Log.i("&&&", "userId is "+userId);
			Log.i("&&&", "userId is "+groupId);
			saveToDB(userId,groupId,GroupChatMessage.MESSAGE_TO,1,content,sendTime,0);//存入本地数据库
		}
	}
	
	/**
	 * 存入本地数据库
	 * @param self_Id
	 * @param group_Id
	 * @param direction
	 * @param type
	 * @param content
	 * @param time
	 */
	public void saveToDB(String self_Id,String group_Id,int direction,int type,String content,String time,int showTime){
		ContentValues values=new ContentValues();
		values.put("self_Id", self_Id);
		values.put("group_Id", group_Id);
		values.put("direction", direction);
		values.put("type", 1);//这里是指消息类型，但是目前并没有定义，目前只是文本类型
		values.put("content", content);
		values.put("time", time);
		values.put("showTime", showTime);
		dbUtil.insertGroupChatMessage(values);
	}
	/**
	 * 子线程执行与网络相关的操作
	 */
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			sendMessage(Config.CROWD_MESSAGE_TEXT,content);
		}
	};
	/**
	 * 处理接收到的消息
	 */
	public void processMessage(Message msg){
		Log.i(TAG, "PersonChatActivity----聊天界面中接收到新的消息");
		if(msg.what==Config.CROWD_MESSAGE_FROM){
			Log.i(TAG, "PersonChatActivity----收到msg.what==Config.RECEIVE_MESSAGE");
			Bundle bundle = msg.getData();
			GroupChatMessage newMessages = (GroupChatMessage)bundle.getSerializable("groupChatMessage");
			message.add(newMessages);
			Log.i(TAG, "这是我接收到的短信，即将准备存入本地数据库，内容是"+newMessages.getContent());
			String sendrerID = newMessages.getSenderId();
			String groupID = newMessages.getGroupId();
			int direction = newMessages.getDirection();
			int type = newMessages.getType();
			String time = newMessages.getTime();
			String content = newMessages.getContent();
			int showTime = newMessages.getShowTime();
			Log.i(TAG, "senderId:"+sendrerID+"、groupID:"+groupID+"direction"+direction);
			Log.i(TAG, "type:"+type+"、time:"+time);
			saveToDB(sendrerID,groupID,direction,type,time,content,showTime);
			handler.sendEmptyMessage(1);
			Log.i(TAG, "PersonChatActivity----聊天界面更新成功");
		}
	}
	/**
	 * 判断是否在聊天界面
	 */
	@Override
	public boolean isChatting(Object info) {
		GroupChatMessage groupChatMessage=(GroupChatMessage) info;
		Log.i(TAG, "direction="+groupChatMessage.getDirection());
		Log.i(TAG, "time="+groupChatMessage.getTime());
		Log.i(TAG, "headimg="+groupChatMessage.getHeadImg());
		Log.i(TAG, "content="+groupChatMessage.getContent());
		return true;
	}
	
	
}
