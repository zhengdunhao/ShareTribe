package com.atm.chatonline.setting.ui;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.CkeckRegisterMessage;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.ClearEditText;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

public class ChangeEmail extends BaseActivity implements OnClickListener{
	
	private String tag = "ChangeEmail";
	private ClearEditText edit_login_pwd,edit_new_email;
	private Button btnBack, btnSure;
	
	private TextView title;
	private SpannableString sps = null;
	private String titleName ="换绑邮箱";

	//发送参数给服务器
	private String cookie;
	private String changeEmailRelativePath;
	
	private String pwd,loginPwd,newEmail,userID;
	
	//服务器反应
	private String response;
	
	private BBSConnectNet bBSConnectNet;
	
	private Handler handler;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_email_view);
		initUI();
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.SUCCESS){
					TipAlertDialog("提示","修改成功",true);
				}else if(msg.what==Config.USED){
					TipAlertDialog("提示","该邮箱已被占用",false);
				}else if(msg.what==Config.FAILED){
					TipAlertDialog("提示","邮箱修改失败，请稍后重试",false);
				}
			}
		};
	}
	
	void initUI(){
		edit_login_pwd = (ClearEditText)findViewById(R.id.edit_login_pwd);
		edit_new_email = (ClearEditText)findViewById(R.id.edit_new_email);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnSure = (Button) findViewById(R.id.btn_sure);
		
		title = (TextView)findViewById(R.id.title);
		sps = new SpannableString(titleName);
		sps.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, titleName.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体 0-4字体的长度
		title.setText(sps);
		title.setTextSize(18);
		btnBack.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		//cookie 和更改密码的链接
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		userID = BaseActivity.getSelf().getUserID();
		changeEmailRelativePath = "user/changeEmail.do";
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			this.onBackPressed();
			break;
		case R.id.btn_sure:
			pwd = BaseActivity.getSelf().getPwd();
			loginPwd = edit_login_pwd.getText().toString();
			if(pwd.equals(loginPwd)){
				newEmail = edit_new_email.getText().toString();
				//用正则表达式验证邮箱
				if(new CkeckRegisterMessage().checkEmail(newEmail)){
					LogUtil.p(tag, "邮箱的格式正确");
					sendEmail();
				}else{
					TipAlertDialog("提示","邮箱格式不对喔",false);
				}
			}else{
				TipAlertDialog("提示","登入密码不正确",false);
			}
			break;
		}
		
	}
	
	void sendEmail(){
		new Thread(sendEmailRunnable).start();
	}
	
	Runnable sendEmailRunnable = new Runnable(){
		public void run(){
			try{
				bBSConnectNet = new BBSConnectNet(changeEmailRelativePath,cookie,userID,newEmail,Config.CHANGE_EMAIL);
				response = bBSConnectNet.getResponse();
				LogUtil.p(tag, "response:"+response);
				Message msg = new Message();
				if(response.equals(Config.SUCCESS+"")){
					msg.what = Config.SUCCESS;
					handler.sendMessage(msg);
				}else if(response.equals(Config.USED+"")){
					msg.what = Config.USED;
					handler.sendMessage(msg);
				}else{
					msg.what = Config.FAILED;
					handler.sendMessage(msg);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	};

	void TipAlertDialog(String title, String message, final Boolean flag) {
		AlertDialog.Builder sure = new AlertDialog.Builder(ChangeEmail.this);
		LogUtil.p(tag, "handler123");
		sure.setTitle(title)
		.setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (flag) {
							LogUtil.p(tag, "ChangeEmail.this.finish()");
							ChangeEmail.this.finish();
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
