package com.atm.chatonline.chat.ui;

import java.util.ArrayList;
import java.util.List;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.adapter.SingleMessageAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.example.studentsystem01.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;



public class PersonalMessageActivity extends BaseActivity implements OnClickListener{
	private String tag = "PersonalMessageActivtiy";
	
	private Button btnBack,btnChat;	//返回按钮
	private ImageButton btnAttention;	//爱心
	private CircleImageView imgHead;	//头像
	private TextView peopleName;	//人物名
	private TextView attentions;	//关注人数
	private TextView followers;		//粉丝人数
	private TextView publishNoteNum;//发帖数量
	private TextView collectNoteNum;//收藏帖数量
	private ListView messageLV;		//消息列表
	private List<Friend> friendMessage = new ArrayList<Friend>();
	private Handler handler;
	String  userID = BaseActivity.getSelf().getUserID();
	private String friendID;
	private SingleMessageAdapter singleMessageAdapter;
	private Bitmap trueBitmap;//黄色
	private Bitmap falseBitmap;//白色
	private LinearLayout ll_attentions,ll_followers,ll_publish_note,ll_collect_note;
	
	public void onCreate(Bundle savedInstanceState){
		Log.i(tag, "即将进入PersonalMessageActivity");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_messge_view);
		Intent intent = getIntent();
		friendID = intent.getStringExtra("friendID");
		Log.i(tag, "从SearchFriendListActivity传来的friendID:"+friendID);
		Log.i(tag, "userID="+userID);
		initUI();
		
		handler = new Handler(){
			@SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			public void handleMessage(Message msg02){
				if(msg02.what==Config.REFRESH_UI){
					imgHead.setImageBitmap(friendMessage.get(0).getBm());//friendMessage.get(0).getBm()
					peopleName.setText(friendMessage.get(0).getNickName());
					Log.i(tag, "朋友的名字是:"+friendMessage.get(0).getNickName());
					attentions.setText(friendMessage.get(0).getAttentions());
					followers.setText(friendMessage.get(0).getFollowers());
					publishNoteNum.setText(friendMessage.get(0).getPublishNoteNum());
					collectNoteNum.setText(friendMessage.get(0).getCollectNoteNum());
					singleMessageAdapter = new SingleMessageAdapter(PersonalMessageActivity.this,R.layout.personal_message_simpleitem,friendMessage);
					messageLV.setAdapter(singleMessageAdapter);
					singleMessageAdapter.notifyDataSetChanged();
					Log.i(tag, "relationship:"+friendMessage.get(0).getRelationship());
					if(friendMessage.get(0).getRelationship()==Config.RELATIONSHIP_ATTENTION){
						Log.i(tag, "已经关注了此人");
						btnAttention.setBackground(new BitmapDrawable(trueBitmap));
					}else{
						Log.i(tag, "此人还未被关注");
						btnAttention.setBackground(new BitmapDrawable(falseBitmap));
					}
					Log.i(tag, "friendMessage更新界面更新成功");
				}
			}
		};
		btnBack.setOnClickListener(this);
		btnChat.setOnClickListener(this);
		btnAttention.setOnClickListener(this);
		attentions.setOnClickListener(this);
		followers.setOnClickListener(this);
		ll_attentions.setOnClickListener(this);
		ll_followers.setOnClickListener(this);
		ll_publish_note.setOnClickListener(this);
		ll_collect_note.setOnClickListener(this);
//		initAttention();
	}
	
	void initUI(){
		btnBack = (Button)findViewById(R.id.single_chat_back);
		btnChat=(Button)findViewById(R.id.btn_sixin);
		btnAttention = (ImageButton)findViewById(R.id.personal_attention);
		imgHead = (CircleImageView)findViewById(R.id.head_img);
		peopleName = (TextView)findViewById(R.id.sixin_username);
		attentions = (TextView)findViewById(R.id.attentions);
		followers = (TextView)findViewById(R.id.followers);
		publishNoteNum = (TextView)findViewById(R.id.publish_note_number);
		collectNoteNum = (TextView)findViewById(R.id.collect_note_number);
		messageLV = (ListView)findViewById(R.id.message_list);
		ll_attentions = (LinearLayout)findViewById(R.id.ll_attentions);
		ll_followers = (LinearLayout)findViewById(R.id.ll_followers);
		ll_publish_note = (LinearLayout)findViewById(R.id.ll_publish_note);
		ll_collect_note = (LinearLayout)findViewById(R.id.ll_collect_note);
		trueBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.heart_pressed);
		falseBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.heart_normal);
		new Thread(runnable).start();//搜索后，点击某个人，发生跳转，在初始化时，立即发送请求得到这个人的具体资料。
	}
	

	
	@SuppressLint("NewApi")
	void initAttention(){
		
//		Log.i(tag, "initAttention()---userID:"+userID+"、friendID:"+friendID+"、relationship:"+friendMessage.get(0).getRelationship());
//		if(dbUtil.queryIsAttetion(userID, friendID, Config.RELATIONSHIP_ATTENTION)){
//			Log.i(tag, "通过查找数据库，该用户已被关注");
//			
//			btnAttention.setBackground(new BitmapDrawable(trueBitmap));
//		}else{
//			Log.i(tag, "通过查找数据库，该用户还未被关注");
//			
//			btnAttention.setBackground(new BitmapDrawable(falseBitmap));
//			
//		}
		
		
	}
	
	
	
	@SuppressLint("NewApi")
	public void onClick(View v){
		int id = v.getId();
		Log.i(tag, "onClick()-----userID:"+userID);
		if(id==R.id.personal_attention){
//			if(dbUtil.queryIsAttetion(userID, friendID, Config.RELATIONSHIP_ATTENTION)){
//				btnAttention.setBackground(new BitmapDrawable(falseBitmap));
//				new Thread(reqCanncelRunnable).start();
//				dbUtil.deleteFriendList(userID, friendID, Config.RELATIONSHIP_ATTENTION);
//				Log.i(tag, "该用户:"+friendID+"已被取消");
//			}else{
//				btnAttention.setBackground(new BitmapDrawable(trueBitmap));
//				new Thread(reqAttentionRunnable).start();
//				saveFriendToDB(userID,friendID,friendMessage.get(0).getNickName(),friendMessage.get(0).getDepartment(),Config.RELATIONSHIP_ATTENTION);
//				Log.i(tag, "该用户:"+friendID+"已被成功关注");
//			}
			if(friendMessage.get(0).getRelationship()==Config.RELATIONSHIP_ATTENTION){
				Log.i(tag, "已经关注了此人，现在要取消关注此人");
				btnAttention.setBackground(new BitmapDrawable(falseBitmap));
				new Thread(reqCanncelRunnable).start();
				friendMessage.get(0).setRelationship(Config.RELATIONSHIP_NO_MATTER);
				Log.i(tag, "已经取消关注此人");
			}else{
				Log.i(tag, "此人还没关注，现在准备关注");
				btnAttention.setBackground(new BitmapDrawable(trueBitmap));
				new Thread(reqAttentionRunnable).start();
				friendMessage.get(0).setRelationship(Config.RELATIONSHIP_ATTENTION);
				Log.i(tag, "已经关注此人");
			}
			
			
		}else if(id==R.id.single_chat_back){
			PersonalMessageActivity.this.onBackPressed();
			trueBitmap.recycle();
			falseBitmap.recycle();
			Log.i(tag, "trueBitmap和falseBitmap都被回收");
		}else if(id==R.id.btn_sixin){
			Intent intent=new Intent(this,PersonChatActivity.class);
			intent.putExtra("userID", userID);
			intent.putExtra("friendId", friendMessage.get(0).getFriendID());
			intent.putExtra("nickName", friendMessage.get(0).getNickName());
			intent.putExtra("bm", FileUtil.BitmapToBytes(friendMessage.get(0).getBm(),false));
			startActivity(intent);
		}else if(id==R.id.ll_attentions){
			Log.i(tag, "你点击了这个人的关注列表");
			Log.i(tag, "userID:"+userID+"、friendID:"+friendMessage.get(0).getFriendID()+"nickName:"+friendMessage.get(0).getNickName());
			Intent intent=new Intent(this,AttentionActivity.class);
			
			intent.putExtra("userID", userID);
			intent.putExtra("friendID", friendMessage.get(0).getFriendID());
			intent.putExtra("nickName", friendMessage.get(0).getNickName());
			startActivity(intent);
		}else if(id==R.id.ll_followers){
			Log.i(tag, "你点击了这个人的粉丝列表");
			Log.i(tag, "userID:"+userID+"、friendID:"+friendMessage.get(0).getFriendID()+"nickName:"+friendMessage.get(0).getNickName());
			Intent intent=new Intent(this,FollowersActivity.class);
			
			intent.putExtra("userID", userID);
			intent.putExtra("friendID", friendMessage.get(0).getFriendID());
			intent.putExtra("nickName", friendMessage.get(0).getNickName());
			startActivity(intent);
		}else if(id==R.id.ll_publish_note){
			Intent intent=new Intent(this,PeoplePublishedNote.class);
			
			intent.putExtra("userID", userID);
			intent.putExtra("friendID", friendMessage.get(0).getFriendID());
			intent.putExtra("nickName", friendMessage.get(0).getNickName());
			startActivity(intent);
		}else if(id==R.id.ll_collect_note){
			Intent intent=new Intent(this,PeopleCollectedNote.class);
			
			intent.putExtra("userID", userID);
			intent.putExtra("friendID", friendMessage.get(0).getFriendID());
			intent.putExtra("nickName", friendMessage.get(0).getNickName());
			startActivity(intent);
		}
	}
	
//	public void saveFriendToDB(String userID,String friendID,String nickName,String department,int relationship){
//		ContentValues values = new ContentValues();
//		values.put("self_Id",userID);
//		values.put("friend_Id",friendID);
//		values.put("friend_nickName",nickName);
//		values.put("department",department);
//		values.put("relationship",relationship);
//		dbUtil.insertFriendList(values);
//		
//	}
	Runnable reqCanncelRunnable = new Runnable(){
		public void run(){
			con.reqCanncel(userID, friendID);
			Log.i(tag, "启动线程con.reqCanncelRunnable(userID, friendID)");
		}
	};
	
	Runnable reqAttentionRunnable = new Runnable(){
		public void run(){
			con.reqAttention(userID, friendID);
			Log.i(tag, "启动线程con.reqAttention(userID, friendID)");
		}
	};
	
	Runnable runnable = new Runnable(){
		public void run(){
			Log.i(tag, "启动线程con.reqPersonINFO(friendID),friendID:"+friendID);
			con.reqPersonINFO(friendID);
		}
	};

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==Config.SUCCESS){
			Bundle bundle = msg.getData();
			ArrayList  friendINFOList = bundle.getParcelableArrayList("friendINFOList");
			ArrayList<Friend> listINFO = (ArrayList<Friend>)friendINFOList.get(0);
			friendMessage = listINFO;
			if(friendMessage.size()!=0){
				Log.i(tag, "friendMessage不为空，长度为:"+friendMessage.size());
			}
			Message msg02 = new Message();
			msg02.what = Config.REFRESH_UI;
			handler.sendMessage(msg02);
		}else if(msg.what==Config.SEND_NOTIFICATION){
			Log.i(tag, "新消息通知");
			sendNotifycation();
		}
		
	}
}
