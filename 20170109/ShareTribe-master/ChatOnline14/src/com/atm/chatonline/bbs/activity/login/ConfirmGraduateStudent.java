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
import com.atm.chatonline.bbs.util.SendRegisterStudent;
import com.example.studentsystem01.R;

public class ConfirmGraduateStudent extends Activity implements OnClickListener{
	private String tag = "ConfigGraduateStudent";
	private String stuClass,stuNum,stuName,enterSchoolTime;
	private String userName,pwd;
	private EditText editClass,editNum,editName;
	private Button btnSure,btnBack;
	private String respCode;
	private Handler handler;
	private TextView title;
	
	IsNetworkAvailable conNetwork;
	private SendRegisterStudent sendRegisterStudent;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirm_graduate_student);	
		initUI();
		conNetwork=new IsNetworkAvailable();
		if(!conNetwork.isNetworkAvailable(ConfirmGraduateStudent.this)){
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		pwd = intent.getStringExtra("pwd");
		enterSchoolTime = intent.getStringExtra("enterSchoolTime");
		handler = new Handler(){
			public void handlerMessage(Message msg){
				switch(msg.what){
				case 1:
					Log.i(tag, "ConfirmGraduateStudent-msg.1");
					reRirectTo();
					break;
				case 2:
					new ToastUtil().show(getApplication(),"身份验证有误！");
					break;
				}
			}
		};
		btnSure.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	void initUI(){
		btnSure = (Button)findViewById(R.id.btn_register);
		btnBack = (Button)findViewById(R.id.btn_back);
		editClass = (EditText)findViewById(R.id.edit_student_class);
		editNum = (EditText)findViewById(R.id.edit_student_num);
		editName = (EditText)findViewById(R.id.edit_student_name);
		title = (TextView)findViewById(R.id.title);
		title.setText("毕业生身份认证");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(!conNetwork.isNetworkAvailable(ConfirmGraduateStudent.this)){
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}else{
			stuClass = editClass.getText().toString();
			stuNum = editNum.getText().toString();
			stuName = editName.getText().toString();
			sendRegisterStudent = new SendRegisterStudent(Config.GRADUATE_STUDENT,userName,stuClass,stuNum,stuName,enterSchoolTime);
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
				respCode = sendRegisterStudent.checkGraduateStu();
				Log.i(tag, "runnableSure--respCode:"+respCode);
				if(respCode.equals("success")){
					Log.i(tag, "success:");
					msg.what = 1;
					handler.sendMessage(msg);
					reRirectTo();
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
				sendRegisterStudent.reqExitConfig();
				ConfirmGraduateStudent.this.onBackPressed();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	void reRirectTo(){
		Log.i(tag, "reRirectTo-ConfirmGraduateStudent");
		Intent intent2=new Intent(this,LoginView.class);
		intent2.putExtra("login", com.atm.chatonline.chat.util.Config.LOGIN_AFTER_REGISTER);
//		intent2.putExtra("userId", userName);
//		intent2.putExtra("pwd", pwd);
		Log.i(tag, "向LoginView传递了userId:"+userName+"、pwd："+pwd);
		startActivity(intent2);
		finish();
	}
}
