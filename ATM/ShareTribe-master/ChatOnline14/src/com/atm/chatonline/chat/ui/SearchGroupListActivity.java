package com.atm.chatonline.chat.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.atm.chatonline.chat.adapter.SearchGroupAdapter;
import com.atm.chatonline.chat.info.Group;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;



public class SearchGroupListActivity extends Activity{
	
	private List<Group> searchGroupList = new ArrayList<Group>();
	private ListView searchGroupLV;
	private Handler handler;
	private SearchGroupAdapter groupAdapter;
	
	private String tag="SearchGroupListActivity";

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_group_list);
		initUI();
		
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.REFRESH_UI){
					if(searchGroupList.size()!=0){
						Log.i(tag, "searchGroupList不为空");
					}
					groupAdapter = new SearchGroupAdapter(SearchGroupListActivity.this,R.layout.search_group_item,searchGroupList);
					searchGroupLV.setAdapter(groupAdapter);
					groupAdapter.notifyDataSetChanged();
					Log.i(tag, "UI 被调用");
				}
			}
		};
		
		searchGroupLV.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.i(tag, "listView被点击");
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
			}
			
		});
		
	}

	
	void initUI(){
		searchGroupLV = (ListView)findViewById(R.id.search_group_lv);
	}
	
	void showGroupList(ArrayList list){
		Log.i(tag, "showGroupList被调用");
		searchGroupList = list;
		Message msg = new Message();
		msg.what = Config.REFRESH_UI;
		handler.sendMessage(msg);
		
	}
	
	
	
	
	
	

}
