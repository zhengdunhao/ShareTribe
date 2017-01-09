package com.atm.chatonline.bbs.activity.bbs;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.recuit.ui.RecuitPostDetailView;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.ui.PersonalMessageActivity;
import com.atm.chatonline.share.SharePopupWindow;
import com.atm.chatonline.share.WebPageShare;
import com.atm.chatonline.usercenter.activity.usercenter.LabelOfBBS;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.activity.bbs ---BBSPostDetailView
 * @作用 该类用于显示贴子详情
 * @作者 钟YD
 * @时间 2015-8-24
 * 
 * */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class BBSPostDetailView extends BaseActivity implements OnClickListener {
	private WebView webView;
	private WebSettings webSettings;
	private LinearLayout ll_clickGood, ll_comment, ll_share, ll_report;
	private ImageView iv_clickGood, iv_comment, iv_share, iv_report, iv_return,
			iv_collect;
	private TextView tv_clickGood, tv_comment, tv_share, tv_report;
	private String essayId = "";
	private String contentResponse, collectResponse, clickResponse;
	private String contentPath = "essay_content.action";// 帖子内容
	private String collectPath = "essay_collectEssay.action";// 收藏帖子路径
	private String clickGoodPath = "essay_clickGood.action";// 点赞路径
	private BBSConnectNet httpClientGet;
	private String url = UriAPI.SUB_URL + "essay/";
	public static final int IS_CLICK_COLLECT = 1, IS_CLICK_NOT_COLLECT = 2,
			NOT_CLICK_IS_COLLECT = 3, NOT_CLICK_NOT_COLLECT = 4;
	public boolean isClicked, isCollected;
	private Handler handler;
	private int replyNum;
	private static String cookie;
	private ProgressDialog dialog;

	private String tag = "BBSPostDetailView";

	private SharePopupWindow pop;// 添加一个Popwindow变量和一个监听对象,线性布局变量 author 李
									// 2016.4.14
	private OnClickListener listener;
	private LinearLayout parentLayout;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_post_detail);

		// 当在BBSMainView点击任意一篇贴子时，会传过来一个essatId
		Bundle bundle = this.getIntent().getExtras();
		essayId = bundle.getString("id");

		// 初始化监听对象，监听用户的操作
		initLintener();
		initView();
		initEvent();

		// 获取cookie
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		requestContent();// 开启线程向服务器获取帖子数据

		// 异步消息处理机制
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case IS_CLICK_COLLECT:
					iv_clickGood.setImageResource(R.drawable.clickgood_green);
					tv_clickGood.setTextColor(0xff33cc66);
					tv_clickGood.setText("取消赞");
					Log.d(tag, "IS_CLICK_COLLECT:collected==" + isCollected);
					iv_collect.setBackground(getResources().getDrawable(R.drawable.collected));
					break;
				case IS_CLICK_NOT_COLLECT:
					iv_clickGood.setImageResource(R.drawable.clickgood_green);
					tv_clickGood.setTextColor(0xff33cc66);
					tv_clickGood.setText("取消赞");
					Log.d(tag, "IS_CLICK_NOT_COLLECT:collected==" + isCollected);
					iv_collect.setBackground(getResources().getDrawable(R.drawable.collect));
					break;
				case NOT_CLICK_IS_COLLECT:
					iv_clickGood.setImageResource(R.drawable.clickgood);
					tv_clickGood.setTextColor(0xff666666);
					tv_clickGood.setText("点赞");
					Log.d(tag, "NOT_CLICK_IS_COLLECT:collected==" + isCollected);
					iv_collect.setBackground(getResources().getDrawable(R.drawable.collected));
				case NOT_CLICK_NOT_COLLECT:
					iv_clickGood.setImageResource(R.drawable.clickgood);
					tv_clickGood.setTextColor(0xff666666);
					tv_clickGood.setText("点赞");
					Log.d(tag, "NOT_CLICK_NOT_COLLECT:collected==" + isCollected);
					iv_collect.setBackground(getResources().getDrawable(R.drawable.collect));
					break;
				}
				tv_comment.setText("评论(" + replyNum + ")");
			}
		};
		webViewLoadUrl();// WebView加载网页

	}

	// WebView加载网页
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub

		webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		// 当在网页中点击其他链接时，依旧用该webView打开链接
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				// 如果不需要其他对点击链接事件的处理返回true，否则返回false
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				super.onPageFinished(view, url);
			}
		});
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo1");
		synCookies(this, url + essayId + ".html");// 同步cookie
		webView.loadUrl(url + essayId + ".html");
		dialog = ProgressDialog.show(this, null, "页面加载中，请稍后..");

	}

	/**
	 * 因为客户端和网页的cookie是不一样的，所以这里要同步一下cookie
	 */
	public static void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		cookieManager.setCookie(url, cookie);
		CookieSyncManager.getInstance().sync();
	}

	// 开启线程向服务器获取帖子数据
	private void requestContent() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					httpClientGet = new BBSConnectNet(essayId, contentPath,
							cookie);
					contentResponse = httpClientGet.getResponse();
					parseJSONWithJSONObject(contentResponse);// 处理获取到的数据
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// 开启线程向服务器传送数据：是否收藏
	private void collect() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					BBSConnectNet get = new BBSConnectNet(essayId, collectPath,
							cookie, isCollected);
					collectResponse = get.getResponse();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 开启线程向服务器传送数据：是否点赞
	private void clickGood() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					BBSConnectNet get = new BBSConnectNet(essayId,
							clickGoodPath, cookie, isClicked);
					clickResponse = get.getResponse();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 处理获取到的数据
	private void parseJSONWithJSONObject(String response) {
		// TODO Auto-generated method stub
		JSONObject json;
		Message message;
		try {
			json = new JSONObject(response);
			message = new Message();
			message.what = 0;
			isClicked = json.getBoolean("clickGood");
			System.out.println("clickGood is " + isClicked);
			isCollected = json.getBoolean("collect");
			replyNum = json.getInt("replyNum");
			if (isClicked) {
				if (isCollected) {
					message.what = IS_CLICK_COLLECT;
				} else
					message.what = IS_CLICK_NOT_COLLECT;
			} else {
				if (isCollected) {
					message.what = NOT_CLICK_IS_COLLECT;
				} else
					message.what = NOT_CLICK_NOT_COLLECT;
			}

			handler.sendMessage(message);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 初始化控件
	private void initView() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.webView);
		ll_clickGood = (LinearLayout) findViewById(R.id.ll_clickGood);
		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		ll_share = (LinearLayout) findViewById(R.id.ll_share);
		ll_report = (LinearLayout) findViewById(R.id.ll_report);
		iv_clickGood = (ImageView) findViewById(R.id.iv_clickGood);
		iv_comment = (ImageView) findViewById(R.id.iv_comment);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_report = (ImageView) findViewById(R.id.iv_report);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		tv_clickGood = (TextView) findViewById(R.id.tv_clickGood);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		tv_share = (TextView) findViewById(R.id.tv_share);
		tv_report = (TextView) findViewById(R.id.tv_report);
		parentLayout = (LinearLayout) findViewById(R.id.parent_layout);
	}

	// 设置监听器
	private void initEvent() {
		// TODO Auto-generated method stub
		ll_clickGood.setOnClickListener(this);
		ll_comment.setOnClickListener(this);
		ll_share.setOnClickListener(this);
		ll_report.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
	}

	// 监听对象，判断用户操作（分享给朋友|朋友圈） 2016.4.13 李
	private void initLintener() {
		listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bitmap thumb = BitmapFactory.decodeResource(
						BBSPostDetailView.this.getResources(),
						R.drawable.pic_software_small_jpg);
				WebPageShare webpage = new WebPageShare(BBSPostDetailView.this,
						url + essayId + ".html", "论坛信息",
						"在这里你可以和同校的学生交流，甚至和毕业的师兄师姐/还没毕业的师弟师妹进行交流，相互帮助", thumb);
				if (!webpage.isInstalled()) {
					Toast.makeText(
							BBSPostDetailView.this.getApplicationContext(),
							"您还没有安装微信客户端！", Toast.LENGTH_SHORT).show();
					return;
				}
				switch (v.getId()) {
				// 点击分享给朋友图标
				case R.id.ly_share_friend:
					
					webpage.shareToWx(0);
					break;
				// 点击了分享到朋友圈图标
				case R.id.ly_share_timeline:
					webpage.shareToWx(1);
					break;
				}
				// 每次分享完毕关闭弹出窗口
				pop.dismiss();
			}
		};
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ll_clickGood:
			String response = "";
			clickGood();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONObject click = new JSONObject(clickResponse);
				response = click.getString("tip");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isClicked == false) {
				if (response.equals("success")) {
					iv_clickGood.setImageResource(R.drawable.clickgood_green);
					tv_clickGood.setTextColor(0xff33cc66);
					tv_clickGood.setText("取消赞");
					isClicked = true;
				} else {
					Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
				}
			} else {
				if (response.equals("success")) {
					iv_clickGood.setImageResource(R.drawable.clickgood);
					tv_clickGood.setTextColor(0xff666666);
					tv_clickGood.setText("点赞");
					isClicked = false;
				} else {
					Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.ll_comment:
			Intent intent_comment = new Intent(this, BBSCommentView.class);
			Bundle bundle = new Bundle();
			bundle.putString("essayId", essayId);
			intent_comment.putExtras(bundle);
			startActivity(intent_comment);
			break;
		case R.id.ll_share:
			// 添加一个弹出窗口，author 李 2016.4.14
			if (pop == null) {
				pop = new SharePopupWindow(this, 0.5f, listener);
			}
			pop.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.ll_report:
			Intent intent_report = new Intent(this, BBSReportView.class);
			intent_report.putExtra("essayId", essayId);
			startActivity(intent_report);
			break;
		case R.id.iv_return:
			BBSPostDetailView.this.finish();
			break;
		case R.id.iv_collect:
			String collResponse = "";
			collect();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONObject collect = new JSONObject(collectResponse);
				collResponse = collect.getString("tip");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isCollected == false) {
				iv_collect.setBackground(getResources().getDrawable(
						R.drawable.collected));
				Toast.makeText(BBSPostDetailView.this, "收藏成功",
						Toast.LENGTH_SHORT).show();
				isCollected = true;
			} else {
				iv_collect.setBackground(getResources().getDrawable(
						R.drawable.collect));
				Toast.makeText(BBSPostDetailView.this, "已取消收藏",
						Toast.LENGTH_SHORT).show();
				isCollected = false;
			}
			break;
		}

	}

	// Javascript调用安卓方法的内部类
	class DemoJavaScriptInterface {
		// 因为安全问题，在Android4.2中(如果应用的android:targetSdkVersion数值为17+)
		// JS只能访问带有 @JavascriptInterface注解的Java函数。
		// 因此如果你的开发版本比较高，需要在被调用的函数前加上@JavascriptInterface注解。
		@JavascriptInterface
		public void goClickGoodView(String url) {

			Intent intent = new Intent(BBSPostDetailView.this,
					BBSClickGoodListView.class);
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			intent.putExtras(bundle);
			BBSPostDetailView.this.startActivity(intent);
		}

		@JavascriptInterface
		public void finishPostDetailView() {
			BBSPostDetailView.this.finish();
		}

		// 跳转到个人信息列表2015.11.16李
		@JavascriptInterface
		public void toPersonalMsg(String friendID) {
			Intent intent = new Intent(BBSPostDetailView.this,
					PersonalMessageActivity.class);
			intent.putExtra("friendID", friendID);
			startActivity(intent);
		}

		// 跳转到标签列表2015.11.16李
		@JavascriptInterface
		public void goLabelofBBS(String label) {
			Intent intent = new Intent(BBSPostDetailView.this, LabelOfBBS.class);
			intent.putExtra("cookie", cookie);
			intent.putExtra("userTag", label);
			startActivity(intent);
		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根

	}

}
