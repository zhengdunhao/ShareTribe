package com.atm.chatonline.bbs.activity.login;
/**
 * @author Jackbing
 * 找回用户名
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.MyToast;
import com.atm.chatonline.bbs.util.CkeckRegisterMessage;
import com.atm.chatonline.bbs.util.SendErrorLoginMsg;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

public class FindUsername extends Activity implements OnClickListener{
	private EditText edit_find_username;
	private Button btn_find_submit;
	private TextView text_find_appeal;
	private String respCode,eMail="",tag="findusername";
	private MyToast toast;
	private IsNetworkAvailable conNetwork=null;//判断是否有网络连接
	private Handler handler;
	int i=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_username);
		initView();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				switch(msg.what){
				case 1:
					showToast(respCode);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					redirectToLoginView();
					break;
				case 2:
					showToast(respCode);
					break;
				}
			}
			
		};
		
	}
	private void initView() {
		edit_find_username=(EditText) findViewById(R.id.edit_find_username);
		btn_find_submit=(Button)findViewById(R.id.btn_find_submit);
		text_find_appeal=(TextView)findViewById(R.id.text_find_appeal);
		Button btnBack=(Button)findViewById(R.id.btn_back);
		btn_find_submit.setOnClickListener(this);
		text_find_appeal.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.btn_find_submit:
			if(isNetworkAvailable()){
				Log.i(tag, "你点击了找回账户的邮箱验证，确定按钮");
				findUserName();
			
			}else{
				showToast("当前没有可用网络！");
			}
			break;
		case R.id.text_find_appeal:
			if(isNetworkAvailable()){
				Log.i(tag, "你点击了申述的连接");
				findAppeal();
				
			}else{
				showToast("当前没有可用网络！");
			}
			break;
		case R.id.btn_back:
			Log.i(tag, "返回按钮被点击");
			onBackPressed();
			break;
		}
	}
	
	/**
	 * 判断是否有网络连接
	 * @return
	 */
	private boolean isNetworkAvailable() {
		if(conNetwork==null){
			conNetwork=new IsNetworkAvailable();
		}
		return conNetwork.isNetworkAvailable(FindUsername.this);
	}
	/**
	 * 跳转到申诉界面
	 */
	private void findAppeal() {
		
		startActivity(new Intent(this,UserAppeal.class));
		finish();
	}
	
	/**
	 * 成功找回账号，跳转到登录界面
	 */
	public void redirectToLoginView(){
		Intent intent=new Intent(this,LoginView.class);
		Bundle bundle=new Bundle();
		bundle.putInt("login", Config.FIRSTLOGIN);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	private void findUserName() {
		eMail=edit_find_username.getText().toString().trim();
		Log.i(tag,"email ="+eMail);
		//验证邮箱是否规范
		if(new CkeckRegisterMessage().checkEmail(eMail)){
			
			Thread thread=new Thread(runnable);
			thread.start();
		}else{
			showToast("请填写一个规范的邮箱");
		}
	}
	
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			SendErrorLoginMsg sendErrorLogin=new SendErrorLoginMsg();
			String tips=sendErrorLogin.sendEmailMsg(eMail);
			//判断respcdoe情况
			if(tips.equals("success")){//查找成功
				respCode="请登录您的邮箱获取您的账户名";
				handler.sendEmptyMessage(1);//为了避免创建多一个message对象，直接发送emptymsg来代替，1代表成功并跳转
				return;						//2代表只显示respCode而不跳转
			}else if(tips.equals("error")){//不规范
				respCode="服务器无响应";
			}else if(tips.equals("failed")){//未被注册
				respCode="该邮箱未被注册";
			}
			handler.sendEmptyMessage(2);
		}
		
	};
	public void showToast(String s)
		{
			if(toast==null)
			{
				toast=MyToast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
			}else{
				toast.setText(s);toast.setDuration(Toast.LENGTH_SHORT);
			}
			toast.show();
		}
		//cancel掉toast
		public void cancelToast()
		{
			if(toast!=null){
				toast.cancel();
			}
		}
		
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			cancelToast();
 			Log.i(">>>>>>", "退出");
 			finish();
 		}
	
}
