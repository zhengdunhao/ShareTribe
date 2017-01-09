package com.atm.chatonline.bbs.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.Config;
import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.ToastUtil;
import com.atm.chatonline.bbs.util.SendRegisterTeacher;
import com.example.studentsystem01.R;

public class ConfirmTeacher extends Activity implements OnClickListener{
	private String tag = "ConfirmTeacher";
	private String teaNum,teaSchoolPwd,userName,pwd;
	private String respCode;
	private Button btnBack,btnSure;
	private EditText editTeaNum,editTeaSchoolPwd;
	private Handler handler;
	IsNetworkAvailable conNetwork;
	private SendRegisterTeacher sendRegisterTeacher;
	private TextView title;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirm_teacher);	
		initUI();
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		pwd = intent.getStringExtra("pwd");
		conNetwork=new IsNetworkAvailable();
		if(!conNetwork.isNetworkAvailable(ConfirmTeacher.this)){
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}
		btnBack.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
				case 1:
					reRirectTo();
					break;
				case 2:
					new ToastUtil().show(getApplication(),"身份验证有误！");
					break;
				}
			}
		};
	}
	
	void initUI(){
		btnBack = (Button)findViewById(R.id.btn_back);
		btnSure = (Button)findViewById(R.id.btn_register);
		editTeaNum = (EditText)findViewById(R.id.edit_teacher_num);
		editTeaSchoolPwd = (EditText)findViewById(R.id.edit_teacher_pwd);
		title = (TextView)findViewById(R.id.title);
		title.setText("教师身份认证");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(!conNetwork.isNetworkAvailable(ConfirmTeacher.this)){
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}else{
			teaNum = editTeaNum.getText().toString();
			teaSchoolPwd = editTeaSchoolPwd.getText().toString();
			sendRegisterTeacher = new SendRegisterTeacher(Config.TEACHER,userName,teaNum,teaSchoolPwd);
			switch(v.getId()){
			case R.id.btn_register:
				new Thread(runnableSure).start();
				break;
			case R.id.btn_back:
				new Thread(runnableBack).start();
				break;
			}
		}
	}
	
	Runnable runnableSure = new Runnable(){
		public void run(){
			Message msg=new Message();
			try {
				respCode = sendRegisterTeacher.checkTeacher();
				Log.i(tag, "runnableSure--respCode:"+respCode);
				if(respCode.equals("success")){
					msg.what = 1;
					handler.sendMessage(msg);
				}else{
					msg.what = 2;
					handler.sendMessage(msg);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	
	Runnable runnableBack = new Runnable(){
		public void run(){
			
			try {
				sendRegisterTeacher.reqExitConfig();
				ConfirmTeacher.this.onBackPressed();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	
	public void reRirectTo()//转向主界面
	{
		Intent intent2=new Intent(this,LoginView.class);
//		intent2.putExtra("userId", userName);
//		intent2.putExtra("pwd", pwd);
		Log.i(tag, "向LoginView传递了userId:"+userName+"、pwd："+pwd);
		startActivity(intent2);
		finish();
	}
}
