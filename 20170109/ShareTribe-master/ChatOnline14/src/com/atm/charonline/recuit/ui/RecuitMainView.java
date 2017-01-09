
package com.atm.charonline.recuit.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.recuit.util.BBSConnectNet;
import com.atm.chatonline.bbs.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SendLoginInfo;
import com.atm.chatonline.bbs.util.SharedPreferenceUtils;
import com.atm.chatonline.bbs.util.SlidingTitleLayout;
import com.atm.chatonline.chat.info.User;
import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.recuit.adapter.RecuitAdapter;
import com.example.studentsystem01.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @类 com.atm.charonline.recuit.ui ---RecuitMainView
 * @作用
 * @作者 陈小二
 * @时间 2015-10-26
 * 
 */
@SuppressLint("ResourceAsColor")
public class RecuitMainView extends BaseActivity {
	private SlidingMenu slidingMenu;
	private RadioGroup radioGroup;
	private String title[] = { "招聘", "求职"};
	private LinearLayout linearLayout;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ImageView imgViewMore, imgViewEdit, imgViewSearch;
	private HorizontalScrollView horizontalScrollView;
	private TextView textView;
	private View view01;
	private static View view02;
	private User user;
	private String userID;
	private Activity acitivity;
	private String[] relativePath = { "recuit_getRecuit.action",
	"apply_getApply.action" };
	private SlidingTitleLayout slidingTitleLayout;
	private Spinner sp;
	private int nums = 0;

	private static Handler handler;
	public static Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private ExtendsIntent[] intent;
	private ProgressDialog progressDialog;
//	private InitView initview;

	
	//recuit
	private String id="";
	private String tip = "";
	
	private BBSConnectNet bBSConnectNet;
	private RecuitAdapter recuitAdapter;
	private PullToRefreshListView plv;
	private int recuitNums = 0;
	private String response;
	private String cookie;
	private TextView recuit_wait;
	private FrameLayout recuitLayout;
	private ScrollPageViewAdapter pageAdapter;
	private TextView recuitWait;

	public static View getView02() {
		return view02;
	}

	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.bbs_main_view);
	sp = (Spinner) findViewById(R.id.sp_workType);
	recuitWait = (TextView)findViewById(R.id.bbs_wait);
	linearLayout = (LinearLayout) findViewById(R.id.tab_view);// 获取bbs_main_view的子布局，即标题栏下面的布局
	viewPager = (ViewPager) findViewById(R.id.pager);
	slidingTitleLayout=(SlidingTitleLayout)findViewById(R.id.sliding_title_layout);
	slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
	horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
	pageViews = new ArrayList<View>();
	pageAdapter=new ScrollPageViewAdapter(pageViews);
	intent = new ExtendsIntent[30];
	new GetDataTask1().execute();
//	InItView();
	InItTitle();
//	initUserHead();
	setSelector(0);
//	// 进度提示框
//	progressDialog = new ProgressDialog(BBSMainView.this);
//	progressDialog.setTitle("再等等，快出来了");
//	progressDialog.setMessage("Loading...");
//	progressDialog.setCancelable(true);
//	progressDialog.show();

	initData();
	if (con == null) {
		LogUtil.d( "con为null");

	} else {
		LogUtil.d( "con不为null");
	}
//	InItView();
	
	

	viewPager.setAdapter(pageAdapter);// 添加之后可以屏幕左右滑动
	viewPager.clearAnimation();
	viewPager.setOnPageChangeListener(new OnPageChangeListener() {// 这个是点击不同卡片，跳转不同的页面

				@Override
				public void onPageSelected(int arg0) {
					LogUtil.d( "viewPager-1");
					setSelector(arg0);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					LogUtil.d( "viewPager-2");
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					LogUtil.d( "viewPager-3");
				}
			});
	handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==Config.MAINVIEW_UPDATEUI){
				pageViews.add(view01);
				pageViews.add(view02);
				pageAdapter.notifyDataSetChanged();
				nums++;
			}
		}
	};
    sp.setOnItemSelectedListener(
    		new OnItemSelectedListener() {
    			public void onItemSelected(AdapterView<?> parent, 
    				View view, int position, long id) {
    				LogUtil.d("点击了spinner");
//    				String a = "Spinner1: position="  + position  + " id="  + id;
    				LogUtil.d("点击了spinner");
//    				MyToast toast=MyToast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT);
//    				toast.show();
    				LogUtil.d("点击了spinner");
//    				recuitList.clear();
    				switch(position) {
    				case 0:
        				LogUtil.d("点击了全部");
    					tip="全部";
//    					 Message msg1 = new Message();
//    					 msg1.what = Config.ALLKIND; 
//    					 if(RecuitListView.getHandler()!=null)
//    					 RecuitListView.getHandler().sendMessage(msg1);
//    					 if( ApplyListView.getHandler()!=null)
//    					 ApplyListView.getHandler().sendMessage(msg1);
    					 changeWorkType(Config.ALLKIND);
    					break;
    				case 1:
        				LogUtil.d("点击了实习");
//    					tip="实习";
//   					 Message msg5 = new Message();
//   					 msg5.what = Config.INTERNSHIP;
//					 if(RecuitListView.getHandler()!=null)
//   					 RecuitListView.getHandler().sendMessage(msg5);
//					 if( ApplyListView.getHandler()!=null)
//					 ApplyListView.getHandler().sendMessage(msg5);
					 changeWorkType(Config.INTERNSHIP);
    					break;
    				case 2:
        				LogUtil.d("点击了兼职");
    					tip="兼职";
//   					 Message msg2 = new Message();
//   					 msg2.what = Config.PARTTIME;
//					 if(RecuitListView.getHandler()!=null)
//   					 RecuitListView.getHandler().sendMessage(msg2);
//					 if( ApplyListView.getHandler()!=null)
//					 ApplyListView.getHandler().sendMessage(msg2);
					 changeWorkType(Config.PARTTIME);
    					break;
    				case 3:
        				LogUtil.d("点击了全职");
    					tip="全职";
//   					 Message msg3 = new Message();
//   					 msg3.what = Config.FULLTIME;
//					 if(RecuitListView.getHandler()!=null)
//   					 RecuitListView.getHandler().sendMessage(msg3);
//					 if( ApplyListView.getHandler()!=null)
//					 ApplyListView.getHandler().sendMessage(msg3);
					 changeWorkType(Config.FULLTIME);
    					break;
    				default:
        				LogUtil.d("点击了全部r");
    					tip="全部";
//   					 Message msg4 = new Message();
//   					 msg4.what = Config.ALLKIND;
//					 if(RecuitListView.getHandler()!=null)
//   					 RecuitListView.getHandler().sendMessage(msg4);
//					 if( ApplyListView.getHandler()!=null)
//					 ApplyListView.getHandler().sendMessage(msg4);
					 changeWorkType(Config.ALLKIND);
    				}
    				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					 changeWorkType(Config.ALLKIND);
				}
    			});
}
/**
	 * @param allkind
	 */
	protected void changeWorkType(int allkind) {
			 Message msg4 = new Message();
			 msg4.what = allkind;
		 if(RecuitListView.getHandler()!=null)
			 RecuitListView.getHandler().sendMessage(msg4);
		 Message msg5 = new Message();
		 msg5.what = allkind;
		 if( ApplyListView.getHandler()!=null)
		 ApplyListView.getHandler().sendMessage(msg5);
		
	}

/*	class InitView extends AsyncTask<Void,Integer,Boolean>{
	@Override
	protected void onPreExecute(){
		Log.i(tag, "--------------first");
		progressDialog.show();
		Log.i(tag,"---------progressDialog.show()");
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.i(tag, "------------------second");
		try{
		Looper.prepare();
		view01 = getLocalActivityManager().startActivity("activity01",
				new Intent(intent1)).getDecorView();
		Log.i(tag, "-------!!!!!!!!!!");
		view02 = getLocalActivityManager().startActivity("activity02",
				new Intent(BBSMainView.this, BBSDepartmentView.class))
				.getDecorView();
		Log.i(tag, "---------@@@@@@@@@2");
		view03 = getLocalActivityManager().startActivity("activity03",
				new Intent(intent2)).getDecorView();
		Log.i(tag, "---------@@@@@@@@@333");
		
		viewPager.setAdapter(new ScrollPageViewAdapter(pageViews));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	protected void onProgressUpdate(){
//		Log.i(tag, "------------third");
	}
	
	protected void onPostExecute(Boolean result){
		Log.i(tag, "-----------four");
		if(result==true){
		pageViews.add(view01);
		pageViews.add(view02);
		pageViews.add(view03);
		
		viewPager.setAdapter(pageAdapter);// 添加之后可以屏幕左右滑动
		Log.i(tag, "-----------six");
//		viewPager.clearAnimation();
//		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// 这个是点击不同卡片，跳转不同的页面
//
//					@Override
//					public void onPageSelected(int arg0) {
//						setSelector(arg0);
//					}
//
//					@Override
//					public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//					}
//
//					@Override
//					public void onPageScrollStateChanged(int arg0) {
//
//					}
//				});
		}
		progressDialog.dismiss();
		Log.i(tag, "-----------five");
	}
	
}
*/
private void initData() {
	user = getPreference();
	userID = user.getUserID();
	Bundle bundle = getIntent().getExtras();
//	if (bundle != null) {
//		int login = bundle.getInt("login");
//		Log.i("BBSMainView", "isAutoLogin is " + login);
//		if (login == Config.AUTOLOGIN) {
//			LogUtil.d("login==Config.AUTOLOGIN");
//			initAutoLogin();
//		}
//	}
	// boolean result = dbUtil.queryIsFriendList(userID);
	// if(!result){
	// Log.i(tag, "你的朋友列表没记录");
	// // new Thread(myAttention).start();
	// }

}

private void initAutoLogin() {// 建立登录链接
	LogUtil.d("建立登录连接");
	new Thread(runnable).start();

}

public void initUserHead() {
	if (FileUtil.isFile(userID)) {
		LogUtil.d( "initUserHead()手机内存卡有图");
	} else {
		LogUtil.d("initUserHead()手机内存没有图，向服务器拿");
		new Thread(reqUserHeadRunnable).start();
	}
}

Runnable runnable = new Runnable() {

	@Override
	public void run() {
		// con=Communication.newInstance();//这里不需要private Communication
		// con，因为会造成con不是WoliaoBaseActivity
		if (con == null) {
			LogUtil.d( "new con");
			BaseActivity.con = Communication.newInstance();// 这里不需要private
																	// Communication
																	// con，因为会造成con不是WoliaoBaseActivity
		} else if (!con.newNetWorker01.socketChannel.isRegistered()) {
			LogUtil.d( "opensocket channel");
			BaseActivity.con.openSocketChannel();
		}
		// con.reqLogin(user.getUserID(),user.getPwd());
		// Log.i(tag, "已发送");
	}

};

Runnable reqUserHeadRunnable = new Runnable() {
	public void run() {
		BaseActivity.con.reqUserHead(userID);
	}
};

// Runnable myAttention = new Runnable(){
// public void run(){
// con.reqMyAttentionList();
// }
// };

/**
 * 初始化滑动菜单
 * 
 * */

void InItSlidingMenu() {
	slidingMenu = new SlidingMenu(this);
	slidingMenu.setMode(SlidingMenu.LEFT);
	slidingMenu.setBehindOffset(4 / 5);
	slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
	slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// 设置触摸范围
																// TOUCHMODE_FULLSCREEN（全局）
	slidingMenu.attachToActivity(this, SlidingMenu.RIGHT);// SLIDING_CONTENT
	LogUtil.d("InItSlidingMenu()1111111");
	slidingMenu.setMenu(R.layout.sliding_menu_view);
	LogUtil.d( "InItSlidingMenu()走完了");
}

/***
 * 初始化小视图，即发现，院系，关注等视图 加载到pageViews
 */
@SuppressWarnings("deprecation")
void InItView() {
	LogUtil.d("initview()---------11111");
	intent[nums] = new ExtendsIntent(this, RecuitListView.class, null,
			relativePath[0], tip,1);
	LogUtil.d("initview()---------222222");
	intent[nums+1] = new ExtendsIntent(this, ApplyListView.class, null,
			relativePath[1], tip, 1);
	LogUtil.d("initViewRunnable()进入");
	// Looper.prepare();
	view01 = getLocalActivityManager().startActivity("1activity01"+nums,
			new Intent(intent[nums])).getDecorView();
	LogUtil.d("!!!!!!!!!!");
	LogUtil.d( "@@@@@@@@@2");
	view02 = getLocalActivityManager().startActivity("1activity03"+nums,
			new Intent(intent[nums+1])).getDecorView();
	LogUtil.d( "***********");
	pageViews.add(view01);
	pageViews.add(view02);
//	pageViews.add(view03);
	nums+=2;
	LogUtil.d( "***********");

//	Message msg = new Message();
//	msg.what = Config.MAINVIEW_UPDATEUI;
//	handler.sendMessage(msg);
	
//	InItTitle();
//	InItTitle();
	pageAdapter.notifyDataSetChanged();
//	initview = new InitView();
//	initview.execute();
//	progressDialog.dismiss();
//	 new Thread(initViewRunnable).start();

}

/***
 * 初始化radiobutton，设置每个radiobutton的长度，当刚开始初始化时，第一个角标即发现被初始化为红色，其余为灰色
 * 把所有radiobutton放在radioGroup上
 */
@SuppressLint("ResourceAsColor")
void InItRadioButton() {
	@SuppressWarnings("deprecation")
	int width = getWindowManager().getDefaultDisplay().getWidth() / 3;// 求平均长度
	for (int i = 0; i < title.length; i++) {
		RadioButton radioButton = new RadioButton(this, null,
				R.style.radioButton);
		// radioButton.setText(title[i]);这里我省略了
		radioButton.setTextSize(17);
		radioButton.setTextColor(com.example.studentsystem01.R.color.black);
		radioButton.setWidth(width);
		// radioButton.setHeight(height);
		radioButton.setGravity(Gravity.CENTER);
		radioGroup.addView(radioButton);
	}
}

/***
 * init title
 * 
 */
@SuppressLint("ResourceAsColor")
void InItTitle() {
	textViews = new ArrayList<TextView>();
	@SuppressWarnings("deprecation")
	int width = getWindowManager().getDefaultDisplay().getWidth() / 2;
	int height = 70;
	for (int i = 0; i < title.length; i++) {
		textView = new TextView(this);
		textView.setText(title[i]);
		textView.setTextSize(17);
		textView.setTextColor(com.example.studentsystem01.R.color.black);
		textView.setWidth(width);
		// textView.setHeight(height - 30);
		textView.setGravity(Gravity.CENTER);
		textView.setId(i);
		textView.setOnClickListener(new TextView.OnClickListener() {// 创建监听
			public void onClick(View v) {
				setSelector(v.getId());
			}

		}); // 每次点击textview都有触发响应，都会调用setSelector(int
			// id),id为多少，就跳到哪个textview
		textViews.add(textView);
		// 分割线

		// 我个人认为是这样的，子视图向父视图说明需要在父视图里面占多大。子视图：textview，父视图：是个布局LinearLayout
		// textview向入驻LinearLayout就要通过LayoutParam来传递大小。

		View view = new View(this);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.width = 1;
		layoutParams.height = height - 40;
		layoutParams.gravity = Gravity.CENTER;
		view.setLayoutParams(layoutParams);
		view.setBackgroundColor(com.example.studentsystem01.R.color.gray);
		linearLayout.addView(textView);
		// if (i != title.length - 1) {
		// linearLayout.addView(view);
		// }

	}
}

/***
 * 选中效果 个人觉得，这个是起初始化作用，所以一般先赋角标为0的textview，然后角标为0的textview先变成红色，其余的依次弄成灰色
 * 全部textview都有弄
 */
@SuppressWarnings("deprecation")
public void setSelector(int id) {
	for (int i = 0; i < title.length; i++) {
		if (id == i) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.grouplist_item_bg_normal);
			textViews.get(id).setBackgroundDrawable(
					new BitmapDrawable(bitmap));
			textViews.get(id).setTextColor(Color.GREEN);// 选中则显示红色
			viewPager.setCurrentItem(i);
		}

		else {
			textViews.get(i).setBackgroundDrawable(new BitmapDrawable());
			textViews.get(i).setTextColor(
					com.example.studentsystem01.R.color.black);
		}
	}
}

public void onClick(View v) {

	switch (v.getId()) {
	
	}
}

@SuppressLint("ResourceAsColor")
@Override
public void processMessage(Message msg) {// 登录结果
	if (msg.what == Config.SUCCESS) {
		Log.i("BBSMainView",
				"LoginActivity----得到LOGIN_SUCCESS，即将跳转ChatMainActivity");
		// Intent intent=new Intent(this,ChatMainActivity.class);
		// setPreference(username,pwd);
		Thread thread = new Thread(httpLogin);
		thread.start();
		// startActivity(intent);
	} else if (msg.what == Config.FAILED) {
		// msg.what=1;handler.sendMessage(msg);
		// Toast.makeText(getApplicationContext(), "用户登录失败",
		// Toast.LENGTH_SHORT).show();
	} else if (msg.what == Config.USER_LOGIN_ALREADY) {
		// showToast("用户已登录");
		// Toast.makeText(getApplicationContext(), "用户已登录",
		// Toast.LENGTH_SHORT).show();
	} else if (msg.what == Config.SEND_NOTIFICATION) {
		sendNotifycation();
	}

}

Runnable httpLogin = new Runnable() {

	@Override
	public void run() {
		SendLoginInfo sendLoginInfo = new SendLoginInfo(user.getUserID(),
				null);
		try {
			String respCode = sendLoginInfo.checkLoginInfo();
			if (respCode.equals("success"))// 密码或用户名不为空,并且登录成功
			{
				// handler.sendEmptyMessage(2);//跳转到论坛主界面
				SharedPreferences pref = getSharedPreferences("data",
						Context.MODE_PRIVATE);
				String cookie = pref.getString("cookie", "");
				if (cookie.equals("")) {
					Log.d("cookie", sendLoginInfo.getCookie());
					SharedPreferences.Editor editor1 = getSharedPreferences(
							"data", Context.MODE_PRIVATE).edit();
					editor1.putString("cookie", sendLoginInfo.getCookie());
					editor1.commit();
				} else {
					Log.i("cookie", "cookie =" + cookie);
				}

			} else {
				Log.i("/////", "1111");
				// 用户名或密码输入错误
				// handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			Log.i("0000", "2222");
			// handler.sendEmptyMessage(4);//服务器无响应
		}
	}

};
/*
// 尝试用线程加载数据
 Runnable initViewRunnable = new Runnable(){
 @SuppressWarnings("deprecation")
 public void run(){
	 Log.i(tag, "initViewRunnable()进来");
		 Looper.prepare();
		view01 = BBSMainView.this.getLocalActivityManager().startActivity("activity01",
				new Intent(intent1)).getDecorView();
		Log.i(tag, "!!!!!!!!!!");
		view02 = BBSMainView.this.getLocalActivityManager().startActivity("activity02",
				new Intent(BBSMainView.this, BBSDepartmentView.class))
				.getDecorView();
		Log.i(tag, "@@@@@@@@@2");
		view03 = BBSMainView.this.getLocalActivityManager().startActivity("activity03",
				new Intent(intent2)).getDecorView();
		Log.i(tag, "***********");
 Log.i(tag, "+++++++++++");
 Message msg1 = new Message();
 msg1.what = Config.REFRESH_UI;
 handler.sendMessage(msg1);
 }
 };
*/
@SuppressWarnings("deprecation")
@Override
protected void onDestroy() {
	// TODO 自动生成的方法存根
	super.onDestroy();
	Log.i(">>>>>>>>>", "ondestory");
	new Thread(exitRunnable).start();
}

Runnable exitRunnable = new Runnable() {

	@Override
	public void run() {
		Log.i("---->>>>>>", "userId = " + user.getUserID());
		con.exit(user.getUserID());
		con.shutDownSocketChannel();
		// redirectTo();
	}

};
/*
 * private void redirectTo(){ Intent intent=new
 * Intent(this,LoginView.class); intent.putExtra("login", Config.AUTOLOGIN);
 * startActivity(intent); finish(); }
 */

/**
 * 异步加载数据
 */
private class GetDataTask1 extends AsyncTask<Void , Void , String>{
	
	
	public GetDataTask1(){
	}
	
	protected String doInBackground(Void... params) {
	try{
		Thread.sleep(500);
		}catch(InterruptedException e){
		}
		

//		InItView();
		return null;
	}

	protected void onPostExecute(String result){
		InItView();
//		pageViews.add(view01);
//		pageViews.add(view02);
////		pageViews.add(view03);
//		LogUtil.d( "***********");

//		Message msg = new Message();
//		msg.what = Config.MAINVIEW_UPDATEUI;
//		handler.sendMessage(msg);
		
//		InItTitle();
//		InItTitle();
		recuitWait.setVisibility(View.GONE);
//		pageAdapter.notifyDataSetChanged();
		
//		InItView();
//		initUserHead();
//		setSelector(0);
	}}
	//按两次才退出程序
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogUtil.d("按了系统自带的返回键");
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
                   Object mHelperUtils;
                   Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                   mExitTime = System.currentTimeMillis();

			} else {
                   finish();
                   con.shutDownSocketChannel();
                   LogUtil.d("shutDownSocketChannel");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		if(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext())==null){
			slidingTitleLayout.hideBadgeView();
		}else{
			slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
		}
	}
}
