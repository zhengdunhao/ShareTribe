package com.atm.chatonline.bbs.activity.bbs;

/**
 * 此类用于显示论坛的主界面
 * 2015-7-30-郑
 * */

import java.util.ArrayList;
import java.util.List;

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
import android.view.View.OnClickListener;
import android.view.Window;
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

import com.atm.charonline.bbs.util.ExtendsIntent;
import com.atm.charonline.bbs.util.LogUtil;
import com.atm.charonline.bbs.util.SendLoginInfo;
import com.atm.charonline.recuit.adapter.RecuitAdapter;
import com.atm.charonline.recuit.bean.Recuit;
import com.atm.charonline.recuit.util.BBSConnectNet;
import com.atm.chatonline.bbs.activity.login.LoginView;
import com.atm.chatonline.bbs.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.chat.info.User;
import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.setting.ui.SettingView;
import com.example.studentsystem01.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ResourceAsColor")
public class BBSMainView extends BaseActivity implements OnClickListener {
	private String tag = "BBSMainView";
	private SlidingMenu slidingMenu;
	private RadioGroup radioGroup;
	private String title[] = { "发现", "院系", "关注" };
	private LinearLayout linearLayout;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private HorizontalScrollView horizontalScrollView;
	private ImageView imgViewMore, imgViewEdit, imgViewSearch;
	private TextView textView;
	private View view01;
	private static View view02;
	private long mExitTime;
	private View view03;
	private User user;
	private String userID;
	private Activity acitivity;
	private String[] relativePath = { "essay_mainEssay.action",
			"essay_attendEssay.action" };

	private static Handler handler;
	public static Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private ExtendsIntent intent1;
	private ExtendsIntent intent2;
	private ProgressDialog progressDialog;
//	private InitView initview;

	
	//recuit
	private String id="";
	private String tip = "";
	private String relativePath01="recuit_getRecuit.action";
	
	private BBSConnectNet bBSConnectNet;
	private List<Recuit> recuitList = new ArrayList<Recuit>();
	private RecuitAdapter recuitAdapter;
	private PullToRefreshListView plv;
	private int recuitNums = 0;
	private String response;
	private String cookie;
	private TextView recuit_wait;
	private ImageView reImgViewMore,reImgViewEdit,reImgViewSearch;
	private FrameLayout recuitLayout;
	private ScrollPageViewAdapter pageAdapter;
	private Spinner sp;
	private TextView bbsWait;

	//标题栏，是有搜索，编辑的最上面的那一栏,bbs、recuit的标题栏都不一样
	private LinearLayout bbsTitle,recuitTitle;
	
	public static View getView02() {
		return view02;
	}

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_main_view);
		sp = (Spinner) findViewById(R.id.sp_workType);
		sp.setVisibility(View.GONE);
		linearLayout = (LinearLayout) findViewById(R.id.tab_view);// 获取bbs_main_view的子布局，即标题栏下面的布局
		viewPager = (ViewPager) findViewById(R.id.pager);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		pageViews = new ArrayList<View>();
		pageAdapter=new ScrollPageViewAdapter(pageViews);
        //进入等待加载界面。。
        bbsWait = (TextView) findViewById(R.id.bbs_wait);
		new GetDataTask1().execute();
//		InItView();
		InItTitle();
		initUserHead();
		setSelector(0);
//		// 进度提示框
//		progressDialog = new ProgressDialog(BBSMainView.this);
//		progressDialog.setTitle("再等等，快出来了");
//		progressDialog.setMessage("Loading...");
//		progressDialog.setCancelable(true);
//		progressDialog.show();

		initData();
		if (con == null) {
			Log.i(tag, "con为null");

		} else {
			Log.i(tag, "con不为null");
		}
//		InItView();
		
		

		viewPager.setAdapter(pageAdapter);// 添加之后可以屏幕左右滑动
		viewPager.clearAnimation();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// 这个是点击不同卡片，跳转不同的页面

					@Override
					public void onPageSelected(int arg0) {
						Log.i(tag, "viewPager-1");
						setSelector(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						Log.i(tag, "viewPager-2");
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						Log.i(tag, "viewPager-3");
					}
				});
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.MAINVIEW_UPDATEUI){
					pageViews.add(view01);
					pageViews.add(view02);
					pageViews.add(view03);
					pageAdapter.notifyDataSetChanged();
					BBSListView.getBbsWait().setVisibility(View.GONE);
				}
			}
		};
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
//			Log.i(tag, "------------third");
		}
		
		protected void onPostExecute(Boolean result){
			Log.i(tag, "-----------four");
			if(result==true){
			pageViews.add(view01);
			pageViews.add(view02);
			pageViews.add(view03);
			
			viewPager.setAdapter(pageAdapter);// 添加之后可以屏幕左右滑动
			Log.i(tag, "-----------six");
//			viewPager.clearAnimation();
//			viewPager.setOnPageChangeListener(new OnPageChangeListener() {// 这个是点击不同卡片，跳转不同的页面
//
//						@Override
//						public void onPageSelected(int arg0) {
//							setSelector(arg0);
//						}
//
//						@Override
//						public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//						}
//
//						@Override
//						public void onPageScrollStateChanged(int arg0) {
//
//						}
//					});
			}
			progressDialog.dismiss();
			Log.i(tag, "-----------five");
		}
		
	}
*/
	private void initData() {
		user = getPreference();
		userID = BaseActivity.getSelf().getUserID();
		LogUtil.p(tag, "userID:"+userID);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int login = bundle.getInt("login");
			Log.i("BBSMainView", "isAutoLogin is " + login);
			if (login == Config.AUTOLOGIN) {
				Log.i(tag, "login==Config.AUTOLOGIN");
				initAutoLogin();
			}
		}
		// boolean result = dbUtil.queryIsFriendList(userID);
		// if(!result){
		// Log.i(tag, "你的朋友列表没记录");
		// // new Thread(myAttention).start();
		// }

	}

	private void initAutoLogin() {// 建立登录链接
		Log.i(tag, "建立登录连接");
		new Thread(runnable).start();

	}
	public void initUserHead() {
		LogUtil.p(tip, "initUserHead--userID:"+BaseActivity.getSelf().getUserID());
		if (FileUtil.isFile(BaseActivity.getSelf().getUserID())) {
			Log.i(tag, "initUserHead()手机内存卡有图");
		} else {
			Log.i(tag, "initUserHead()手机内存没有图，向服务器拿");
			new Thread(reqUserHeadRunnable).start();
		}
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// con=Communication.newInstance();//这里不需要private Communication
			// con，因为会造成con不是WoliaoBaseActivity
			if (con == null) {
				Log.i(tag, "new con");
				BaseActivity.con = Communication.newInstance();// 这里不需要private
																		// Communication
																		// con，因为会造成con不是WoliaoBaseActivity
			} else if (!con.newNetWorker01.socketChannel.isRegistered()) {
				Log.i(tag, "opensocket channel");
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
	//InItSlidingMenu()已经没在用了2016.7.19
	void InItSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffset(4 / 5);
		slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// 设置触摸范围
																	// TOUCHMODE_FULLSCREEN（全局）
		slidingMenu.attachToActivity(this, SlidingMenu.RIGHT);// SLIDING_CONTENT
		Log.i(tag, "InItSlidingMenu()1111111");
		slidingMenu.setMenu(R.layout.sliding_menu_view);
		Log.i(tag, "InItSlidingMenu()走完了");
	}

	/***
	 * 初始化小视图，即发现，院系，关注等视图 加载到pageViews
	 */
	@SuppressWarnings("deprecation")
	void InItView() {
		Log.i(tag, "initview()---------11111");
		intent1 = new ExtendsIntent(this, BBSListView.class, null,
				relativePath[0], "first", 1);
		Log.i(tag, "initview()---------222222");
		intent2 = new ExtendsIntent(this, BBSListView.class, null,
				relativePath[1], "", 1);
		if (intent1 == null) {
			Log.i(tag, "intent1为null");
		} else {
			Log.i(tag, "intent1不为null");
		}
		Log.i(tag, "initViewRunnable()进入");
		// Looper.prepare();
		view01 = getLocalActivityManager().startActivity("activity01",
				new Intent(intent1)).getDecorView();
		Log.i(tag, "!!!!!!!!!! view01是否为null="+(view01==null));
		view02 = getLocalActivityManager().startActivity("activity02",
				new Intent(BBSMainView.this, BBSDepartmentView.class))
				.getDecorView();
		Log.i(tag, "@@@@@@@@@2");
		view03 = getLocalActivityManager().startActivity("activity03",
				new Intent(intent2)).getDecorView();
		Log.i(tag, "***********");
//		pageViews.add(view01);
//		pageViews.add(view02);
//		pageViews.add(view03);

//		initview = new InitView();
//		initview.execute();
		Log.i(tag, "``````````");
//		progressDialog.dismiss();
//		 new Thread(initViewRunnable).start();
		Log.i(tag, "#######");

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
		int width = getWindowManager().getDefaultDisplay().getWidth() / 3;
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

/*	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imgView_more:
			slidingMenu.toggle(true);
			break;
		case R.id.imgView_edit:

			// startActivity(new
			// Intent(BBSMainView.this,NavigationPublishPost.class));
			startActivity(new Intent(BBSMainView.this, BBSPublishPostView.class));
			break;
		case R.id.imgView_search:
			startActivity(new Intent(BBSMainView.this, NavigationSearch.class));
			break;
		case R.id.recuit_imgView_more:
			slidingMenu.toggle(true);
			break;
		case R.id.recuit_imgView_edit:
			startActivity(new Intent(BBSMainView.this,RecuitNavigationPublishPost.class));
			break;
		case R.id.recuit_imgView_search:
			startActivity(new Intent(BBSMainView.this,RecuitNavigationSearch.class));
			break;
		case R.id.bbs:
			bbsTitle.setVisibility(View.VISIBLE);
			recuitTitle.setVisibility(View.GONE);
			horizontalScrollView.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.VISIBLE);
			recuitLayout.setVisibility(View.GONE);
			slidingMenu.toggle(false);
			break;
		case R.id.chat:
			Log.d("kkkkkkkk", "chat");
			Intent intent = new Intent(BBSMainView.this, ChatMainActivity.class);
			startActivity(intent);
			break;
		case R.id.recuit:
			Log.i(tag, "招聘界面--------即将进入");
			
			recuit_wait.setVisibility(View.GONE);
//			try {
//				loadData();
//			} catch (JSONException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			bbsTitle.setVisibility(View.GONE);
			recuitTitle.setVisibility(View.VISIBLE);
			horizontalScrollView.setVisibility(View.GONE);
			viewPager.setVisibility(View.GONE);
			recuitLayout.setVisibility(View.VISIBLE);
			slidingMenu.toggle(false);
			break;
		case R.id.user_center:
			Log.i(tag, "个人中心被点击");
			Intent intent1=new Intent(BBSMainView.this,UserCenter.class);
			startActivity(intent1);
			break;

		}
	}*/

	@Override
	public void processMessage(Message msg) {// 登录结果
		if (msg.what == Config.LOGIN_SUCCESS) {
			Log.i("BBSMainView",
					"LoginActivity----得到LOGIN_SUCCESS，即将跳转ChatMainActivity");
			Log.i(tag, "第二次登录成功");
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
			LogUtil.p(tag, "新消息通知");
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}

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

//			InItView();
			return null;
		}
	
		protected void onPostExecute(String result){
			InItView();
			Message msg = new Message();
			msg.what = Config.MAINVIEW_UPDATEUI;
			handler.sendMessage(msg);
			bbsWait.setVisibility(View.GONE);
			
//			InItTitle();
//			InItTitle();
			pageAdapter.notifyDataSetChanged();
//			initUserHead();
//			setSelector(0);
		}
	}

	private void redirectTo() {
		Intent intent = new Intent(this, LoginView.class);
		intent.putExtra("login", Config.BE_OFF_LOGIN);
		Log.i(tag, "main view queue size="+queue.size());
		startActivity(intent);
		
	}

	//按两次才退出程序
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
        	 Log.i(tag,"按了系统自带的返回键");
                 if ((System.currentTimeMillis() - mExitTime) > 2000) {
                         Object mHelperUtils;
                         Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                         mExitTime = System.currentTimeMillis();

                 } else {
                         finish();
                         con.shutDownSocketChannel();
                         LogUtil.p(tag, "shutDownSocketChannel");
                 }
                 return true;
         }
         return super.onKeyDown(keyCode, event);
 }

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}


}
