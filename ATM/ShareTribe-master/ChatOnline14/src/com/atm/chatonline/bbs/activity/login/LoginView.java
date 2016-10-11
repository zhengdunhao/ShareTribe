package com.atm.chatonline.bbs.activity.login;
/**
 * ¸ÃÀàÓÃÓÚ£¬Éú³ÉµÇÂ½Ò³Ãæ£¬ÏòºóÌ¨´«ÊäµÇÂ¼ÐÅÏ¢
 * 2015.7.21,atm--Àî
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

import com.atm.charonline.bbs.util.LogUtil;
import com.atm.charonline.bbs.util.SendLoginInfo;
import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.MyToast;
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
	SharedPreferences preferences;//´æ´¢Ð¡Êý¾Ý£¬´æ´¢»î¶¯ÔËÐÐµÄ´ÎÊý
	IsNetworkAvailable conNetwork;//ÅÐ¶ÏÊÇ·ñÓÐÍøÂçÁ¬½Ó
	private boolean flag=true;
	private int login=Config.AUTOLOGIN;//´æ´¢intentÀïÃæÐ¯´øµÄÕûÐÍÊý¾Ý£¬3±íÊ¾×Ô¶¯µÇÂ¼£¬4±íÊ¾µÚÒ»´ÎµÇÂ¼£¬5±íÊ¾ÏÂÏßÖ®ºóµÇÂ¼
	private User user;
	private ProgressDialog progressDialog;// ½ø¶ÈÌõ
	private InputMethodManager mInputMethodManager;
	private RelativeLayout parent;

	final float radius=25.f;

	 private Context context=null;
//	private View viewBackground;
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
//        final Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.login_background);
//		bulr(bmp,parent);
        initPopupWindow();
        initData();//³õÊ¼»¯Êý¾Ý
        user=getPreference();//»ñÈ¡userÐÅÏ¢
        //»ñÈ¡SharedPreference¶ÔÏó
        preferences = getSharedPreferences("count",MODE_PRIVATE);
        //»ñÈ¡countÖµ£¬µÚÒ»¸ö²ÎÊýÊÇ¼ü£¬´æÈëÊ±ÓÃÄÄ¸ö¼ü£¬¾ÍÄÄ¸ö¼ü£¬µÚ¶þ¸öÊÇÄ¬ÈÏ²ÎÊý
        int count = preferences.getInt("count", 0);
        //ÅÐ¶Ï³ÌÐòÓëµÚ¼¸´ÎÔËÐÐ£¬Èç¹ûÊÇµÚÒ»´ÎÔËÐÐÔòÌø×ªµ½Òýµ¼Ò³Ãæ
        if (count == 0) {
        	startActivity(new Intent(getApplicationContext(), WelcomeView.class));
            finish();
        }
        
        Editor editor = preferences.edit();
        //´æÈëÊý¾Ý£¬½¡ÃûÎªcount
        editor.putInt("count", ++count);
        //Ìá½»ÐÞ¸Ä
        editor.commit();
        
        
        //Í¨¹ýgerPreference»ñÈ¡£¬ÓÃ»§ÃûºÍÃÜÂë
       

        
//        new Thread(runIsNetWorkAvailable).start();
	
        if(!conNetwork.isNetworkAvailable(LoginView.this)){
			String tag=null;
			Log.i(tag, "1232132133");
			Toast.makeText(getApplicationContext(), "µ±Ç°Ã»ÓÐ¿ÉÓÃÍøÂç£¡", Toast.LENGTH_LONG).show();
		}
        
        //ÒÔÏÂÎªÐÂÔö¼ÓifÓï¾ä£¬ÏÈÅÐ¶ÏÊÇ·ñ±£´æÓÐÃÜÂë£¬ÈôÓÐÔò¿ÉÄÜÎª×Ô¶¯µÇÂ¼»ò±»ÆÈÏÂÏß½øÈëµÇÂ¼½çÃæµÄµÇÂ¼Çé¿ö
        if(!user.getPwd().equals("")&&!user.getUserID().equals("")&&login!=Config.LOGIN_AFTER_REGISTER){
        	if(login==Config.AUTOLOGIN){
        		Log.i("********>>>", "userId="+user.getUserID()+",pwd="+user.getPwd());
        		autoRedirectTo();
        		Log.i(tag, "autoRedirectTo");
        	}else if(login==Config.BE_OFF_LOGIN){
        		t1.setText(user.getUserID());
        	}
        	Log.i(">>>>>","this step is going1");
        }
        Log.i(">>>>>","this step is going2");
        
        handler=new Handler(){
        	public void handleMessage(Message msg)
        	{
        		switch(msg.what)
        		{
        		case 1:
        			Log.i(tag, "µÇÂ¼Ê§°Ü");
        			btnLogin.setClickable(true);//Ê§°ÜµÄÊ±ºò£¬°´Å¥»¹ÄÜÔÚµã»÷2016.7.16
        			showToast("µÇÂ¼Ê§°Ü");
        			break;
        		case 2:
        			Log.i(tag, "msg.what=2,×ßredirectTo£¨£©");
        			redirectTo();	
        			/*if(islogin){
        				Log.i(tag,"isLogin1 is "+islogin);
        				redirectTo();
        			}else{
        				
        			islogin=true;
        			Log.i(tag,"isLogin2 is "+islogin);
        			}*/
        			break;
        		case 3:
        			String tag=null;
        			Log.i(tag, "22222222222");
        			flag=false;
        			showToast("µ±Ç°Ã»ÓÐ¿ÉÓÃÍøÂç");
        			break;
        		default :
//        			showToast("·þÎñÆ÷ÎÞÏìÓ¦");
        				break;
        		}
        	}
        };
        }catch(Exception e){
        	e.printStackTrace();
        }
       }
        
    
    
    private void initData() {//ÐÂ¼ÓµÄ·½·¨ 2015.9.14-Àî
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		//Èç¹ûbundleÎªnull£¬Ö¤Ã÷ÊÇ°´·µ»Ø¼üÍË³ö³ÌÐò£¬¼´ÔÙ´Îµã»÷³ÌÐò¾Í»á×Ô¶¯µÇÂ¼£¬ËùÒÔloginÄ¬ÈÏÖµÉèÖÃÎªConfig.AUTOLOGIN
		if(bundle!=null){
		login=bundle.getInt("login");
		Log.i(tag, "logins is "+login);
		}
		Log.i(tag, "login is "+login);
		
	}


	public void onClick(View v){
		Log.i(tag, "onclick ±»µã»÷");
    	if(!flag){
    		Message msg=new Message();
    		msg.what=3;
			handler.sendMessage(msg);
		}
		else{
		String tag=null;
		Log.i(tag, "111");
		switch(v.getId())
		{
		case R.id.btnLogin:	//°´ÏÂµÇÂ¼°´Å¥
			
			login(t1.getText().toString(),t2.getText().toString());
			BaseActivity.getSelf().setUserID(t1.getText().toString());
			BaseActivity.getSelf().setPwd(t2.getText().toString());
			btnLogin.setClickable(false);
			Log.i(tag, "BTNLOGIN) ±»µã»÷");
			
			break;
		case R.id.register:	//°´ÏÂ×¢²á½çÃæ
			cancelToast();//Ïû³ýÏûÏ¢ÌáÊ¾¿ò
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
				//Toast.makeText(getApplication(), "°´Å¥²»ÏìÓ¦", Toast.LENGTH_SHORT).show();
				break;
		}
		}
    }
    
	/**
	 * ¸ßË¹Ä£ºýµÄÊµÏÖ SDK Build-tools ±ØÐë´óÓÚ»òµÈÓÚ18
	 * 			  SDK Tools ±ØÐëÐ¡ÓÚ»òµÈÓÚ22
	 * @param bmp  ´«ÈëµÄÒªÊµÏÖÄ£ºýµÄBimapÍ¼Æ¬ £¬¿ÉÒÔÓÃBitmapFactory.decodeResource£¨£©»ñÈ¡
	 * @param img  ´«ÈëµÄImageView
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint("NewApi")
	private void bulr(Bitmap bmp, RelativeLayout view) {
		 	RenderScript rs = RenderScript.create(context);// ¹¹½¨Ò»¸öRenderScript¶ÔÏó
		    Allocation overlayAlloc = Allocation.createFromBitmap(rs, bmp);// ´´½¨ÓÃÓÚÊäÈëµÄ½Å±¾ÀàÐÍ
		    ScriptIntrinsicBlur blur = 
		        ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());// ´´½¨¸ßË¹Ä£ºý½Å±¾
		    blur.setInput(overlayAlloc);// ÉèÖÃÊäÈë½Å±¾ÀàÐÍ
		    blur.setRadius(radius);// ÉèÖÃÄ£ºý°ë¾¶£¬·¶Î§0f<radius<=25f
		    blur.forEach(overlayAlloc);// Ö´ÐÐ¸ßË¹Ä£ºýËã·¨£¬²¢½«½á¹ûÌîÈëÊä³ö½Å±¾ÀàÐÍÖÐ
		    overlayAlloc.copyTo(bmp);// ½«Êä³öÄÚ´æ±àÂëÎªBitmap£¬Í¼Æ¬´óÐ¡±ØÐë×¢Òâ
		    view.setBackground(new BitmapDrawable(getResources(), bmp));//ÉèÖÃ±³¾°
		    rs.destroy();// ¹Ø±ÕRenderScript¶ÔÏó
	}
    
    //³õÊ¼»¯popupWindow
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
			public void onClick(View v) {//Ìø×ªÕÒ»ØÕË»§½çÃæ
				popup.dismiss();
				Intent intent=new Intent(LoginView.this,FindUsername.class);
				startActivity(intent);
			}
        	
        });
        v.findViewById(R.id.btn_forget_password).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {//Ìø×ªÍü¼ÇÃÜÂë½çÃæ
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
    	Log.i(tag, "login ±»Ö´ÐÐ");
    	username=userId;
		this.pwd=pwd;
		Thread thread = new Thread(runnable);
		thread.start();
		try {
			Log.i(tag, "join±»Ö´ÐÐ");
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    //ÕâÊÇÈí¼þ¹Ø±Õºó£¬²»Ð¶ÔØ£¬ÔÙ´ò¿ªÈí¼þ£¬×Ô¶¯µÇÈë
    public void autoRedirectTo(){
    	try{
//    	initProgressDialog();
    	Log.i(tag, "ÕâÊÇÈí¼þ¹Ø±Õºó£¬²»Ð¶ÔØ£¬ÔÙ´ò¿ªÈí¼þ£¬×Ô¶¯µÇÈë");
       	cancelToast();//Ïû³ýÏûÏ¢ÌáÊ¾¿ò
    	Log.i(tag, "·¢ËÍ×Ô¶¯µÇÂ¼ÇëÇó");
    	if(con==null){
    		Log.i(tag, "conÎªnull");
//    		BaseActivity.con=Communication.newInstance();
    	}
    	
    	BaseActivity.getSelf().setUserID(user.getUserID());
    	setPreference(user.getUserID(),user.getPwd());
    	LogUtil.p(tag, "µ±Ç°µÄuserID:"+user.getUserID());
    	LogUtil.p(tag, "´æÈëBaseActivityµÄuserID:"+BaseActivity.getSelf().getUserID());
    	Thread thread = new Thread(autoLoginRunnable);
    	thread.start();
    	
    	Log.i(tag, "×öhttpLogin");
//    	Log.i(tag, "autoRedirectTo---httpLogin");
    	Thread thread2=new Thread(httpLogin);
    	Log.i(tag, "autoRedirectTo---httpLogin111");
		thread2.start();
    	
    	Intent intent=new Intent(this,BBSMainView.class);
    	Bundle bundle=new Bundle();
    	bundle.putInt("login", login);
    	LogUtil.p(tag, "login:"+login);
    	intent.putExtras(intent);
    	startActivity(intent);
//    	new Thread(runnableGetOfflineMessage).start();
    	finish();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    //½øÈëÖ÷½çÃæ
    public void redirectTo()
    {
    	try{
    	cancelToast();//Ïû³ýÏûÏ¢ÌáÊ¾¿ò
//    	Log.i(tag, "·¢ËÍ×Ô¶¯µÇÂ¼ÇëÇó");
//    	Thread thread = new Thread(autoLoginRunnable);
//    	thread.start();
    	
    	Intent intent=new Intent(this,BBSMainView.class);
    	Bundle bundle=new Bundle();
    	bundle.putInt("login", login);
    	intent.putExtras(intent);
    	startActivity(intent);
//    	if(login==Config.AUTOLOGIN){
//			progressDialog.dismiss();
//			Log.i(tag, "½ø¶È¿ò±»ÏûÃð");
//			}
    	finish();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    Runnable runnable=new Runnable(){

		@Override
		public void run() {
			Message msg=new Message();
			
			//ÃÜÂë»òÓÃ»§ÃûÎª¿Õ
			if("".equals(username)||"".equals(pwd))
			{
				respCode="ÓÃ»§Ãû»òÃÜÂë´íÎó";
				msg.what=1;
				handler.sendMessage(msg);
				
			} else{
				Log.i(tag, "username ="+username+"  "+" password ="+pwd);
				//µÚÒ»´ÎµÇÂ¼¾ÍÒªÐÂ½¨Communication,·ñÔò¾ÍÖ±½ÓÁ¬½Ó£¬¾ÍÊÇµ÷ÓÃnetworker ÀïÃæµÄconnect·½·¨
				Log.i(tag, "login"+login);
				if(con==null){
					BaseActivity.con=Communication.newInstance();//ÕâÀï²»ÐèÒªprivate Communication con£¬ÒòÎª»áÔì³Écon²»ÊÇWoliaoBaseActivity
				Log.i("---->>>>","con is null");
				}else{
					Log.i(tag, "openSocketChannel");
					Log.i("**************","con is not null");
					BaseActivity.con.openSocketChannel();
				}
				BaseActivity.con.reqLogin(username,pwd);
				Log.i(tag, "reqLogin ±»Ö´ÐÐ");
				/*sendLoginInfo = new SendLoginInfo(username,pwd);
				respCode=sendLoginInfo.checkLoginInfo();
				if(respCode.equals("µÇÂ¼³É¹¦"))//ÃÜÂë»òÓÃ»§Ãû²»Îª¿Õ
				{
						msg.what=2;handler.sendMessage(msg);
				}else{
					//ÓÃ»§Ãû»òÃÜÂëÊäÈë´íÎó
					msg.what=1;handler.sendMessage(msg);
				}*/
		}
	}
   };
   
   Runnable autoLoginRunnable=new Runnable(){

		@Override
		public void run() {
			//con=Communication.newInstance();//ÕâÀï²»ÐèÒªprivate Communication con£¬ÒòÎª»áÔì³Écon²»ÊÇWoliaoBaseActivity
			if(con==null){
				Log.i(tag, "new con");
				BaseActivity.con=Communication.newInstance();//ÕâÀï²»ÐèÒªprivate Communication con£¬ÒòÎª»áÔì³Écon²»ÊÇWoliaoBaseActivity
			}else if(!con.newNetWorker01.socketChannel.isRegistered()){
				Log.i(tag, "opensocket channel");
				BaseActivity.con.openSocketChannel();
			}
			Log.i(tag, "autoLoginRunnable--con.reqLogin:user.getUserID:"+user.getUserID());
			con.reqLogin(user.getUserID(),user.getPwd());	
			//setCookie();
			Log.i(tag, "ÒÑ·¢ËÍ");
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
		/*sendLoginInfo = new SendLoginInfo(username,null);	
		try {
			respCode=sendLoginInfo.checkLoginInfo();
			Log.i(tag, "ÂÛÌ³µÇÈërespCode:"+respCode);
			if(respCode.equals("success"))//ÃÜÂë»òÓÃ»§Ãû²»Îª¿Õ,²¢ÇÒµÇÂ¼³É¹¦
			{
					handler.sendEmptyMessage(2));//Ìø×ªµ½ÂÛÌ³Ö÷½çÃæ
					Log.d("cookie",sendLoginInfo.getCookie());
			        SharedPreferences.Editor editor1 =  getSharedPreferences("data",Context.MODE_PRIVATE).edit();
			        editor1.putString("cookie", sendLoginInfo.getCookie());
			        editor1.commit();
					
			}else{
				//ÓÃ»§Ãû»òÃÜÂëÊäÈë´íÎó
				handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			handler.sendEmptyMessage(4);//·þÎñÆ÷ÎÞÏìÓ¦
		}*/
		
		setCookie();
	}
	   
   };
   
   
   
   public void setCookie(){
	   Log.i(tag, "setCookie--username:"+user.getUserID());
	   sendLoginInfo = new SendLoginInfo(user.getUserID(),null);	
		try {
			respCode=sendLoginInfo.checkLoginInfo();
			Log.i(tag, "ÂÛÌ³µÇÈërespCode:"+respCode);
			if(respCode.equals("success"))//ÃÜÂë»òÓÃ»§Ãû²»Îª¿Õ,²¢ÇÒµÇÂ¼³É¹¦
			{
					
					Log.d("setCookie()",sendLoginInfo.getCookie());
			        SharedPreferences.Editor editor1 =  getSharedPreferences("data",Context.MODE_PRIVATE).edit();
			        editor1.putString("cookie", sendLoginInfo.getCookie());
			        editor1.commit();
			        handler.sendEmptyMessage(2);//Ìø×ªµ½ÂÛÌ³Ö÷½çÃæ
					
			}else{
				//ÓÃ»§Ãû»òÃÜÂëÊäÈë´íÎó
				handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			handler.sendEmptyMessage(4);//·þÎñÆ÷ÎÞÏìÓ¦
		}
   }
   Runnable runnableGetOfflineMessage = new Runnable(){
	 public void run(){
		 Log.i(tag, "runnableGetOfflineMessage--userID:"+user.getUserID());
		 BaseActivity.con.getOfflineMessage(user.getUserID());
	 }  
   };
 //ÅÐ¶ÏtoastÊÇ·ñ´æÔÚ
 		public void showToast(String s)
 		{
 			if(toast==null)
 			{
 				toast=MyToast.makeText(getApplicationContext(), respCode, Toast.LENGTH_SHORT);
 			}else{
 				toast.setText(s);toast.setDuration(Toast.LENGTH_SHORT);
 			}
 			toast.show();
 		}
 		//cancelµôtoast
 		public void cancelToast()
 		{
 			if(toast!=null){
 				toast.cancel();
 			}
 		}
 		//ÓÃ»§°´·µ»Ø¼ü
 		@Override
 		public void onBackPressed(){
 			cancelToast();
 			Log.i(">>>>","onback");
 			finish();
 		}
 		
// 		private void initProgressDialog() {
// 			// ½ø¶ÈÌáÊ¾¿ò
// 			progressDialog = new ProgressDialog(LoginView.this);
// 			progressDialog.setTitle("ÕýÔÚÎªÄãÌø×ªÖ÷½çÃæ");
// 			progressDialog.setMessage("Loading...");
// 			progressDialog.setCancelable(true);
// 			progressDialog.show();
// 		}

 		
	


		@Override
		public void processMessage(Message msg) {
			try{
			Message msg1 = new Message();//ÓëprocessMessageµÄmsgÇø·Ö
			if(msg.what==Config.LOGIN_SUCCESS){
				
//				Log.i(tag, "LoginActivity----µÃµ½LOGIN_SUCCESS£¬¼´½«Ìø×ªChatMainActivity");
				//Intent intent=new Intent(this,ChatMainActivity.class);
//				Log.i(tag, "µÇÈë³É¹¦ÁË£¬ÇëÇó»ñÈ¡ÀëÏßÏûÏ¢");
				
				setPreference(user.getUserID(),user.getPwd());
				Log.i(tag, "userID:"+user.getUserID()+"¡¢pwd:"+user.getPwd()+"--²åÈëÊý¾Ý³É¹¦");
				BaseActivity.getSelf().setUserID(t1.getText().toString());
				BaseActivity.getSelf().setPwd(t2.getText().toString());
				LogUtil.p(tag, "BaseActivity.getSelf().getUserID:"+BaseActivity.getSelf().getUserID());
				new Thread(runnableGetOfflineMessage).start();
				Log.i(tag, "µÇÈë³É¹¦ÁË£¬ÇëÇó»ñÈ¡ÀëÏßÏûÏ¢");
				new Thread(httpLogin).start();
//				redirectTo();
				//startActivity(intent);
			}else if(msg.what==Config.FAILED){
				Log.i(tag, "msg.what=failed");
				msg1.what=1;
				handler.sendMessage(msg1);//2016.7.16Õâ¸öÐÞ¸ÄºÜÖØÒª£¬handlerµÄmsgºÍprocessMessageµÄmsgÒª²»Í¬
				//Toast.makeText(getApplicationContext(), "ÓÃ»§µÇÂ¼Ê§°Ü", Toast.LENGTH_SHORT).show();
			}else if(msg.what==Config.USER_LOGIN_ALREADY){
				showToast("ÓÃ»§ÒÑµÇÂ¼");
				//Toast.makeText(getApplicationContext(), "ÓÃ»§ÒÑµÇÂ¼", Toast.LENGTH_SHORT).show();
			}else if (msg.what == Config.SEND_NOTIFICATION) {
				LogUtil.p(tag, "ÐÂÏûÏ¢Í¨Öª");
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
