package com.atm.chatonline.setting.ui;

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

import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;
/**
 * Author:ZDH
 * Content:点击账户安全后跳转的界面，内容有更换密码，换绑邮箱
 * */
public class AccountSafe extends BaseActivity implements OnClickListener{
	
	private Button btnBack;
	private LinearLayout changePwd_ll,changeEmail_ll;
	private TextView title;
	private SpannableString sps = null;
	private String titleName ="账户安全";
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_safe_view);
		initUI();
	}

	void initUI(){
		btnBack = (Button)findViewById(R.id.btn_back);
		changePwd_ll = (LinearLayout)findViewById(R.id.chang_pwd);
		changeEmail_ll = (LinearLayout)findViewById(R.id.change_email);
		title = (TextView)findViewById(R.id.title);
		sps = new SpannableString(titleName);
		sps.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, titleName.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体 0-4字体的长度
		title.setText(sps);
		title.setTextSize(18);
		btnBack.setOnClickListener(this);
		changePwd_ll.setOnClickListener(this);
		changeEmail_ll.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			this.onBackPressed();
			break;
		case R.id.chang_pwd:
			Intent intent = new Intent(AccountSafe.this,ChangePassword.class);
			startActivity(intent);
			break;
		case R.id.change_email:
			Intent intent1 = new Intent(AccountSafe.this,ChangeEmail.class);
			startActivity(intent1);
			break;
		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

}
