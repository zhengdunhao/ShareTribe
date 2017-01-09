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
import com.atm.chatonline.bbs.commom.UserConfirm;
import com.atm.chatonline.bbs.util.SendRegisterStudent;
import com.example.studentsystem01.R;

public class ConfirmInternalStudent extends Activity implements OnClickListener{
	private String tag = "ConfigInternalStudent";
	private Button btnBack,btnSure;
	private String stuNum,stuSchoolPwd,userName,pwd;
	private String respCode;
	private EditText editStuNum,editStuSchoolPwd;
	private Handler handler;
	IsNetworkAvailable conNetwork;
	private SendRegisterStudent sendRegisterStudent;
	UserConfirm userConfirm;
	private TextView title;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirm_internal_student);
		initUI();
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		pwd = intent.getStringExtra("pwd");
		conNetwork=new IsNetworkAvailable();
		if(!conNetwork.isNetworkAvailable(ConfirmInternalStudent.this)){
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}
		
		userConfirm = new UserConfirm();
		
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
				case 3:
					new ToastUtil().show(getApplication(),"账号已被使用！");
					break;
				}
			}
		};
	}
	void initUI(){
		btnBack = (Button)findViewById(R.id.btn_back);
		btnSure = (Button)findViewById(R.id.btn_register);
		editStuNum = (EditText)findViewById(R.id.edit_student_num);
		editStuSchoolPwd = (EditText)findViewById(R.id.edit_student_pwd);
		title = (TextView)findViewById(R.id.title);
		title.setText("在校生身份认证");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(!conNetwork.isNetworkAvailable(ConfirmInternalStudent.this)){
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}else{
			stuNum = editStuNum.getText().toString();
			stuSchoolPwd = editStuSchoolPwd.getText().toString();
			sendRegisterStudent = new SendRegisterStudent(Config.INTERNAL_STUDENT,userName,stuNum,stuSchoolPwd);
			switch(v.getId()){
			case R.id.btn_register:
				new Thread(runnableSure).start();
				break;
			case R.id.btn_back:
				
				new Thread(runnableBack).start();
				ConfirmInternalStudent.this.onBackPressed();
				break;
			}
		}
		
	}
	
	
	Runnable runnableSure = new Runnable(){
		public void run(){
			Message msg=new Message();
			try {
				respCode = sendRegisterStudent.checkInternalStu();
				Log.i(tag, "runnableSure--respCode:"+respCode);
				if(respCode.equals("success")){
					msg.what = 1;
					handler.sendMessage(msg);
				}else if(respCode.equals("failed")){
					msg.what = 2;
					handler.sendMessage(msg);
				}else if(respCode.equals("used")){
					msg.what = 3;
					handler.sendMessage(msg);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			userConfirm.userConfirm(stuNum, stuSchoolPwd, "学生");
//			msg.what = 1;
//			handler.sendMessage(msg);
		}
	};
	
	Runnable runnableBack = new Runnable(){
		public void run(){
			
			try {
				sendRegisterStudent.reqExitConfig();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	
	public void reRirectTo()//转向主界面
	{
		Intent intent2=new Intent(this,LoginView.class);
		intent2.putExtra("login", com.atm.chatonline.chat.util.Config.LOGIN_AFTER_REGISTER);
//		intent2.putExtra("userId", userName);
//		intent2.putExtra("pwd", pwd);
		Log.i(tag, "向LoginView传递了userId:"+userName+"、pwd："+pwd);
		startActivity(intent2);
		finish();
	}
}
