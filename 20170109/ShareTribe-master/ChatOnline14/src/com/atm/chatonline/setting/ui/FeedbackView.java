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

public class FeedbackView extends BaseActivity implements OnClickListener{
	private String tag = "FeedbackView";
	private Button btnBack,btnSure;
	private ClearEditText editFeedback;
	
	private String feedBackMess;
	private String userID;
	
	//发送参数给服务器
	private String cookie;
	private String feedbackRelativePath;
	
	private TextView title;
	private SpannableString sps = null;
	private String titleName ="反  馈";
	
	//服务器反应
	private String response;
		
	private BBSConnectNet bBSConnectNet;
		
	private Handler handler;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_view);
		initUI();
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.SUCCESS){
					TipAlertDialog("成功","您的意见我们已经收到，我们会及时跟进!",true);
				}else if(msg.what==Config.FAILED){
					TipAlertDialog("提示","网络出现异常",false);
				}
			}
		};
	}
	
	void initUI(){
		btnBack = (Button)findViewById(R.id.btn_back);
		btnSure = (Button)findViewById(R.id.btn_sure);
		editFeedback = (ClearEditText)findViewById(R.id.edit_feedback);
		
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
		feedbackRelativePath = "user/feedback.do";
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			this.onBackPressed();
			break;
		case R.id.btn_sure:
			sendFeedback();
			break;
		}
		
	}
	
	void sendFeedback(){
		new Thread(sendFeedbackRunnable).start();
	}
	
	Runnable sendFeedbackRunnable = new Runnable(){
		public void run(){
			try{
				feedBackMess = editFeedback.getText().toString();
				bBSConnectNet = new BBSConnectNet(feedbackRelativePath,cookie,userID,feedBackMess,Config.FEEDBACK_MESSAGE);
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
	
	void TipAlertDialog(String title, String message, final Boolean flag) {
		AlertDialog.Builder sure = new AlertDialog.Builder(FeedbackView.this);
		LogUtil.p(tag, "handler123");
		sure.setTitle(title)
		.setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (flag) {
							LogUtil.p(tag, "ChangeEmail.this.finish()");
							FeedbackView.this.finish();
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
