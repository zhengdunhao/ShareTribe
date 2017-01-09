package com.atm.chatonline.setting.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.setting.util.SlideSwitch;
import com.example.studentsystem01.R;
/**
 * Author:ZDH
 * Content:主要是设置界面，内容有账户安全，字体大小，勿扰模式，反馈，关于我们
 * */
public class SettingView extends BaseActivity implements OnClickListener{
	private String tag = "SettingView";
	private Button btnBack;
	private LinearLayout account_ll,font_size_ll,feedback_ll,about_us_ll;
	private TextView title;
	private float fontSize;
	private SpannableString sps = null;
	private String titleName ="设  置";
	private SlideSwitch swiDisturb;
	private int swiStatus;
	private List<View> listView = new ArrayList<View>();
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_view);
		initData();
		initUI();
	}

	void initUI(){
		btnBack = (Button)findViewById(R.id.btn_back);
		account_ll = (LinearLayout)findViewById(R.id.acconut_for_safe);
//		font_size_ll = (LinearLayout)findViewById(R.id.font_size);
		feedback_ll = (LinearLayout)findViewById(R.id.feedback);
		about_us_ll = (LinearLayout)findViewById(R.id.about_us);
		title = (TextView)findViewById(R.id.title);
		swiDisturb = (SlideSwitch)findViewById(R.id.switch_disturb);
		swiDisturb.setText("", "");
		
		sps = new SpannableString(titleName);
		sps.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, titleName.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体 0-4字体的长度
		title.setText(getString(R.string.text_setting));
		btnBack.setOnClickListener(this);
		account_ll.setOnClickListener(this);
//		font_size_ll.setOnClickListener(this);
		feedback_ll.setOnClickListener(this);
		about_us_ll.setOnClickListener(this);
		
		swiDisturb.setOnClickListener(this);
//		swiDisturb.setOnSwitchChangedListener(onSwitchChangedListener)
		
	}
	
	void initData(){
		fontSize = getFontSize();
		LogUtil.p(tag, "从保存中获得的字体大小是:"+fontSize);
	}
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

//	protected void onResume(){
//		super.onResume();
//		getAllChildViews(SettingView.this);
//	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			this.onBackPressed();
			break;
			
		case R.id.acconut_for_safe:
			LogUtil.p(tag, "账户安全被点击");
			Intent intent = new Intent(SettingView.this,AccountSafe.class);
			startActivity(intent);
			break;
			//经过讨论先暂时去掉2017.7.19
//		case R.id.font_size:
//			LogUtil.p(tag, "字体大小被点击");
////			Intent intent1 = new Intent(SettingView.this,SettingFont.class);
////			startActivity(intent1);
//			break;
			
//		case R.id.switch_disturb:
//			LogUtil.p(tag, "勿扰模式被点击");
//			if(swiDisturb.mSwitchStatus==swiDisturb.SWITCH_ON){
//				BaseActivity.isDisturb = false;//表示进入 勿扰模式
//				LogUtil.p(tag, "勿扰模式");
//			}else{
//				BaseActivity.isDisturb = true;//表示进入  可以接收信息模式
//				LogUtil.p(tag, "接收信息模式");
//			}
			
		case R.id.feedback:
			LogUtil.p(tag, "反馈被点击");
			Intent intent2 = new Intent(SettingView.this,FeedbackView.class);
			startActivity(intent2);
			break;
			
		case R.id.about_us:
			LogUtil.p(tag, "关于我们被点击");
			Intent intent3 = new Intent(SettingView.this,AboutUsView.class);
			startActivity(intent3);
			break;
		}
	}

}
