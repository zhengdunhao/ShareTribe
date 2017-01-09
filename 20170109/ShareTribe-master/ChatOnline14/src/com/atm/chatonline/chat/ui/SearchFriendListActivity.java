package com.atm.chatonline.chat.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.chat.adapter.SearchFriendAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.info.Group;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;



public class SearchFriendListActivity extends BaseActivity implements
OnClickListener,OnTouchListener {
	private List<Friend> searchFriendList = new ArrayList<Friend>();
	private ListView searchFriendLV;
	private Handler handler;
	private String userId;
	private SearchFriendAdapter friendAdapter;
	private Button btnBack;
	private EditText searchText;
	private InputMethodManager mInputMethodManager;
	
	private String tag="SearchFriendListActivity";

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_group_friend_list);
		SharedPreferences pref = getSharedPreferences("User",MODE_PRIVATE);
		userId=pref.getString("userID", "");
		Log.i(tag,"userId >>>>"+userId);
		initUI();
		searchText.setOnEditorActionListener(new OnEditorActionListener(){
			public boolean onEditorAction(TextView v,int actionId,KeyEvent event){
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					closeInput(v);
					new Thread(runnable).start();
					Log.i(tag, "键盘的搜索按钮被点击");
//					flag=true;
					
					
					return true;
				}
				return false;
			}
		});
		
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.REFRESH_UI){
					if(searchFriendList.size()!=0){
						Log.i(tag, "searchGroupList不为空");
					}
					friendAdapter = new SearchFriendAdapter(SearchFriendListActivity.this,R.layout.search_friend_item,searchFriendList);
					searchFriendLV.setAdapter(friendAdapter);
					friendAdapter.notifyDataSetChanged();
					Log.i(tag, "friendList更新界面更新成功");
				}
			}
		};
		
		searchFriendLV.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.i(tag, "listView被点击");
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		//这个方法我该过，2015.8.25 jackbing，添加了昵称变量
		searchFriendLV.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,int position,long id){
				Friend friend = searchFriendList.get(position);
				String friendID = friend.getFriendID();
				String nickName=friend.getNickName();
//				new Thread(runnable).start();
				Intent intent = new Intent(SearchFriendListActivity.this,PersonalMessageActivity.class);

				intent.putExtra("userId", userId);
				intent.putExtra("friendID", friendID);
				intent.putExtra("nickName", nickName);
				
				Log.i(tag, "nickName------>>>"+nickName);
				Log.i(tag, "SearchFriendListActivity即将跳转到PersonalMessageActivity，而且还将传递friendID为:"+friendID);
				startActivity(intent);
			}
		});
		
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			if(SearchFriendListActivity.this.getCurrentFocus()!=null){
				if(SearchFriendListActivity.this.getCurrentFocus().getWindowToken()!=null){
					closeInput(SearchFriendListActivity.this.getCurrentFocus());
					Log.i(tag, "状态栏被点击");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	
	Runnable runnable = new Runnable(){	//只对在输入法按搜索按钮有效，对直接按卡片切换无效
		public void run(){
			String searchTxt = searchText.getText().toString();
			Log.i(tag, "搜索的内容是:"+searchTxt);
			if(!searchTxt.equals("")){
//			if(choose==0){		//找群
//				con.findCrowd(searchTxt,userId);
//				click=1;
//			}else if(choose==1){	//找人
//				con.findUser(searchTxt,userId);
//				click=0;
//			}
			con.findUser(searchTxt, userId);
			Log.i(tag, "searchTxt:"+searchTxt);
			}
			
		}
	};
	void initUI(){
		searchFriendLV = (ListView)findViewById(R.id.search_group_lv);
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		searchText = (EditText)findViewById(R.id.search_edit);
		searchText.setOnTouchListener(this);
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	}
	
	void showFriendList(ArrayList list){
		Log.i(tag, "showFriendList被调用");
		searchFriendList = list;
		Message msg = new Message();
		msg.what = Config.REFRESH_UI;
		handler.sendMessage(msg);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.search_edit:    //输入框触摸实现
			mInputMethodManager.showSoftInput(searchText, 0);
			Log.i(tag, "333333333333333");
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			SearchFriendListActivity.this.onBackPressed();
			break;
//		case R.id.pager:
//			Log.i(tag, "被点了");
//			break;

		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		Log.i(tag, "process北調用");
		 if(msg.what==Config.USER_FIND_SUCCESS){
			Bundle bundle = msg.getData();
			ArrayList friendList = bundle.getParcelableArrayList("friendList");
			if(friendList.size()!=0){
				Log.i(tag, "friendList.size()不为空");
			}
			ArrayList<Friend> listFriend = (ArrayList<Friend>)friendList.get(0);
			if(listFriend.size()!=0){
				Log.i(tag, "Bundle传递ArrayList是成功的");
			}
//			if(getLocalActivityManager().getActivity("SearchFriendListActivity")!=null){
//				Log.i(tag, "SearchFriendListActivity不为空");
//				
//			}
			showFriendList(listFriend);
			
		}
		else if(msg.what==Config.NOT_FOUND){
			Log.i(tag, "没有找到相关的信息");
			Toast.makeText(SearchFriendListActivity.this, "没有找到相关的信息", Toast.LENGTH_SHORT).show();
		}else if(msg.what==Config.NOT_FOUND_HOBBY_USER){
			Log.i(tag, "没有找到你感兴趣的用户");
			Toast.makeText(SearchFriendListActivity.this, "没有找到你感兴趣的用户", Toast.LENGTH_SHORT).show();
		}
	}
	
//	Runnable runnable = new Runnable(){
//		public void run(){
//			
//		}
//	};

	public void switchInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public void openInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(findViewById(R.id.search_edit), InputMethodManager.SHOW_FORCED);
	}
	
	public boolean closeInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
}
