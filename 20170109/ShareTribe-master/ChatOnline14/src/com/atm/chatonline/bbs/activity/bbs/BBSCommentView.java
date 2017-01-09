package com.atm.chatonline.bbs.activity.bbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.atm.chatonline.bbs.adapter.ExpressionPagerAdapter;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.ui.PersonalMessageActivity;
import com.example.studentsystem01.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * @类 com.atm.chatonline.activity.bbs ---BBSCommentView
 * @作用 该类用于显示贴子的评论
 * @作者 钟YD
 * @时间 2015-8-24
 * 
 */

public class BBSCommentView extends BaseActivity implements
		OnClickListener {

	private static String tag = "BBSCommentView";
	private PullToRefreshWebView pWebView;
	private WebView comment_view;
	private WebSettings webSettings;
	private ImageView iv_return, iv_expression;
	private EditText input;
	private Button send_btn;
	private String userId = "#", userName, repContent;
	private int floorId = 0;
	private InputMethodManager mInputMethodManager;
	private ViewPager exp_pager;
	private LinearLayout ll_expression, ll_webview, ll_function;
	private List<Map<String, Object>> listItems1, listItems2;
	private List<View> view;
	private View viewPager1, viewPager2;
	private ExpressionPagerAdapter pagerAdapter;
	private SimpleAdapter adapter1, adapter2;
	private GridView grid1, grid2;
	private boolean isFaceShow = false, isKeyboardShow;
	private String[] description1, description2;// 每个表情对应的文字描述
	private Resources res;
	private BBSConnectNet httpClientGet;
	private ProgressDialog dialog;
	private static String cookie;
	private String essayId, url = UriAPI.SUB_URL + "replyList";
	private int[] imageIds1 = { R.drawable.exp01, R.drawable.exp2,
			R.drawable.exp3, R.drawable.exp4, R.drawable.exp5, R.drawable.exp6,
			R.drawable.exp7, R.drawable.exp8, R.drawable.exp9,
			R.drawable.exp10, R.drawable.exp11, R.drawable.exp12,
			R.drawable.exp13, R.drawable.exp14, R.drawable.exp15,
			R.drawable.exp16, R.drawable.exp17, R.drawable.exp18,R.drawable.exp19,
			R.drawable.delete, };
	private int[] imageIds2 = { R.drawable.exp22, R.drawable.exp23,
			R.drawable.exp24, R.drawable.exp25, R.drawable.exp26,
			R.drawable.exp27, R.drawable.exp28, R.drawable.exp29,
			R.drawable.exp30, R.drawable.exp31, R.drawable.exp32,
			R.drawable.exp33, R.drawable.delete, };
	private int webLoadProgress;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment_view);

		initView();
		getArray();// 获取资源文件夹下定义的数组

		// 获取从BBSPostDetailView传过来的essayId
		Bundle bundle = this.getIntent().getExtras();
		essayId = bundle.getString("essayId");

		// 获取cookie
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		// jsonDemo();// 开启线程向服务器传递数据

		webViewLoadUrl();// WebView加载网页
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		accomplishExpBoard();// 实现表情面板
		initEvent();
		input.clearFocus();// 清楚文本框的焦点 10.25
		ll_function.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Log.i("onTouch", "yes");
				if (isKeyboardShow == true) {
					mInputMethodManager.hideSoftInputFromWindow(
							input.getWindowToken(), 0);
				}
				return false;
			}
		});
		// 设置文本框的监听事件 10.25
		input.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1 == true) {
					if (isFaceShow == true) {
						ll_expression.setVisibility(View.GONE);
						isFaceShow = false;
					}
					mInputMethodManager.showSoftInput(input, 0);
					isKeyboardShow = true;
					try {
						Thread.sleep(80);// 解决此时会黑一下屏幕的问题
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	/**
	 * 设置监听
	 */
	private void initEvent() {
		// TODO Auto-generated method stub
		iv_return.setOnClickListener(this);
		send_btn.setOnClickListener(this);
		iv_expression.setOnClickListener(this);
	}

	/**
	 * 因为客户端和网页的cookie是不一样的，所以这里要同步一下cookie
	 */
	public static void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		//cookieManager.removeSessionCookie();// 移除
		Log.i(tag, "synCookies--cookie:" + cookie);
		cookieManager.setCookie(url, cookie);
		CookieSyncManager.getInstance().sync();
	}

	/**
	 * 实现表情面板
	 */
	private void accomplishExpBoard() {
		// TODO Auto-generated method stub
		listItems1 = new ArrayList<Map<String, Object>>();
		listItems2 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imageIds1.length; i++) {
			Map<String, Object> listItem1 = new HashMap<String, Object>();
			listItem1.put("image1", imageIds1[i]);
			listItems1.add(listItem1);
		}
		for (int i = 0; i < imageIds2.length; i++) {
			Map<String, Object> listItem2 = new HashMap<String, Object>();
			listItem2.put("image2", imageIds2[i]);
			listItems2.add(listItem2);
		}
		adapter1 = new SimpleAdapter(this, listItems1, R.layout.simple_item,
				new String[] { "image1" }, new int[] { R.id.image });
		adapter2 = new SimpleAdapter(this, listItems2, R.layout.simple_item,
				new String[] { "image2" }, new int[] { R.id.image });
		viewPager1 = View.inflate(this, R.layout.viewpager1, null);
		viewPager2 = View.inflate(this, R.layout.viewpager2, null);
		grid1 = (GridView) viewPager1.findViewById(R.id.grid1);
		grid2 = (GridView) viewPager2.findViewById(R.id.grid2);
		grid1.setAdapter(adapter1);
		grid2.setAdapter(adapter2);

		view = new ArrayList<View>();
		view.add(viewPager1);
		view.add(viewPager2);
		pagerAdapter = new ExpressionPagerAdapter(view);
		exp_pager.setAdapter(pagerAdapter);

		grid1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int index1 = Math.max(input.getSelectionStart(), 0);
				String oriContent1 = input.getText().toString();
				StringBuilder sBuilder1 = new StringBuilder(oriContent1);
				if (arg2 == 20) {
					if (input.getSelectionStart() > 0) {
						int selection = input.getSelectionStart();
						String text2 = oriContent1.substring(selection - 1);
						if (")".equals(text2)) {
							int start = oriContent1.lastIndexOf("#");
							int end = selection;
							input.getText().delete(start, end);
						}
						// input.getText().delete(selection - 1, selection);
					}
				} else {
					sBuilder1.insert(index1, description1[arg2]);
					input.setText(sBuilder1.toString());
					input.setSelection(index1 + description1[arg2].length());
				}
			}
		});

		grid2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int index2 = Math.max(input.getSelectionStart(), 0);
				String oriContent2 = input.getText().toString();
				StringBuilder sBuilder2 = new StringBuilder(oriContent2);
				if (arg2 == 12) {
					if (input.getSelectionStart() > 0) {
						int selection = input.getSelectionStart();
						String text2 = oriContent2.substring(selection - 1);
						if (")".equals(text2)) {
							int start = oriContent2.lastIndexOf("#");
							int end = selection;
							input.getText().delete(start, end);
						}
						// input.getText().delete(selection - 1, selection);
					}
				} else {
					sBuilder2.insert(index2, description2[arg2]);
					input.setText(sBuilder2.toString());
					input.setSelection(index2 + description2[arg2].length());
				}

			}
		});
	}

	/**
	 * WebView加载网页
	 */
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub
		webSettings = comment_view.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		// 让webView可以显示来自网页的提示框或其他
		comment_view.setWebChromeClient(new MyWebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					webLoadProgress = newProgress;
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		comment_view.setWebViewClient(new WebViewClient() {
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
		comment_view.addJavascriptInterface(new CommentViewJSInterface(),
				"commentView");
		synCookies(this, url + ".jsp?essayId=" + essayId);
		comment_view.loadUrl(url + ".jsp?essayId=" + essayId);
		dialog = ProgressDialog.show(this, null, "页面加载中，请稍后..");
		pWebView();
	}

	private void pWebView() {
		// TODO Auto-generated method stub
		// 设置PullToRefresh刷新模式
		pWebView.setMode(Mode.BOTH);
		// 设置PullRefreshListView上拉加载时的加载提示
		pWebView.getLoadingLayoutProxy(false, true).setPullLabel(
				"加载更多回复");
		pWebView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"加载中...");
		pWebView.getLoadingLayoutProxy(false, true).setReleaseLabel(
				"松开加载");
		// 设置PullRefreshListView下拉加载时的加载提示
		pWebView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
		pWebView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
				"刷新中...");
		pWebView.getLoadingLayoutProxy(true, false).setReleaseLabel(
				"松开刷新");
		pWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<WebView>() {
			
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<WebView> refreshView) {
				// TODO Auto-generated method stub
				// 设置PullRefreshListView下拉加载时的加载提示
				/*pWebView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");*/
//				pWebView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
//						"刷新中...");
//				pWebView.getLoadingLayoutProxy(true, false).setReleaseLabel(
//						"松开刷新");
				/*String str = DateUtils.formatDateTime(BBSCommentView.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);*/
				
				// 下拉刷新热门回复 JS：freshReply();
				new RefreshReplyTask().execute();
//				new GetReplyTask().execute();
				/*refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(str);*/

			}

			
			
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {
				// 上拉加载更多回复 JS：getReply();
				new GetReplyTask().execute();
			}
		});
	}

	// 获取资源文件夹下定义的数组
	private void getArray() {
		// TODO Auto-generated method stub
		res = BBSCommentView.this.getResources();
		description1 = res.getStringArray(R.array.description1);
		description2 = res.getStringArray(R.array.description2);
	}

	// 初始化控件
	private void initView() {
		// TODO Auto-generated method stub
		input = (EditText) findViewById(R.id.input);
		send_btn = (Button) findViewById(R.id.send);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_expression = (ImageView) findViewById(R.id.expression);
		ll_expression = (LinearLayout) findViewById(R.id.ll_expression);
		ll_function = (LinearLayout) findViewById(R.id.ll_function);
		// ll_webview = (LinearLayout) findViewById(R.id.ll_webview);
		exp_pager = (ViewPager) findViewById(R.id.exp_pager);
		pWebView = (PullToRefreshWebView) findViewById(R.id.pwebview);
		comment_view = pWebView.getRefreshableView();
	}

	// Javascript调用安卓的内部类
	class CommentViewJSInterface {
		@JavascriptInterface
		public void replyWho(String id, String name, int floorID) {
			userId = id;
			floorId = floorID;
			mInputMethodManager.showSoftInput(input, 0);
			input.setHint("回复 " + name);
			input.requestFocus();
		}
		
		@JavascriptInterface
		public void toPersonalMsg(String friendID){
			Log.i(tag, "bhj");
			Intent intent=new Intent(BBSCommentView.this,PersonalMessageActivity.class);
			intent.putExtra("friendID", friendID);
			startActivity(intent);
		}
	}

	class RefreshReplyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			LogUtil.d("GetResplyTash~~~~~~~~~");
			// TODO Auto-generated method stub
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			comment_view.getSettings().setBuiltInZoomControls(true);
			comment_view.getSettings().setSupportZoom(true);

			// 加载需要显示的网页
			comment_view.loadUrl("javascript:freshReply()");
			return null;
		}

		
		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
//			comment_view.getSettings().setBuiltInZoomControls(true);
//			comment_view.getSettings().setSupportZoom(true);
//			// 加载需要显示的网页
//			comment_view.loadUrl("javascript:freshReply()");
			pWebView.onRefreshComplete();// 停止刷新操作
		}

	}

	class GetReplyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			LogUtil.d("GetResplyTash~~~~~~~~~");
			// TODO Auto-generated method stub
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			comment_view.getSettings().setBuiltInZoomControls(true);
			comment_view.getSettings().setSupportZoom(true);
			// 加载需要显示的网页
			comment_view.loadUrl("javascript:getReply()");
			
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
//			comment_view.getSettings().setBuiltInZoomControls(true);
//			comment_view.getSettings().setSupportZoom(true);
//			// 加载需要显示的网页
//			comment_view.loadUrl("javascript:getReply()");
			if (webLoadProgress == 100) {
				pWebView.onRefreshComplete();// 停止刷新操作
			}
		}
	}

	// 开启线程向服务器传递数据
	private void jsonDemo() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					httpClientGet = new BBSConnectNet(essayId, "replyList.jsp",
							cookie);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.iv_return:
			BBSCommentView.this.finish();
			break;
		case R.id.send:
			if (input.getText().toString() != null) {
				repContent = input.getText().toString();
				comment_view.loadUrl("javascript:addAReply('" + userId + "',"
						+ floorId + ",'" + repContent + "')");
				input.setText("");
				input.setHint("");
			}
			break;
		case R.id.expression:

			if (!isFaceShow) {
				// 当表情面板出现时，隐藏键盘
				mInputMethodManager.hideSoftInputFromWindow(
						input.getWindowToken(), 0);
				isKeyboardShow = false;
				try {
					Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ll_expression.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				ll_expression.setVisibility(View.GONE);
				isFaceShow = false;
			}
			break;
		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根

	}

}
