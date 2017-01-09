package com.atm.chatonline.chat.ui;

/**
 * 私聊的列表
 * 
 * */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.adapter.FriendAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.util.FileUtil;
import com.example.studentsystem01.R;



public class FriendListActivity extends Activity{

	private List<Friend> FriendList ;
	private  ListView listView;
	private String userID="10001";
	private String tag="FriendListActivity";
	private Handler handler;
	private FriendAdapter friendAdapter;
	private long mExitTime;
	protected void onCreate(Bundle savedInstanceState){
		Log.i(tag, "FriendListActivity--------即将进入");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friend_list_view);
		initData();
		Log.i(tag, "FriendListActivity-------我从User类中获得userID是:"+userID);
		Log.i(tag, "FriendListActivity--------完成"+userID+"初始化朋友列表");
		initAdapter();
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,int position,long id){
				Log.i(tag, "FriendListActivity ------222");
				Log.i(tag, "FriendList的大小"+FriendList.size());
				LogUtil.p(tag, "position:"+position);
				userID=BaseActivity.getSelf().getUserID();
				Friend friend = FriendList.get(position);
				String friendID=friend.getFriendID();
				String nickName=friend.getNickName();
				Bitmap bm=friend.getBm();
				Log.i(tag, "FriendListActivity --333--userID:"+userID);
				Log.i(tag, "FriendListActivity --333--friendID:"+friendID);
				Intent intent=new Intent(FriendListActivity.this, PersonChatActivity.class);
				Log.i(tag, "FriendListActivity ------444");
				intent.putExtra("userID", userID);
				intent.putExtra("friendId", friendID);
				intent.putExtra("nickName", nickName);
				intent.putExtra("bm", FileUtil.BitmapToBytes(bm,false));
				Log.i(tag, "传送PersonChatActivity的userID:"+userID+"、friendID:"+friendID);
				startActivity(intent);
				Log.i(tag, "FriendListActivity ------555");
			}
		});
		handler=new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==1){
					initAdapter();
					friendAdapter.notifyDataSetChanged();
				}
			}
		};
	}
	
	private void initData(){
		userID=BaseActivity.getSelf().getUserID();
		Log.i(tag, "GroupListActivity-------我从User类中获得userID是:"+userID);
		ArrayList list=getIntent().getParcelableArrayListExtra("list");
		FriendList=(List<Friend>) list.get(0);
	}
	private void initAdapter() {
		friendAdapter = new FriendAdapter(FriendListActivity.this,R.layout.group_friend_item,FriendList);//************
		if(FriendList.size()>0){
			Log.i(tag, "nickName ="+FriendList.get(0).getNickName());
		
		}else{
			Log.i(tag, "friendIdnickName ***");
		}
		listView = (ListView)findViewById(R.id.list_friend);
		listView.setAdapter(friendAdapter);
	}


	@Override
    public void onBackPressed() {
//      super.onBackPressed();
        finish();
    }
	
	
	protected void upDate(List<Friend> FriendList){
		this.FriendList=FriendList;
		Log.i(tag, "upDate()---FriendList :"+this.FriendList.size());
		handler.sendEmptyMessage(1);
	}

}
