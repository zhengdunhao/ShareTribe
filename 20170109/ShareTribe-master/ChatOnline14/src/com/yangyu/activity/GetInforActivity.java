package com.yangyu.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
 *	功能描述：获取用户资料
 * 
 * 启动页面时传递一个int类型的字段type，用于标记获取自己的资料（type = 0）还是别人的资料（type = 1）。
 * 如果尝试获取别人的资料，示例代码会获取不同平台Share SDK的官方帐号的资料。
 * 
 * 如果资料获取成功，会通过{@link ShowInforPage}展示
 */
public class GetInforActivity  extends Activity implements Callback, OnClickListener, WeiboActionListener {
	
	//定义标题栏布局对象
	private TitleLayout llTitle;
	
	private Button sinaBt,renrenBt,qzoneBt,tengxunBt;
	
	private Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		handler = new Handler(this);
		
		setContentView(R.layout.activity_userinfo);
					
		initView();
		
		initData();
					
	}
	
	/**
	 * 初始化组件
	 */
	private void initView(){
		//得到标题栏对象
		llTitle = (TitleLayout) findViewById(R.id.llTitle);

		//得到按钮对象
		sinaBt    = (Button) findViewById(R.id.btnSw);
		renrenBt  = (Button) findViewById(R.id.btnRr);
		qzoneBt   = (Button) findViewById(R.id.btnQz);
		tengxunBt = (Button) findViewById(R.id.btnTc);
		
		
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		//标题栏设置返回按钮监听
		llTitle.getBtnBack().setOnClickListener(this);
		//设置标题栏的标题文本
		llTitle.getTvTitle().setText(R.string.get_my_info);
		
		//设置监听
		sinaBt.setOnClickListener(this);
		renrenBt.setOnClickListener(this);
		qzoneBt.setOnClickListener(this);
		tengxunBt.setOnClickListener(this);
	}
	
	/**
	 * 点击按钮获取授权用户的资料
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(llTitle.getBtnBack())) {
			finish();
			return;
		}
		
		String name = null;		
		
		switch (v.getId()) {
		case R.id.btnSw:
			name = SinaWeibo.NAME;
			break;
		case R.id.btnTc:
			name = TencentWeibo.NAME;
			break;
		case R.id.btnRr:
			name = Renren.NAME;
			break;
		case R.id.btnQz:
			name = QZone.NAME;
			break;
		}	
		
		if (name != null) {
			AbstractWeibo weibo = AbstractWeibo.getWeibo(this, name);
			weibo.setWeiboActionListener(this);
			String account = null;
			
			weibo.showUser(account);
		}
	}

	public void onComplete(AbstractWeibo weibo, int action,HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
		
		Message msg2 = new Message();
		msg2.what = 1;
		JsonUtils ju = new JsonUtils();
		String json = ju.fromHashMap(res);
		msg2.obj = ju.format(json);
		handler.sendMessage(msg2);
	}

	public void onError(AbstractWeibo weibo, int action, Throwable t) {
		t.printStackTrace();
		
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}

	public void onCancel(AbstractWeibo weibo, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}

	/** 处理操作结果 */
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case 1: {
				Intent i = new Intent(this, ShowInforActivity.class);
				i.putExtra("data", String.valueOf(msg.obj));
				startActivity(i);
			}
			break;
			default: {
				AbstractWeibo weibo = (AbstractWeibo) msg.obj;
				String text = NewsDetailActivity.actionToString(msg.arg2);
				switch (msg.arg1) {
					case 1: { // 成功
						text = weibo.getName() + " completed at " + text;
					}
					break;
					case 2: { // 失败
						text = weibo.getName() + " caught error at " + text;
					}
					break;
					case 3: { // 取消
						text = weibo.getName() + " canceled at " + text;
					}
					break;
				}
				
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return false;
	}

	
}
