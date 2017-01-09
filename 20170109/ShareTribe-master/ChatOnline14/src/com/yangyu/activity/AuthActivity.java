package com.yangyu.activity;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.Toast;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.atm.chatonline.schoolnews.ui.NewsDetailActivity;
import com.example.studentsystem01.R;

/**
 * @author yangyu
 *	功能描述：授权和取消授权Activity，由于UI显示需要授权过的平台显示账户的名称，
 *	  因此此页面事实上展示的是“获取用户资料”和“取消授权”两个功能。
 */
public class AuthActivity extends Activity implements Callback, OnClickListener, WeiboActionListener {
	//定义CheckedTextView对象
	private CheckedTextView	 sinaCt,qzoneCt,tengxunCt,renrenCt;
	
	//定义Handler对象
	private Handler handler;

	//定义标题栏对象
	private TitleLayout llTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		
		initView();
		
		initData();
	}

	/**
	 * 初始化组件
	 */
	private void initView(){
		//实例化Handler对象并设置信息回调监听接口
		handler = new Handler(this);

		//得到标题栏对象
		llTitle = (TitleLayout) findViewById(R.id.llTitle);		
		
		//得到组件对象
		sinaCt    = (CheckedTextView)findViewById(R.id.ctvSw);
		qzoneCt   = (CheckedTextView)findViewById(R.id.ctvQz);
		tengxunCt = (CheckedTextView)findViewById(R.id.ctvTc);
		renrenCt  = (CheckedTextView)findViewById(R.id.ctvRr);		
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		llTitle.getBtnBack().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		llTitle.getTvTitle().setText("用户授权登录");
		
		//设置监听
		sinaCt.setOnClickListener(this);
		qzoneCt.setOnClickListener(this);
		tengxunCt.setOnClickListener(this);
		renrenCt.setOnClickListener(this);

		//获取平台列表
		AbstractWeibo[] weibos = AbstractWeibo.getWeiboList(this);
		
		for(int i = 0;i < weibos.length;i++){
			if (!weibos[i].isValid()) {
				continue;
			}
			
			CheckedTextView ctv = getView(weibos[i]);
			if (ctv != null) {
				ctv.setChecked(true);
				// 得到授权用户的用户名称
				String userName = weibos[i].getDb().get("nickname"); 
				if (userName == null || userName.length() <= 0 || "null".equals(userName)) {
					// 如果平台已经授权却没有拿到帐号名称，则自动获取用户资料，以获取名称
					userName = getWeiboName(weibos[i]);
					//添加平台事件监听
					weibos[i].setWeiboActionListener(this);
					//显示用户资料，null表示显示自己的资料
					weibos[i].showUser(null);
				}
				ctv.setText(userName);
			}
		}
	}
	
	/**
	 * 在CheckedTextView组件中显示授权用户的名称
	 */
	private CheckedTextView getView(AbstractWeibo weibo) {
		if (weibo == null) {
			return null;
		}
		
		String name = weibo.getName();
		if (name == null) {
			return null;
		}
		
		View v = null;
		if (SinaWeibo.NAME.equals(name)) {
			v = findViewById(R.id.ctvSw);
		}
		else if (TencentWeibo.NAME.equals(name)) {
			v = findViewById(R.id.ctvTc);
		}		
		else if (Renren.NAME.equals(name)) {
			v = findViewById(R.id.ctvRr);
		}
		else if (QZone.NAME.equals(name)) {
			v = findViewById(R.id.ctvQz);
		}		
		
		if (v == null) {
			return null;
		}
		
		if (! (v instanceof CheckedTextView)) {
			return null;
		}
		
		return (CheckedTextView) v;
	}
	
	/**
	 * 得到授权用户的用户名称
	 */
	private String getWeiboName(AbstractWeibo weibo) {
		if (weibo == null) {
			return null;
		}
		
		String name = weibo.getName();
		if (name == null) {
			return null;
		}
		
		int res = 0;
		if (SinaWeibo.NAME.equals(name)) {
			res = R.string.sinaweibo;
		}
		else if (TencentWeibo.NAME.equals(name)) {
			res = R.string.tencentweibo;
		}		
		else if (Renren.NAME.equals(name)) {
			res = R.string.renren;
		}
		else if (QZone.NAME.equals(name)) {
			res = R.string.qzone;
		}		
		
		if (res == 0) {
			return name;
		}		
		return this.getResources().getString(res);
	}
	
	/**
	 * 授权和取消授权的按钮点击监听事件
	 */
	@Override
	public void onClick(View v) {				
		AbstractWeibo weibo = getWeibo(v.getId());
		
		CheckedTextView ctv = (CheckedTextView) v;
		if (weibo == null) {
			ctv.setChecked(false);
			ctv.setText(R.string.not_yet_authorized);
			return;
		}
		
		if (weibo.isValid()) {
			weibo.removeAccount();
			ctv.setChecked(false);
			ctv.setText(R.string.not_yet_authorized);
			return;
		}
		
		weibo.setWeiboActionListener(this);
		weibo.showUser(null);		
	}

	/**
	 * 获得授权
	 */
	private AbstractWeibo getWeibo(int vid) {
		String name = null;
		switch (vid) {
		// 进入新浪微博的授权页面
		case R.id.ctvSw:
			name = SinaWeibo.NAME;
			break;
		// 进入腾讯微博的授权页面
		case R.id.ctvTc:
			name = TencentWeibo.NAME;
			break;
		// 进入人人网的授权页面
		case R.id.ctvRr:
			name = Renren.NAME;
			break;
		// 进入QQ空间的授权页面
		case R.id.ctvQz:
			name = QZone.NAME;
			break;
		}
		
		if (name != null) {
			return AbstractWeibo.getWeibo(this, name);
		}
		return null;
	}		

	/**
	 * 授权成功的回调
	 *  weibo - 回调的平台
	 *	action - 操作的类型
	 *	res - 请求的数据通过res返回
	 */
	@Override
	public void onComplete(AbstractWeibo weibo, int action,HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);		
	}

	/**
	 * 授权失败的回调
	 */
	@Override
	public void onError(AbstractWeibo weibo, int action, Throwable t) {
		t.printStackTrace();
		
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);	
	}
	
	/**
	 * 取消授权的回调
	 */
	@Override
	public void onCancel(AbstractWeibo weibo, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);	
	}

	/** 
	 * 处理从授权页面返回的结果
	 * 
	 * 如果获取到用户的名称，则显示名称；否则如果已经授权，则显示平台名称
	 */
	@Override
	public boolean handleMessage(Message msg) {
		AbstractWeibo weibo = (AbstractWeibo) msg.obj;
		String text = NewsDetailActivity.actionToString(msg.arg2);

		switch (msg.arg1) {
			case 1: { // 成功
				text = weibo.getName() + " completed at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
			break;
			case 2: { // 失败
				text = weibo.getName() + " caught error at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
				return false;
			}
			case 3: { // 取消
				text = weibo.getName() + " canceled at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		CheckedTextView ctv = getView(weibo);
		if (ctv != null) {
			ctv.setChecked(true);
			String userName = weibo.getDb().get("nickname"); // getAuthedUserName();
			if (userName == null || userName.length() <= 0
					|| "null".equals(userName)) {
				userName = getWeiboName(weibo);
			}
			ctv.setText(userName);
		}
		return false;
	}
}
