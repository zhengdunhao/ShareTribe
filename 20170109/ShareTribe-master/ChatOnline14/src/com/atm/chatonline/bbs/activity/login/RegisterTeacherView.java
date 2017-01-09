package com.atm.chatonline.bbs.activity.login;

/**
 * 教师注册界面，用于输入教师注册的基本信息，并将消息传递给处理类
 * 2015.7.24--李
 * 
 * 修改了 只有在有网络的前提下，才可以注册账号，每当打开教师注册界面的时候，会判断当前是否有可用的网络
 * 2015-7-28-郑
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.Config;
import com.atm.chatonline.bbs.commom.CountDownUtil;
import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.ToastUtil;
import com.atm.chatonline.bbs.util.CkeckRegisterMessage;
import com.atm.chatonline.bbs.util.SendRegisterTeacher;
import com.example.studentsystem01.R;

public class RegisterTeacherView extends Activity implements OnClickListener,OnTouchListener {
	private CheckBox chkAgree;
	private String userName, pwd, comfirmPwd, userSchool, userDept, userEmail;
	private String emailResponse;
	private String respCode;
	private String emailNum, comfirmNumber;
	private Spinner spSchool, spDept;
	private int log_school,log_dept;
	private Button btnRegister, btnNext,btnBack01,btnBack02;
	private EditText txtName, txtPwd, txtComPwd, txtEmail, txtNumber;
	private Handler handler;
	private Button btnEmail;
	private TextView tvAgree, title, title02;
	private SendRegisterTeacher sendRegisterTeacher;
	private String tag = "RegisterTeacherView";
	IsNetworkAvailable conNetwork;
	private InputMethodManager mInputMethodManager;
	private RelativeLayout rl_register_teacher_view;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_teacher_view);
		initUI();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					reRirectTo();
					break;
				case 2:
					new ToastUtil().show(getApplication(), "信息填写不完整或有误");
					break;
				case 3:
					new ToastUtil().show(getApplication(), respCode);
					break;
				case 4:
					new ToastUtil().show(getApplication(), "验证码填写有误！");
					break;
				case 5:
					new ToastUtil().show(getApplication(), "账号已经被占用！");
					break;
				case 6:
					CountDownUtil countDown = new CountDownUtil(btnEmail);
					countDown.action();
					new ToastUtil().show(getApplication(), "验证码已发送，请登录邮箱获取验证码");
					break;

				case 7:
					new ToastUtil().show(getApplication(), "邮箱已被用");
					break;
				case 8:
					new ToastUtil().show(getApplication(), "邮箱不可用");
					break;
				default:
					break;
				}
			}
		};

		/**
		 * 修改了 只有在有网络的前提下，才可以注册账号，每当打开教师注册界面的时候，会判断当前是否有可用的网络 2015-7-28-郑
		 */
		conNetwork = new IsNetworkAvailable();
		if (!conNetwork.isNetworkAvailable(RegisterTeacherView.this)) {
			Toast.makeText(getApplicationContext(), "当前没有可用网络！",
					Toast.LENGTH_LONG).show();
		}

	}

	void initUI() {
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		rl_register_teacher_view = (RelativeLayout)findViewById(R.id.rl_register_teacher_view);
		spSchool = (Spinner) findViewById(R.id.spi_school_t);
		spDept = (Spinner) findViewById(R.id.spi_department_t);
		btnNext = (Button) findViewById(R.id.btn_next);
		btnBack01 = (Button) findViewById(R.id.btn_back);
		txtName = (EditText) findViewById(R.id.txt_account_t);
		txtPwd = (EditText) findViewById(R.id.txt_password_t);
		txtComPwd = (EditText) findViewById(R.id.txt_rep_password_t);
		title = (TextView) findViewById(R.id.title);
		title.setText("教师注册通道");
		title.setTextSize(18);
		btnNext.setOnClickListener(this);
		btnBack01.setOnClickListener(this);
		rl_register_teacher_view.setOnTouchListener(this);
		spSchool.setOnTouchListener(this);
		spDept.setOnTouchListener(this);
	}

	public void onClick(View v) {
		/**
		 * 修改了 在没有网络的情况下，若点击注册按钮，则显示 当前没有可用网络 2015-7-28-郑
		 */
		if (!conNetwork.isNetworkAvailable(RegisterTeacherView.this)) {
			Toast.makeText(getApplicationContext(), "当前没有可用网络！",
					Toast.LENGTH_LONG).show();
		} else {
			switch (v.getId()) {
			case R.id.btn_next:
				userName = txtName.getText().toString();
				pwd = txtPwd.getText().toString();
				comfirmPwd = txtComPwd.getText().toString();
				userSchool = spSchool.getSelectedItem().toString();
				userDept = spDept.getSelectedItem().toString();
				log_school = spSchool.getSelectedItemPosition();
				log_dept = spDept.getSelectedItemPosition();
				if (!userName.equals("") && !pwd.equals("")
						&& !comfirmPwd.equals("") && pwd.equals(comfirmPwd)) {
					nextView();
				}else{
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
				break;
			case R.id.btn_back:
				this.onBackPressed();
			
				break;
			}
		}
	}

	void nextView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.register_teacher_view02, null);
		btnRegister = (Button) v.findViewById(R.id.btn_register);
		btnEmail = (Button) v.findViewById(R.id.btn_email_t);
		btnBack02 = (Button) v.findViewById(R.id.btn_back);
		tvAgree = (TextView) v.findViewById(R.id.tv_agree_t);
		chkAgree = (CheckBox) v.findViewById(R.id.chk_agree_t);
		txtEmail = (EditText) v.findViewById(R.id.txt_email_t);
		txtNumber = (EditText) v.findViewById(R.id.txt_ver_num_t);
		title02 = (TextView) v.findViewById(R.id.title);
		title02.setText("邮箱验证");
		title02.setTextSize(18);
		btnBack02.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				setContentView(R.layout.register_teacher_view);
				initUI();
				txtName.setText(userName);
				txtPwd.setText(pwd);
				txtComPwd.setText(comfirmPwd);
				spSchool.setSelection(log_school);
				spDept.setSelection(log_dept);
				
			}
		});
		chkAgree.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btnRegister.setClickable(true);
			}
		});

		tvAgree.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tvAgree.setTextColor(android.graphics.Color.BLUE);
				Intent intent = new Intent(RegisterTeacherView.this,
						RegisterAgreementView.class);
				startActivity(intent);
			}
		});

		btnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userEmail=txtEmail.getText().toString();
				comfirmNumber=txtNumber.getText().toString();
				new Thread(runnable).start();
			}
		});
		
		

		btnEmail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userEmail = txtEmail.getText().toString();

				// 发送邮箱字符创到服务器
				if (txtEmail.getText().toString().equals(""))// 没有输入邮箱
				{
					new ToastUtil().show(getApplication(), "请输入如邮箱");
				} else// 有输入邮箱则调用checkResiterMessage检查邮箱的正确性
				{
					Log.i(tag, "邮箱有东西");

					if (new CkeckRegisterMessage().checkEmail(userEmail))// 检查合乎规则则发送给服务器，
					{
						Log.i(tag, "邮箱符合规则");
						// 倒计时 ---------------------修改2015.9.9-郑

						// 发送邮箱到服务器
						new Thread(emailRunnable).start();

					} else {
						Log.i(tag, "邮箱不符合规则");
						// 邮箱不和规则
						new ToastUtil().show(getApplication(), "请填写一个规范的邮箱");
					}
				}
			}
		});

		setContentView(v);
	}

	Runnable emailRunnable = new Runnable() {
		public void run() {
			CountDownUtil countDown = new CountDownUtil(btnEmail);
			countDown.action();
			sendRegisterTeacher = new SendRegisterTeacher(userEmail);
			emailResponse = sendRegisterTeacher.checkEmail();// 已经把邮箱发送给服务器，服务器也获取邮箱----------------------修改2015.9.9-郑
			Message msg = new Message();
			Log.i(tag, "emailResponse:" + emailResponse);
			if (emailResponse.equals("success")) {
				// 走到这里，说明邮箱格式正确而且没有被用过，所以才可以倒计时

				msg.what = 6;
				handler.sendMessage(msg);
				Log.i(tag, "countDown按钮进入倒计时");
				emailNum = sendRegisterTeacher.getChangeJson().getEmailNum();
				Log.i(tag, "从服务器获取的邮箱验证码是" + emailNum);

			} else if (emailResponse.equals("alreadyExit")) {
				msg.what = 7;
				handler.sendMessage(msg);

			} else if (Integer.parseInt(emailResponse) == 201) {
				msg.what = 8;
				handler.sendMessage(msg);

			}
		}
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = new Message();

			userEmail = txtEmail.getText().toString();
			
				try {
					Log.i(tag, "comfirmNumber:" + comfirmNumber + "、emailNum:"
							+ emailNum);
					if (!comfirmNumber.equals(emailNum)) {
						msg.what = 4;
						handler.sendMessage(msg);
						Log.i(tag, "验证码有错");
					} else {
						respCode = new SendRegisterTeacher(Config.TEACHER,
								userName, pwd, userSchool, userDept, userEmail)
								.checkRegister();
						if (respCode.equals("注册成功")) {
							// 注册成功
							msg.what = 1;
							handler.sendMessage(msg);
						} else if (respCode.equals("used")) {
							msg.what = 5;
							handler.sendMessage(msg);
							Log.i(tag, "账号被占用");
						} else {
							// 由于服务器原因，注册失败
							msg.what = 3;
							handler.sendMessage(msg);
						}
					}
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			
		}
	};

	public void reRirectTo()// 转向主界面
	{
		Intent intent2 = new Intent(this, ConfirmTeacher.class);
		intent2.putExtra("userName", userName);
		intent2.putExtra("pwd", pwd);
		Log.i(tag, "向ConfirmTeacher传递了userID:" + userName + "、pwd：" + pwd);
		startActivity(intent2);
		finish();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_register_teacher_view:
			mInputMethodManager.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
			break;
		case R.id.spi_school_t:
			mInputMethodManager.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
			break;
		case R.id.spi_department_t:
			mInputMethodManager.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
			break;
		default:
			break;
		}
		return false;
	}
}
