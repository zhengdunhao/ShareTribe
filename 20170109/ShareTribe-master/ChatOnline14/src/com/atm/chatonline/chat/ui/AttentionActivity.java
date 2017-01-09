package com.atm.chatonline.chat.ui;
/**
 * 通过Config.PERSONALMESSAGEACTIVITY来表示 从PersonalMessageActivity跳转到AttentionActivity
 * Config.BBSPOSTDETAILVIEW来表示 从BBSPostDetailView跳转到AttentionActivity
 * */

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.chat.adapter.AttentionAdapter;
import com.atm.chatonline.chat.adapter.MyAttentionAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

public class AttentionActivity extends BaseActivity implements
		OnClickListener {
	private String tag = "AttentionActivity";

	private List<Friend> attentionList = new ArrayList<Friend>();
	private ListView attentionLV;
	private Handler handler;
	private String userId, friendId, nickName;
	private int fromActivity;
	private AttentionAdapter attentionAdapter;
	private MyAttentionAdapter myAttentionAdapter;
	private Button btnBack;
	private Intent intent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attention_follower_list);
		intent = getIntent();
		userId = intent.getStringExtra("userID");

		// 2015-10-22做修改，目的是尝试如何实现返回@功能的名字
		// 判断来自哪里的Activity
		fromActivity = intent.getIntExtra("fromActivity", 2010);

		Log.i(tag, "接收到formActivity:"+fromActivity);
		Log.i(tag, "我是userId:" + userId + "、查看的是friendId:" + friendId);
		initUI();
		btnBack.setOnClickListener(this);
		attentionLV.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Friend friend = attentionList.get(position);
				

				if(fromActivity == Config.PERSONALMESSAGEACTIVITY){
				Intent intent = new Intent(AttentionActivity.this,
						PersonalMessageActivity.class);
				String friendID = friend.getOtherID();
				String nickName = friend.getNickName();
				Log.i(tag, "nickName------>>>" + nickName);
				Log.i(tag, "friendID------>>>" + friendID);

				intent.putExtra("userId", userId);
				intent.putExtra("friendID", friendID);
				intent.putExtra("nickName", nickName);

				
				Log.i(tag,
						"AttentionActivity即将跳转到PersonalMessageActivity，而且还将传递friendID为:"
								+ friendID);
				startActivity(intent);
				}
				
				else if(fromActivity == Config.BBSPOSTDETAILVIEW){
					Intent intent = new Intent();
					String friendID = friend.getFriendID();
					String nickName = friend.getNickName();
					Log.i(tag, "nickName------>>>" + nickName);
					Log.i(tag, "friendID------>>>" + friendID);
					intent.putExtra("friendID", friendID);
					intent.putExtra("nickName", nickName);
					
					Log.i(tag, "nickName------>>>" + nickName);
					Log.i(tag,
							"AttentionActivity即将跳转到BBSPostDetailView，而且还将传递friendID为:"
									+ friendID);
					AttentionActivity.this.setResult(RESULT_OK,intent);
					AttentionActivity.this.finish();
				}else if(fromActivity == Config.USER_CENTER){
					Intent intent = new Intent(AttentionActivity.this,
							PersonalMessageActivity.class);
					String friendID = friend.getFriendID();
					String nickName = friend.getNickName();
					Log.i(tag, "nickName------>>>" + nickName);
					Log.i(tag, "friendID------>>>" + friendID);

					intent.putExtra("userId", userId);
					intent.putExtra("friendID", friendID);
					intent.putExtra("nickName", nickName);
					Log.i(tag,
							"AttentionActivity即将跳转到PersonalMessageActivity，而且还将传递friendID为:"
									+ friendID);
					startActivity(intent);
				}
			}
		});

		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == Config.REFRESH_UI) {
					if (attentionList.size() != 0) {
						Log.i(tag, "searchGroupList不为空");
					}
					//从个人信息跳转
					if (fromActivity == Config.PERSONALMESSAGEACTIVITY) {
						
						attentionAdapter = new AttentionAdapter(
								AttentionActivity.this,
								R.layout.attention_follower_item, attentionList);
						attentionLV.setAdapter(attentionAdapter);
						attentionAdapter.notifyDataSetChanged();
						Log.i(tag, "friendList更新界面更新成功");
					}
					//从@功能跳转过来的
					else if(fromActivity==Config.BBSPOSTDETAILVIEW ){
						myAttentionAdapter = new MyAttentionAdapter(
								AttentionActivity.this,
								R.layout.my_attention_follower_item, attentionList);
						attentionLV.setAdapter(myAttentionAdapter);
						myAttentionAdapter.notifyDataSetChanged();
						Log.i(tag, "friendList更新界面更新成功");
					}//从个人中心跳转过来
					else if(fromActivity==Config.USER_CENTER){
						myAttentionAdapter = new MyAttentionAdapter(
								AttentionActivity.this,
								R.layout.my_attention_follower_item, attentionList);
						attentionLV.setAdapter(myAttentionAdapter);
						myAttentionAdapter.notifyDataSetChanged();
						Log.i(tag, "friendList更新界面更新成功");
					}
				}
			}
		};
	}

	void initUI() {
		attentionLV = (ListView) findViewById(R.id.attention_follower_lv);
		btnBack = (Button) findViewById(R.id.back_btn);
		Log.i(tag, "fromActivity:" + fromActivity);
		if (fromActivity == Config.PERSONALMESSAGEACTIVITY) {
			friendId = intent.getStringExtra("friendID");
			nickName = intent.getStringExtra("nickName");
			new Thread(othRunnable).start();
		} else if (fromActivity == Config.BBSPOSTDETAILVIEW) {
			new Thread(myRunnable).start();
		}else if(fromActivity == Config.USER_CENTER){
			new Thread(myRunnable).start();
		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == Config.SUCCESS) {
			Bundle bundle = msg.getData();
			ArrayList friendList = bundle
					.getParcelableArrayList("attentionList");
			if (friendList.size() != 0) {
				Log.i(tag, "friendList.size()不为空");
			}
			attentionList = (ArrayList<Friend>) friendList.get(0);
			Message msg1 = new Message();
			msg1.what = Config.REFRESH_UI;
			handler.sendMessage(msg1);
		} else if (msg.what == Config.USER_ATTENTION_LIST_SUCCESS) {
			Bundle bundle = msg.getData();
			ArrayList friendList = bundle
					.getParcelableArrayList("myAttentionList");
			if (friendList.size() != 0) {
				Log.i(tag, "friendList.size()不为空");
			}
			attentionList = (ArrayList<Friend>) friendList.get(0);
			Message msg1 = new Message();
			msg1.what = Config.REFRESH_UI;
			handler.sendMessage(msg1);
		} else {
			Log.i(tag, "其他人的关注列表显示失败");
		}
	}

	// 获取别人的关注列表
	Runnable othRunnable = new Runnable() {
		public void run() {
			Log.i(tag, "启动线程con.reqAttentionList(),friendId:" + friendId);
			Log.i(tag, "从而获取friendId:" + friendId + " 关注的列表");
			con.reqOtherAttentionList(friendId);
		}
	};
	// 获取我的关注列表
	Runnable myRunnable = new Runnable() {
		public void run() {
			Log.i(tag, "启动线程con.reqMyAttentionList(),获取我关注的列表");
			con.reqMyAttentionList();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_btn:
			setResult(RESULT_OK,intent);
			AttentionActivity.this.onBackPressed();
			break;
		}
	}
}
