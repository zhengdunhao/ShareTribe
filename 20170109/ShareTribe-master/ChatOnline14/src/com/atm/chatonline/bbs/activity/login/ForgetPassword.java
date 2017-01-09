package com.atm.chatonline.bbs.activity.login;
/**
 * @author Jackbing
 * 找回密码
 */
import org.json.JSONException;
import org.json.JSONObject;

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
import com.atm.chatonline.bbs.util.SendErrorLoginMsg;
import com.example.studentsystem01.R;

public class ForgetPassword extends Activity implements OnClickListener{
	private EditText forgetusername;
	private Button forgetsubmit;
	private TextView textviewappeal,title;
	private String sourceStr,tag="ForgetPassword",respCode="",confirmNumber,userId;
	private IsNetworkAvailable conNetwork=null;//判断是否有网络连接
	private MyToast toast;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		Log.i(tag, ">>>>");
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
					toUpatePassword();
					break;
				case 2:
					showToast(respCode);
					break;
				}
			}
			
		};
	}
	private void initView() {
		forgetusername=(EditText)findViewById(R.id.edit_forget_pwd);
		forgetsubmit=(Button)findViewById(R.id.btn_forget_submit);
		textviewappeal=(TextView)findViewById(R.id.text_forget_appeal);
		title = (TextView)findViewById(R.id.title);//2017.7.22
		title.setText("找回密码");
		title.setTextSize(18);
		Button btnBack=(Button)findViewById(R.id.btn_back);
		forgetsubmit.setOnClickListener(this);
		textviewappeal.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.text_forget_appeal:
			if(isNetworkAvailable()){
				Log.i(tag, "申诉连接被点击");
				forgetAppeal();
			}else{
			showToast("当前没有可用网络！");
			}
			break;
		case R.id.btn_forget_submit:
			if(isNetworkAvailable()){
			Log.i(tag, "确认按钮被点击");
			forgetPassword();
			}else{
				showToast("当前没有可用网络！");
			}
			break;
		case R.id.btn_back:
			onBackPressed();
			break;
		}
		
	}
	
	
	private void forgetPassword() {
		sourceStr=forgetusername.getText().toString().trim();
		Log.i(tag, "username ="+sourceStr);
		if(sourceStr.equals("")){
			showToast("请输入用户名或绑定的邮箱");
		}else{
			 new Thread(runnable).start();
		}
	}
	
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			SendErrorLoginMsg sendErrorLogin=new SendErrorLoginMsg();
			JSONObject obj=sendErrorLogin.sendUserName(sourceStr);
			try {
				if(obj.getString("tip").equals("success")){//邮箱存在，已发送验证码到您的邮箱
					respCode="请到您的邮箱获取验证码！";
					confirmNumber=obj.getString("captchas");
					userId=obj.getString("userId");
					Log.i(tag, "confirmNumber ="+confirmNumber);
					Log.i(tag, "confirmNumber ="+userId);
					handler.sendEmptyMessage(1);//避免创建没必要的message对象，直接用sendemptymsg
					return;							//1代表用户存在并跳转到更改密码界面，2表示仅仅给出提示
				}else if(obj.getString("tip").equals("unRegister")){//用户名或邮箱未被注册
					respCode="用户名或邮箱未被注册";
				}else if(obj.getString("tip").equals("failed")){
					respCode="用户名或邮箱填写错误！";
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			handler.sendEmptyMessage(2);
		}
		
	};
	
	/**
	 * 判断是否有网络连接
	 * @return
	 */
	private boolean isNetworkAvailable() {
		if(conNetwork==null){
			conNetwork=new IsNetworkAvailable();
		}
		return conNetwork.isNetworkAvailable(ForgetPassword.this);
	}
	/**
	 * 跳转到更改密码界面
	 */
	public void toUpatePassword(){
		Intent intent=new Intent(this,UpDatePassword.class);
		intent.putExtra("confirmNumber", confirmNumber);
		intent.putExtra("userId", userId);
		startActivity(intent);
		
	}
	/**
	 * 跳转到申诉界面
	 */
	private void forgetAppeal() {
		
		startActivity(new Intent(this,UserAppeal.class));
		finish();
	}
	
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
