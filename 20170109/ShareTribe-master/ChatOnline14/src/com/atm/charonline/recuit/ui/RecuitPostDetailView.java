package com.atm.charonline.recuit.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.atm.chatonline.bbs.activity.bbs.BBSClickGoodListView;
import com.atm.chatonline.bbs.activity.bbs.BBSCommentView;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.share.SharePopupWindow;
import com.atm.chatonline.share.WebPageShare;
import com.example.studentsystem01.R;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

/**
 * @类 com.atm.chatonline.activity.bbs ---BBSPostDetailView
 * @作用 该类用于显示贴子详情
 * @作者 钟YD
 * @时间 2015-8-24
 * 
 * */

public class RecuitPostDetailView extends BaseActivity implements OnClickListener {
	private WebView webView;
	private WebSettings webSettings;
	private ImageView iv_return,iv_collect;
	private boolean flag = true;
	private String essayId = "", response;
	private String relativePath = "essay_content.action";
	private BBSConnectNet httpClientGet;
	private String url = UriAPI.SUB_URL + "recuit/";
	public static final int IS_CLICK = 1, IS_NOT_CLICK = 2;
	private Handler handler;
	private int replyNum;
	private static String cookie;
	
	private SharePopupWindow pop;//添加一个Popwindow变量和一个监听对象,线性布局变量 author 李 
    private OnClickListener listener;
    private LinearLayout parentLayout;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recuit_post_detail);
		
		//当在BBSMainView点击任意一篇贴子时，会传过来一个essatId
		Bundle bundle = this.getIntent().getExtras();
		essayId = bundle.getString("id");
		
		//初始化监听对象，监听用户的操作
		initLintener();
		initView();
		initEvent();
				
		//获取cookie
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		jsonDemo();//开启线程向服务器获取数据
		webViewLoadUrl();//WebView加载网页
		

	}
	
	//监听对象，判断用户操作（分享给朋友|朋友圈） 2016.4.13 李
	private void initLintener() {
		listener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap thumb=BitmapFactory.decodeResource(RecuitPostDetailView.this.getResources(), R.drawable.pic_software_small);
				WebPageShare webpage=new WebPageShare(RecuitPostDetailView.this, url + essayId + ".html", "招聘信息", "在这里你可以找到师兄师姐推荐的招聘信息，你可以进来找找是否有你喜欢的工作岗位或实习岗位", thumb);
				if(!webpage.isInstalled()){
					Toast.makeText(RecuitPostDetailView.this.getApplicationContext(), "您还没有安装微信客户端！", Toast.LENGTH_SHORT).show();
					return;
				}
				switch(v.getId()){
				//点击分享给朋友图标
				case R.id.ly_share_friend:
					webpage.shareToWx(0);
					break;
					//点击了分享到朋友圈图标
				case R.id.ly_share_timeline:
					webpage.shareToWx(1);
					break;
				}
				//每次分享完毕关闭弹出窗口
				pop.dismiss();
			}
		};
	}

	//WebView加载网页
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub
		
		webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		//当在网页中点击其他链接时，依旧用该webView打开链接
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				// 如果不需要其他对点击链接事件的处理返回true，否则返回false
				return true;
			}
		});
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo1");
		webView.addJavascriptInterface(new TelRecuitJSInterface(), "recuitView");
		synCookies(this, url + essayId + ".html");//同步cookie
		webView.loadUrl(url + essayId + ".html");
	}
	
	/** 
	 * 因为客户端和网页的cookie是不一样的，所以这里要同步一下cookie 
	 */  
	public static void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//移除  
	    cookieManager.setCookie(url, cookie);
	    CookieSyncManager.getInstance().sync();  
	}  

	//开启线程向服务器获取数据
	private void jsonDemo() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
				httpClientGet = new BBSConnectNet(essayId, relativePath, cookie);
				response = httpClientGet.getResponse();				
				parseJSONWithJSONObject(response);//处理获取到的数据
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();

	}

	//处理获取到的数据
	private void parseJSONWithJSONObject(String response) {
		// TODO Auto-generated method stub
		JSONObject json;
		Message message;
		try {
			json = new JSONObject(response);
			message = new Message();
			message.what = 0;
			boolean clickGood = json.getBoolean("clickGood");
			boolean collect = json.getBoolean("collect");
			replyNum = json.getInt("replyNum");
			if (clickGood == true) {
				message.what = IS_CLICK;
			} else {
				message.what = IS_NOT_CLICK;
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
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		parentLayout=(LinearLayout) findViewById(R.id.parent_layout);
	}

	// 设置监听器
	private void initEvent() {
		iv_return.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.iv_return:
			RecuitPostDetailView.this.finish();
			break;
		case R.id.iv_collect:
			Toast.makeText(RecuitPostDetailView.this, "收藏成功", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}
	
	//Javascript调用安卓方法的内部类
	class DemoJavaScriptInterface{
	// 因为安全问题，在Android4.2中(如果应用的android:targetSdkVersion数值为17+)
				// JS只能访问带有 @JavascriptInterface注解的Java函数。
				// 因此如果你的开发版本比较高，需要在被调用的函数前加上@JavascriptInterface注解。
				@JavascriptInterface
				public void goClickGoodView(String url) {

					Intent intent = new Intent(RecuitPostDetailView.this, BBSClickGoodListView.class);
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					intent.putExtras(bundle);
					RecuitPostDetailView.this.startActivity(intent);
				}
				@JavascriptInterface
				public void finishPostDetailView(){
					RecuitPostDetailView.this.finish();
				}
	}
	/**
	 * js调用native 的句柄是recuitView  方法时tel
	 * @author Jackbing
	 *
	 */
	class TelRecuitJSInterface{
		//type有两种类型 一种是sms，一种是tel
		public void tel(String type,String number){
			
			if(type.equals("sms")){
				//发短信
				sendSmsOnlyNumber(number);
			}else{
				//打电话
				dialPhoneNumber(number);
			}
		}
		
	}
	
	/**
	 * 调用系统发送短信的界面,指定目标发送者
	 * @param content
	 */

	public void sendSmsOnlyNumber(String number){
		Uri smsUri=Uri.parse("smsto:"+number);
		Intent intent=new Intent(Intent.ACTION_SENDTO,smsUri);
		startActivity(intent);
	}
	
	/**
	 * 调用系统打电话的界面，指定目标电话号码
	 * @param number
	 */
	public void dialPhoneNumber(String number){
		Uri telUri=Uri.parse("tel:"+number);
		Intent intent=new Intent(Intent.ACTION_VIEW,telUri);
		startActivity(intent);
	}
	

	@Override
	public void processMessage(Message msg) {
		
		
	}

}
