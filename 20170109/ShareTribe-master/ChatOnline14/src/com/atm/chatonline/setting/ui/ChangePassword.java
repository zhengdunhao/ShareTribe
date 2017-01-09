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
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.ClearEditText;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

/**
 * Author:ZDH Content:修改密码的界面，修改的内容有：旧密码，新密码
 * */
public class ChangePassword extends BaseActivity implements OnClickListener {

	private String tag = "ChangePassword";
	private ClearEditText edit_original_pwd, edit_new_pwd, edit_confirm_pwd;
	private Button btnBack, btnSure;
	
	private TextView title;
	private SpannableString sps = null;
	private String titleName ="更换密码";

	private String oldPwd, newPwd1, newPwd2;
	private String pwd;// 从系统那里获取保存的密码
	private String userID;
	
	//发送参数给服务器
	private String cookie;
	private String changePwdRelativePath;
	
	//服务器反应
	private String response;
	
	private BBSConnectNet bBSConnectNet;
	
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_pwd_view);
		initUI();
		
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.SUCCESS){
					TipAlertDialog("提示","修改成功",true);
					//把系统保存的密码改过来
					setPreference(userID,newPwd1);
					LogUtil.p(tag, "检查：pwd:"+BaseActivity.getSelf().getPwd());
				}else if(msg.what==Config.FAILED){
					TipAlertDialog("提示","网络出现异常",false);
				}
			}
		};
		
	}

	void initUI() {
		edit_original_pwd = (ClearEditText) findViewById(R.id.edit_original_pwd);
		edit_new_pwd = (ClearEditText) findViewById(R.id.edit_new_pwd);
		edit_confirm_pwd = (ClearEditText) findViewById(R.id.edit_confirm_pwd);
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
		changePwdRelativePath =  "user/changePassword.do";
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			this.onBackPressed();
			break;
		case R.id.btn_sure:
			oldPwd = edit_original_pwd.getText().toString();
			newPwd1 = edit_new_pwd.getText().toString();
			newPwd2 = edit_confirm_pwd.getText().toString();
			pwd = BaseActivity.getSelf().getPwd();
			userID = BaseActivity.getSelf().getUserID();
			LogUtil.p(tag, "oldPwd:" + oldPwd + ",newPwd1:" + newPwd1
					+ ",newPwd2:" + newPwd2 + ",pwd:" + pwd);
			if (oldPwd.equals(pwd)) {
				LogUtil.p(tag, "原始密码正确");// 先验证原始密码
				if (newPwd1.equals(newPwd2)) {// 后验证新密码
					LogUtil.p(tag, "新密码一致");
					//发送新密码到服务器
					sendPwd();
				} else {
					LogUtil.p(tag, "新密码不一致");
					TipAlertDialog("提示","新密码不一致",false);
				}
			} else {
				LogUtil.p(tag, "原始密码不正确");
				TipAlertDialog("提示","原始密码有误",false);
			}

			break;
		}
	}

	void TipAlertDialog(String title, String message, final Boolean flag) {
		AlertDialog.Builder sure = new AlertDialog.Builder(ChangePassword.this);
		LogUtil.p(tag, "handler123");
		sure.setTitle(title)
		.setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (flag) {
							LogUtil.p(tag, "ChangePassword.this.finish()");
							ChangePassword.this.finish();
						} else {
							
						}

					}

				});
		sure.create().show();// 少了这个，就没办法显示提示框
	}
	
	void sendPwd(){
		new Thread(sendPwdRunnable).start();
	}
	
	Runnable sendPwdRunnable = new Runnable(){
		public void run(){
			try{
				bBSConnectNet = new BBSConnectNet(changePwdRelativePath,cookie,userID,newPwd1,Config.CHANGE_PWD);
				response = bBSConnectNet.getResponse();
				LogUtil.p(tag, "response:"+response);
				Message msg = new Message();
				if(response.equals(Config.SUCCESS+"")){
					msg.what = Config.SUCCESS;
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

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

}
