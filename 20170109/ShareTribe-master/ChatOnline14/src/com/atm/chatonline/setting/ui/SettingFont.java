package com.atm.chatonline.setting.ui;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atm.chatonline.all.util.FontConfig;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;
/**
 * Author:ZDH
 * Content:这个主要是做字体的改变，但是发现当初没有先自定义一个控件继承TextView，导致现在如果要全局的字体都发送改变很难
 * */

public class SettingFont extends BaseActivity implements OnClickListener{
	
	private String tag = "SettingFont";
	private LinearLayout small_font_ll,medium_font_ll,big_font_ll;
	private Button btnBack;
	private Handler handler;
	
	private TextView title;
	private SpannableString sps = null;
	private String titleName ="字体大小";
	private List<View> listView = new ArrayList<View>();
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_font);
		initUI();
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==FontConfig.SMAIL_FONT){
					LogUtil.p(tag, "小号字体被点");
					setFontSize(FontConfig.SMAIL_FONT);
				}else if(msg.what==FontConfig.NOMAL_FONT){
					LogUtil.p(tag, "中号字体被点");
					setFontSize(FontConfig.NOMAL_FONT);
				}else if(msg.what==FontConfig.BIG_FONT){
					LogUtil.p(tag, "大号字体被点");
					setFontSize(FontConfig.BIG_FONT);
				}
				TipAlertDialog("提示","字体大小设置完毕",true);
			}
		};
	}

	void initUI(){
		small_font_ll = (LinearLayout)findViewById(R.id.small_font);
		medium_font_ll = (LinearLayout)findViewById(R.id.medium_font);
		big_font_ll = (LinearLayout)findViewById(R.id.big_font);
		btnBack = (Button)findViewById(R.id.btn_back);
		title = (TextView)findViewById(R.id.title);
		sps = new SpannableString(titleName);
		sps.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, titleName.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体 0-4字体的长度
		title.setText(sps);
		title.setTextSize(18);
		small_font_ll.setOnClickListener(this);
		medium_font_ll.setOnClickListener(this);
		big_font_ll.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			this.onBackPressed();
			break;
		case R.id.small_font:
			Message msg = new Message();
			msg.what = (int) FontConfig.SMAIL_FONT;
			handler.sendMessage(msg);
			break;
		case R.id.medium_font:
			Message msg1 = new Message();
			msg1.what = (int) FontConfig.NOMAL_FONT;
			handler.sendMessage(msg1);
			break;
		case R.id.big_font:
			Message msg2 = new Message();
			msg2.what = (int) FontConfig.BIG_FONT;
			handler.sendMessage(msg2);
			break;
		}
		
	}
	
	void TipAlertDialog(String title, String message, final Boolean flag) {
		AlertDialog.Builder sure = new AlertDialog.Builder(SettingFont.this);
		LogUtil.p(tag, "handler123");
		sure.setTitle(title)
		.setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (flag) {
							LogUtil.p(tag, "ChangePassword.this.finish()");
							SettingFont.this.finish();
						} else {
							
						}

					}

				});
		sure.create().show();// 少了这个，就没办法显示提示框
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

}
