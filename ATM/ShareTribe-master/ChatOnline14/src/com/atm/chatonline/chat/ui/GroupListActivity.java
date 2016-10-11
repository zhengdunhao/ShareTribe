package com.atm.chatonline.chat.ui;

/**
 * 群聊的列表*/
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.atm.chatonline.chat.adapter.GroupAdapter;
import com.atm.chatonline.chat.info.Group;
import com.example.studentsystem01.R;



public class GroupListActivity extends Activity{
	private List<Group> GroupList;
	private  ListView listView;
	private String userID;
	private String tag="GroupListActivity";
	private static Handler handler;
	private GroupAdapter groupAdapter;
	protected void onCreate(Bundle savedInstanceState){
		Log.i(tag, "GroupListActivity--------即将进入");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.group_list_view);
		initData();
		Log.i(tag, "GroupListActivity--------完成初始化朋友列表");
		initAdapter();
		listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,int position,long id){
				Log.i(tag, "FriendListActivity ------222");
				Group group = GroupList.get(position);
				String groupID=group.getGroupId();
				Log.i(tag, "FriendListActivity --333--userID:"+userID);
				Log.i(tag, "FriendListActivity --333--friendID:"+groupID);
				Intent intent=new Intent(GroupListActivity.this, GroupChatActivity.class);
				Log.i(tag, "FriendListActivity ------444");
				intent.putExtra("userID", userID);
				intent.putExtra("groupID", groupID);
				startActivity(intent);
				Log.i(tag, "FriendListActivity ------555");
			}
		});
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what==1){
					initAdapter();
					groupAdapter.notifyDataSetChanged();
				}
			}
		};
	}
	/**
	 * 初始化要用到的数据，userId,获取从ChatMainActivtiy传递过来的群列表
	 */
	private void initData() {
		userID=getIntent().getStringExtra("userId");
		Log.i(tag, "GroupListActivity-------我从User类中获得userID是:"+userID);
		ArrayList list=getIntent().getParcelableArrayListExtra("list");
		GroupList=(List<Group>)list.get(0);	
	}
	/**
	 * 初始化GroupAdapter
	 */
	private void initAdapter(){
		groupAdapter= new GroupAdapter(GroupListActivity.this,R.layout.group_friend_item,GroupList);
		listView = (ListView)findViewById(R.id.list_group);
		listView.setAdapter(groupAdapter);
	}
	
	/**
	 * 新建群时，返回主界面调用，用来更新群列表项
	 * @throws InterruptedException 
	 */
	protected void upDate(List<Group> groupList){
		GroupList=groupList;
		Log.i(tag, "upDate被调用");
		handler.sendEmptyMessage(1);
	}	
}
