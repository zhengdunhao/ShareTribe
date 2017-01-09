package com.atm.chatonline.schoolnews.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.onekeyshare.ShareAllGird;

import com.atm.charonline.recuit.ui.RecuitPostDetailView;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.share.SharePopupWindow;
import com.atm.chatonline.share.WebPageShare;
import com.example.studentsystem01.R;

public class NewsDetailActivity extends BaseActivity implements
		OnClickListener {

	private String tag = "NewsDetailActivity";
	private String newsId = "", response;
	private String relativePath = "essay_content.action";
	private String cookie;
	private String url = UriAPI.SUB_URL + "schoolActive/getNews/";
	private ProgressDialog progressDialog;// 进度条
	private WebView webView;
	private ImageView imgShare;
	private ImageView imgReturn;

	private BBSConnectNet httpClientGet;
	private WebSettings webSettings;
	// 定义图片存放的地址
	public static String TEST_IMAGE;
	
	
	private SharePopupWindow pop;
	private OnClickListener listener;
	private LinearLayout parentLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail);

		// 初始化ShareSDK
		AbstractWeibo.initSDK(this);
		// 接收被点击的新闻ID
		Bundle bundle = this.getIntent().getExtras();
		Log.i(tag, "newsId:" + bundle.getString("id"));
		newsId = bundle.getString("id");
		// 获取cookie
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		initLintener();
		initView();
		initProgressDialog();
		new Thread(runnable).start();
	}

	void initView() {
		webView = (WebView) findViewById(R.id.news_webView);
		imgShare = (ImageView) findViewById(R.id.iv_share);
		imgReturn = (ImageView) findViewById(R.id.iv_return);
		parentLayout = (LinearLayout) findViewById(R.id.parent_layout);
		imgReturn.setOnClickListener(this);
		imgShare.setOnClickListener(this);
	}

	private void initProgressDialog() {
		// 进度提示框
		progressDialog = new ProgressDialog(NewsDetailActivity.this);
		progressDialog.setTitle("再等等，快出来了");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
	
	//监听对象，判断用户操作（分享给朋友|朋友圈） 2016.4.13 李
		private void initLintener() {
			listener=new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bitmap thumb=BitmapFactory.decodeResource(NewsDetailActivity.this.getResources(), R.drawable.pic_software_small);
					WebPageShare webpage=new WebPageShare(NewsDetailActivity.this, url + newsId + ".do", "校友动态", "快来点击看看快来点击看看快来点击看看快来点击看看快来点击看看快来点击看看快来点击看看", thumb);
					if(!webpage.isInstalled()){
						Toast.makeText(NewsDetailActivity.this.getApplicationContext(), "您还没有安装微信客户端！", Toast.LENGTH_SHORT).show();
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

	// WebView加载网页
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub
		Log.i(tag, "webViewLoadUrl()");
		webSettings = webView.getSettings();
		Log.i(tag, "webViewLoadUrl()2222");
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		Log.i(tag, "webViewLoadUrl()1111");
		// 当在网页中点击其他链接时，依旧用该webView打开链接
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				Log.i(tag, "shouldOverrideUrlLoading-url" + url);
				view.loadUrl(url);
				// 如果不需要其他对点击链接事件的处理返回true，否则返回false
				return true;
			}
		});
		// webView.addJavascriptInterface(new DemoJavaScriptInterface(),
		// "demo1");
		// synCookies(this, url + essayId + ".html");//同步cookie
		Log.i(tag, "loadUrl:" + url + newsId + ".do");
		webView.loadUrl(url + newsId + ".do");
		progressDialog.dismiss();
	}

	Runnable runnable = new Runnable() {
		public void run() {
			webViewLoadUrl();

		}
	};

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_return:
			NewsDetailActivity.this.finish();
			break;

		case R.id.iv_share:
			Log.i(tag, "分享被点了");
			if(pop==null){
				pop=new SharePopupWindow(this, 0.5f, listener);
				}
				pop.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);
				
//			showGrid(false);
			break;
		}
	}

	private void showGrid(boolean silent) {
		Log.i(tag, "showGrid");
		Intent i = new Intent(this, ShareAllGird.class);
		// 分享时Notification的图标
		i.putExtra("notif_icon", R.drawable.ic_launcher);
		// 分享时Notification的标题
		i.putExtra("notif_title", this.getString(R.string.app_name));

		// title标题，在印象笔记、邮箱、信息、微信（包括好友和朋友圈）、人人网和QQ空间使用，否则可以不提供
		i.putExtra("title", this.getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用，否则可以不提供
		i.putExtra("titleUrl", "http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		i.putExtra("text", this.getString(R.string.share_content));
		// imagePath是本地的图片路径，所有平台都支持这个字段，不提供，则表示不分享图片
		i.putExtra("imagePath", NewsDetailActivity.TEST_IMAGE);
		// url仅在微信（包括好友和朋友圈）中使用，否则可以不提供
		i.putExtra("url", "http://sharesdk.cn");
		// thumbPath是缩略图的本地路径，仅在微信（包括好友和朋友圈）中使用，否则可以不提供
		i.putExtra("thumbPath", NewsDetailActivity.TEST_IMAGE);
		// appPath是待分享应用程序的本地路劲，仅在微信（包括好友和朋友圈）中使用，否则可以不提供
		i.putExtra("appPath", NewsDetailActivity.TEST_IMAGE);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
		i.putExtra("comment", this.getString(R.string.share));
		// site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供
		i.putExtra("site", this.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供
		i.putExtra("siteUrl", "http://sharesdk.cn");

		// 是否直接分享
		i.putExtra("silent", silent);
		Log.i(tag, "showGrid1111");
		this.startActivity(i);
	}

	private void initImagePath() {
		try {// 判断SD卡中是否存在此文件夹
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/pic.png";
			} else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath()
						+ "/pic.png";
			}
			File file = new File(TEST_IMAGE);
			// 判断图片是否存此文件夹中
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(),
						R.drawable.pic);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	public static String actionToString(int action) {
		switch (action) {
		case AbstractWeibo.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case AbstractWeibo.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case AbstractWeibo.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case AbstractWeibo.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case AbstractWeibo.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case AbstractWeibo.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case AbstractWeibo.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}

}
