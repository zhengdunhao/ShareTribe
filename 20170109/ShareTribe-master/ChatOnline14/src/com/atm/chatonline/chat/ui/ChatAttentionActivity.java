package com.atm.chatonline.chat.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.commom.NewMessage;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.adapter.MyAttentionAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.example.studentsystem01.R;
/**
 * 这个类打算用来进入聊天主界面的时候，除了出现聊天界面之外，还有关注界面，与之前AttentionActivity不同的是，这里点击
 * 用户直接进入PersonChatActivity界面
 * 郑
 * */
public class ChatAttentionActivity extends BaseActivity{

	private String tag="ChatAttentionActivity";
	private List<Friend> attentionList = new ArrayList<Friend>();
	private ListView attentionLV;
	private Handler handler;
	private String userId ,friendId,nickName;
	private MyAttentionAdapter myAttentionAdapter;
	private Boolean first = true;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_attention_activity);
		initUI();
		attentionLV.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,int position,long id){
				Friend friend = attentionList.get(position);
				userId = BaseActivity.getSelf().getUserID();
				friendId = friend.getFriendID();
				nickName = friend.getNickName();
				Bitmap bm=friend.getBm();
				LogUtil.p(tag, "nickName------>>>" + nickName);
				LogUtil.p(tag, "friendID------>>>" + friendId);
				Intent intent = new Intent(ChatAttentionActivity.this,PersonChatActivity.class);
				intent.putExtra("userID", userId);
				intent.putExtra("friendId", friendId);
				intent.putExtra("nickName", nickName);
				intent.putExtra("bm", FileUtil.BitmapToBytes(bm,false));
				startActivity(intent);
			}
		});
		if(first){
			first=false;
			new Thread(myRunnable).start();
		}
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.REFRESH_UI){
					myAttentionAdapter = new MyAttentionAdapter(
							ChatAttentionActivity.this,
							R.layout.my_attention_follower_item, attentionList);
					attentionLV.setAdapter(myAttentionAdapter);
					myAttentionAdapter.notifyDataSetChanged();
					Log.i(tag, "friendList更新界面更新成功");
				}
			}
		};
 	}
	
	public void initUI(){
		attentionLV = (ListView)findViewById(R.id.chat_attention_lv);
	}
	
	// 获取我的关注列表
	Runnable myRunnable = new Runnable() {
		public void run() {
			Log.i(tag, "启动线程con.reqMyAttentionList(),获取我关注的列表");
			con.reqMyAttentionList();
		}
	};
	
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == Config.USER_ATTENTION_LIST_SUCCESS) {
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
		} else if(msg.what==Config.ISHAS_NEW_MYMESSAGE){
			Bundle bundle=msg.getData();
			NewMessage newMessage=(NewMessage)bundle.getSerializable("NewMessage");
			if(newMessage.getSum()>0){
				if(ChatMainActivity.slidingTitleLayout!=null){
					ChatMainActivity.slidingTitleLayout.showBadgeView(newMessage);
				}
			}	
		}
	}

}
