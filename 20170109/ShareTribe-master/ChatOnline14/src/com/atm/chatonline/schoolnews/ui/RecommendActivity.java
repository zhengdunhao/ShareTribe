package com.atm.chatonline.schoolnews.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SendRecommend;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.setting.ui.SettingView;
import com.example.studentsystem01.R;
/**
 * 这是点击了推荐校友然后跳转到的界面，用户可以填写推荐信息，然后发送到后台
 * */

public class RecommendActivity extends BaseActivity implements OnClickListener{
	private String tag ="RecommendActivity";
	private String name,time,reason;
	private String respCode;
	private Button btnBack;
	private EditText txtName,txtTime,txtReason;
	private TextView btnRecommend;
	private SendRecommend sendRecommend;
	private Handler handler;
	private ProgressDialog progressDialog;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recommend_ui);
		initUI();
		handler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
				case 1:
					LogUtil.p(tag, "handler");
					AlertDialog.Builder sure = new AlertDialog.Builder(RecommendActivity.this);
					LogUtil.p(tag, "handler123");
					sure.setTitle("发送成功")
					.setMessage("我们已经收到您的推荐，感谢您的提供")
					.setPositiveButton("确定",	new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							LogUtil.p(tag, "RecommendActivity.this.finish()");
							RecommendActivity.this.finish();
						}

					});
					sure.create().show();//少了这个，就没办法显示提示框
					LogUtil.p(tag, "RecommendActivity--break");
					break;
				}
			}
		};
	}
	
	void initUI(){
		btnBack = (Button)findViewById(R.id.btn_back);
		txtName = (EditText)findViewById(R.id.txt_name);
		txtTime = (EditText)findViewById(R.id.txt_time);
		txtReason = (EditText)findViewById(R.id.txt_reason);
		btnRecommend = (TextView)findViewById(R.id.btn_recommend);
		btnBack.setOnClickListener(this);
		btnRecommend.setOnClickListener(this);
		progressDialog = new ProgressDialog(RecommendActivity.this);
		progressDialog.setTitle("正在提交，请稍等喔");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_recommend:
			boolean flag=true;//如果姓名，年份，理由三者有任何一个信息为空，则flag为false，不允许发送到后台
			if(txtName.getText().toString().equals("")){
				flag=false;
			}
			if(txtTime.getText().toString().equals("")){
				flag=false;
			}
			if(txtReason.getText().toString().equals("")){
				flag=false;
			}
			if(!flag){
				Toast.makeText(RecommendActivity.this, "信息填写不完整",
						Toast.LENGTH_SHORT).show();
			}else{
				name = txtName.getText().toString();
				time = txtTime.getText().toString();
				reason = txtReason.getText().toString();
				progressDialog.show();
				new Thread(recommendRunnable).start();
			}
			break;
		case R.id.btn_back:
			AlertDialog.Builder back = new AlertDialog.Builder(this);
			back.setTitle("提示框")
					.setMessage("退出当前编辑？")
					.setPositiveButton("退出",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									RecommendActivity.this.finish();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}
							});

			back.create().show();
			
		}
	}
	//按返回键
	@Override
	public void onBackPressed() {
		AlertDialog.Builder back = new AlertDialog.Builder(this);
		back.setTitle("提示框")
				.setMessage("退出当前编辑？")
				.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								RecommendActivity.this.finish();
							}

						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0,
									int arg1) {
								// TODO Auto-generated method stub
							}
						});

		back.create().show();
	}
	//推荐校友发送到后台
	Runnable recommendRunnable = new Runnable(){
		public void run(){
			LogUtil.p(tag, "name:"+name+"、time:"+time+"、reason:"+reason);
			sendRecommend = new SendRecommend(name,time,reason);
			try {
				Message msg=new Message();
				LogUtil.p(tag, "msg可走");
				
				LogUtil.p(tag, "222222222222");
				respCode = sendRecommend.returnRecommend1();
				progressDialog.dismiss();
				LogUtil.p(tag, "3333333333333：：respcode:"+respCode);
				if(respCode.equals("success")){
					msg.what = 1;
					handler.sendMessage(msg);
					LogUtil.p(tag, "44444444444");
				}else{
					LogUtil.p(tag, "55555555555");
					Toast.makeText(RecommendActivity.this, "服务器繁忙",
							Toast.LENGTH_SHORT).show();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
				Toast.makeText(RecommendActivity.this, "服务器繁忙",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	};
	
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

	protected void onResume(){
		super.onResume();
		getAllChildViews(RecommendActivity.this);
	}
}
