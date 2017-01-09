package com.atm.chatonline.bbs.activity.login;
/**
 *
 * 2015.7.21,
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.MyToast;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SendLoginInfo;
import com.atm.chatonline.chat.info.User;
import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.ClearEditText;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;
 
@SuppressLint("NewApi")
public class LoginView extends BaseActivity implements OnClickListener,OnTouchListener{
	String tag="LoginView";
	private Button btnLogin;
	private String username,pwd;
	private ClearEditText t1,t2;
	private TextView register,loginError;
	private Handler handler;
	private String respCode;
	private MyToast toast;
	private PopupWindow popup;
	private SendLoginInfo sendLoginInfo;
	SharedPreferences preferences;
	IsNetworkAvailable conNetwork;
	private boolean flag=true;
	private int login=Config.AUTOLOGIN;
	private User user;
	private ProgressDialog progressDialog;
	private InputMethodManager mInputMethodManager;
	private RelativeLayout parent;
	final float radius=25.f;

	 private Context context=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_view);
        context=getApplicationContext();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        parent = (RelativeLayout)findViewById(R.id.parent);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        t1=(ClearEditText) findViewById(R.id.edit_account);
        t2=(ClearEditText) findViewById(R.id.edit_password);
        register = (TextView) findViewById(R.id.register);
        loginError=(TextView) findViewById(R.id.login_error);
        conNetwork=new IsNetworkAvailable();
        parent.setOnTouchListener(this);
        btnLogin.setOnClickListener(this);
        register.setOnClickListener(this);
        loginError.setOnClickListener(this);
        initPopupWindow();
        initData();
        user=getPreference();

        preferences = getSharedPreferences("count",MODE_PRIVATE);
        
        int count = preferences.getInt("count", 0);
        Editor editor = preferences.edit();

        editor.putInt("count", ++count);
  
        editor.commit();
	
        if(!conNetwork.isNetworkAvailable(LoginView.this)){
			String tag=null;
			Log.i(tag, "1232132133");
			Toast.makeText(getApplicationContext(), "无法连接到网络", Toast.LENGTH_LONG).show();
		}
        
      
        if(!user.getPwd().equals("")&&!user.getUserID().equals("")&&login!=Config.LOGIN_AFTER_REGISTER){
        	if(login==Config.AUTOLOGIN){
        		Log.i("********>>>", "userId="+user.getUserID()+",pwd="+user.getPwd());
        		autoRedirectTo();
        		Log.i(tag, "autoRedirectTo");
        	}else if(login==Config.BE_OFF_LOGIN){
        		t1.setText(user.getUserID());
        	}
        }
      
        
        handler=new Handler(){
        	public void handleMessage(Message msg)
        	{
        		switch(msg.what)
        		{
        		case 1:
        			Log.i(tag, "case 1");
        			btnLogin.setClickable(true);
        			showToast("用户名或密码错误");
        			break;
        		case 2:
        			redirectTo();	
        			break;
        		case 3:
        			String tag=null;
        			flag=false;
        			showToast("无法连接到网络");
        			break;
        		default :
        				break;
        		}
        	}
        };
        }catch(Exception e){
        	e.printStackTrace();
        }
       }
        
  
    private void initData() {
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		if(bundle!=null){
		login=bundle.getInt("login");
		}
	}


	public void onClick(View v){

    	if(!flag){
    		Message msg=new Message();
    		msg.what=3;
			handler.sendMessage(msg);
		}
		else{
		String tag=null;
		switch(v.getId())
		{
		case R.id.btnLogin:	
			login(t1.getText().toString(),t2.getText().toString());
			BaseActivity.getSelf().setUserID(t1.getText().toString());
			BaseActivity.getSelf().setPwd(t2.getText().toString());
			btnLogin.setClickable(false);
			break;
		case R.id.register:	
			cancelToast();
			Intent intent=new Intent(LoginView.this,RegisterChooseView.class);
			startActivity(intent);
			break;
			
		case R.id.login_error:
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			Log.i(tag, "mInputMethodManager.showSoftInput");
			popup.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
			default:
				break;
		}
		}
    }
     
    
    public void initPopupWindow()
    {
    	try{
    	View v=getLayoutInflater().inflate(R.layout.login_error_choose_view, null);
    	popup=new PopupWindow(v,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(-0000);
       
        popup.setBackgroundDrawable(cd);
        popup.setAnimationStyle(R.style.popup_anim_style);
        
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setHeight(340);
        v.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
        	
        });
        v.findViewById(R.id.btn_find_username).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popup.dismiss();
				Intent intent=new Intent(LoginView.this,FindUsername.class);
				startActivity(intent);
			}
        	
        });
        v.findViewById(R.id.btn_forget_password).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popup.dismiss();
				Intent intent=new Intent(LoginView.this,ForgetPassword.class);
				startActivity(intent);
			}
        	
        });
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void login(String userId,String pwd){
    	username=userId;
		this.pwd=pwd;
		Thread thread = new Thread(runnable);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void autoRedirectTo(){
    	try{
       	cancelToast();
    	BaseActivity.getSelf().setUserID(user.getUserID());
    	setPreference(user.getUserID(),user.getPwd());
    	Thread thread = new Thread(autoLoginRunnable);
    	thread.start();
    	Thread thread2=new Thread(httpLogin);
		thread2.start();
    	
    	Intent intent=new Intent(this,BBSMainView.class);
    	Bundle bundle=new Bundle();
    	bundle.putInt("login", login);
    	intent.putExtras(intent);
    	startActivity(intent);
    	finish();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    
    public void redirectTo()
    {
    	try{
    	cancelToast();
    	Intent intent=new Intent(this,BBSMainView.class);
    	Bundle bundle=new Bundle();
    	bundle.putInt("login", login);
    	intent.putExtras(intent);
    	startActivity(intent);
    	btnLogin.setClickable(true);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    Runnable runnable=new Runnable(){

		@Override
		public void run() {
			Message msg=new Message();
			
			
			if("".equals(username)||"".equals(pwd))
			{
				msg.what=1;
				handler.sendMessage(msg);
				
			} else{
				
				if(con==null){
					BaseActivity.con=Communication.newInstance();
				}else{
					BaseActivity.con.openSocketChannel();
				}
				BaseActivity.con.reqLogin(username,pwd);
		}
	}
   };
   
   Runnable autoLoginRunnable=new Runnable(){

		@Override
		public void run() {
			
			if(con==null){
				BaseActivity.con=Communication.newInstance();
			}else if(!con.newNetWorker01.socketChannel.isRegistered()){
				BaseActivity.con.openSocketChannel();
			}
			con.reqLogin(user.getUserID(),user.getPwd());	
		}
		
	};
   
   Runnable runIsNetWorkAvailable=new Runnable(){
	   public void run(){
		   Message msg=new Message();
			if(!conNetwork.isNetworkAvailable(LoginView.this)){
				msg.what=3;
				handler.sendMessage(msg);
				
			}
	   }
   };
   Runnable httpLogin=new Runnable(){

	@Override
	public void run() {
		setCookie();
	}
	   
   };
   
   
   
   public void setCookie(){
	   Log.i(tag, "setCookie--username:"+user.getUserID());
	   sendLoginInfo = new SendLoginInfo(user.getUserID(),null);	
		try {
			respCode=sendLoginInfo.checkLoginInfo();
			
			if(respCode.equals("success"))
			{
					
					Log.d("setCookie()",sendLoginInfo.getCookie());
			        SharedPreferences.Editor editor1 =  getSharedPreferences("data",Context.MODE_PRIVATE).edit();
			        editor1.putString("cookie", sendLoginInfo.getCookie());
			        editor1.commit();
			        handler.sendEmptyMessage(2);
					
			}else{
				
				handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			handler.sendEmptyMessage(4);
		}
   }
   Runnable runnableGetOfflineMessage = new Runnable(){
	 public void run(){
		 Log.i(tag, "runnableGetOfflineMessage--userID:"+user.getUserID());
		 BaseActivity.con.getOfflineMessage(user.getUserID());
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

 		public void cancelToast()
 		{
 			if(toast!=null){
 				toast.cancel();
 			}
 		}
 		
 		@Override
 		public void onBackPressed(){
 			cancelToast();
 			finish();
 		}
 		

		@Override
		public void processMessage(Message msg) {
			try{
			Message msg1 = new Message();
			if(msg.what==Config.LOGIN_SUCCESS){
				setPreference(user.getUserID(),user.getPwd());
				BaseActivity.getSelf().setUserID(t1.getText().toString());
				BaseActivity.getSelf().setPwd(t2.getText().toString());
				LogUtil.p(tag, "BaseActivity.getSelf().getUserID:"+BaseActivity.getSelf().getUserID());
				new Thread(runnableGetOfflineMessage).start();
				new Thread(httpLogin).start();
			}else if(msg.what==Config.FAILED){
				Log.i(tag, "msg.what=failed");
				msg1.what=1;
				handler.sendMessage(msg1);
			}else if(msg.what==Config.USER_LOGIN_ALREADY){
				showToast("用户已经登录");
			}else if (msg.what == Config.SEND_NOTIFICATION) {
				sendNotifycation();
			}
			}catch(Exception e){
	    		e.printStackTrace();
	    	}
			
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.parent:
				mInputMethodManager.hideSoftInputFromWindow(t1.getWindowToken(), 0);
				break;
			default:
				break;
			}
			return false;
		}
}
