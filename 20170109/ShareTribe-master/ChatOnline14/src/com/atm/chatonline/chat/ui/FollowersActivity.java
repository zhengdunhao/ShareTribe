package com.atm.chatonline.chat.ui;

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
import android.widget.Button;
import android.widget.ListView;

import com.atm.chatonline.chat.adapter.AttentionAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

public class FollowersActivity extends BaseActivity implements OnClickListener{
	private String tag = "FollowersActivity";
	
	private List<Friend> followersList = new ArrayList<Friend>();
	private ListView followerLV;
	private Handler handler;
	private String userId,friendId,nickName;
	private AttentionAdapter attentionAdapter;
	private Button btnBack;
	private Intent intent;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attention_follower_list);
		intent=getIntent();
		userId = intent.getStringExtra("userID");
		friendId = intent.getStringExtra("friendID");
		nickName = intent.getStringExtra("nickName");
		Log.i(tag, "我是userId:"+userId+"、查看的是friendId:"+friendId);
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.REFRESH_UI){
					if(followersList.size()!=0){
						Log.i(tag, "searchGroupList不为空");
					}
					attentionAdapter = new AttentionAdapter(FollowersActivity.this,R.layout.attention_follower_item,followersList);
					followerLV.setAdapter(attentionAdapter);
					attentionAdapter.notifyDataSetChanged();
					Log.i(tag, "friendList更新界面更新成功");
				}
			}
		};
		initUI();
		btnBack.setOnClickListener(this);
	}
	void initUI(){
		followerLV = (ListView)findViewById(R.id.attention_follower_lv);
		btnBack = (Button)findViewById(R.id.back_btn);
		new Thread(runnable).start();
	}
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==Config.SUCCESS){
			Bundle bundle = msg.getData();
			ArrayList friendList = bundle.getParcelableArrayList("attentionList");
			if(friendList.size()!=0){
				Log.i(tag, "friendList.size()不为空");
			}
			followersList = (ArrayList<Friend>)friendList.get(0);
			Message msg1 = new Message();
			msg1.what = Config.REFRESH_UI;
			handler.sendMessage(msg1);
		}else{
			Log.i(tag, "其他人的关注列表显示失败");
		}
	}
	
	Runnable runnable = new Runnable(){
		public void run(){
			Log.i(tag, "启动线程con.reqOtherFollowerList(),friendId:"+friendId);
			Log.i(tag, "从而获取friendId:"+friendId+" 关注的列表");
			con.reqOtherFollowerList(friendId);
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back_btn:
			setResult(RESULT_OK,intent);
			FollowersActivity.this.onBackPressed();
			break;
		}
	}
}
