package com.atm.chatonline.usercenter.activity.usercenter;



import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.atm.chatonline.usermsg.ui.IndicatorFragmentActivity;
import com.example.studentsystem01.R;

public class MyBbs extends IndicatorFragmentActivity implements OnClickListener{

	public static String COOKIE;
	public static final int PUBLISH_NOTE=0; 
	public static final int COLLECT_NOTE=1; 
	public static final int RELATED_NOTE=2; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		initView();
		setCookie();
	}
	
	/**
	 * 初始化参数：
	 * 获取由Bundle传入的参数。
	 * 该参数是用于传递给服务器端所需的参数。
	 * 四个参数：id;tip;relativePath
	 */
	public  String setCookie() {
	
		COOKIE = getIntent().getExtras().getString("cookie");
		return COOKIE;
	}
	private void initView(){
		findViewById(R.id.single_chat_back).setOnClickListener(this);
		TextView titleText=(TextView)findViewById(R.id.indicator_title_text);
		titleText.setText("帖  子");
	}
	
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.single_chat_back:
			super.onBackPressed();
			break;
			default:
				break;
		}
		
	}

	
	
	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(PUBLISH_NOTE, getString(R.string.mybbs_publish_note),MyPublishedNote.class));
		tabs.add(new TabInfo(COLLECT_NOTE, getString(R.string.mybbs_collect_note),MyCollectionNote.class));
		tabs.add(new TabInfo(RELATED_NOTE, getString(R.string.mybbs_relate_note),MyRelatedNote.class));
		return PUBLISH_NOTE;
	}
	
}
