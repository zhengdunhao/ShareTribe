package com.atm.chatonline.bbs.activity.bbs;

/**
 * @author yuki
 * @function choose lables
 * date 2016-07-21
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSHttpClient;
import com.atm.chatonline.bbs.util.PhotoItem;
import com.atm.chatonline.bbs.util.SendDataToServer;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usercenter.view.FlowLayout;
import com.example.studentsystem01.R;

public class BBSChooseLabelView extends BaseActivity implements OnClickListener {
	private String subPath = "atm_hotDeptLabel.action";
	private static String cookie;
	private BBSHttpClient httpClient;
	private String contentResponse, response;
	private String tag = "BBSChooseLableView";
	private String dno;
	private FlowLayout flowLayout;// 显示热门标签
	private ImageView iv_return, iv_addLable;
	private TextView tv_publish;
	private List<View> textViews = new ArrayList<View>();
	private Handler handler;
	private EditText et_lable;
	private List<String> ischeckedHotLabel = new ArrayList<String>();// 用户选择添加的标签
	private List<String> hotLabel = new ArrayList<String>();// 服务器传过来的热门标签
	private LayoutInflater inflater;
	private LinearLayout ll_hotLabel;
	private Intent intent;
	private SendDataToServer send = new SendDataToServer();
	private String str_title, str_content, str_type, selectedDept;
	private List<String> aiteID = new ArrayList<String>();
	private List<PhotoItem> selectedPic = new ArrayList<PhotoItem>();
	private String subURL = UriAPI.SUB_URL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_choose_lable);
		intent = getIntent();
		getDataFromPreviousActivity();
		getCookie();
		requestContent();// 开启线程向服务器获取数据
		inflater = LayoutInflater.from(this);
		initialViews();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					initFlowLayout();
					break;
				}
			};
		};
		setListenerForViews();

	}

	// 获取cookie
	private void getCookie() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
	}

	private void getDataFromPreviousActivity() {
		// TODO Auto-generated method stub
		dno = intent.getStringExtra("id");
		str_content = intent.getStringExtra("str_content");
		str_type = intent.getStringExtra("str_type");
		str_title = intent.getStringExtra("str_title");
		selectedDept = intent.getStringExtra("selectedDept");
		if (intent.getStringArrayListExtra("aiteID") != null)
			aiteID = intent.getStringArrayListExtra("aiteID");
		if (((List<PhotoItem>) intent.getSerializableExtra("selectedPic")) != null)
			selectedPic = (List<PhotoItem>) intent
					.getSerializableExtra("selectedPic");
	}

	// 请求热门标签数据
	private void requestContent() {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					httpClient = new BBSHttpClient(dno, cookie, subPath);
					contentResponse = httpClient.getResponse();
					processData(contentResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void setListenerForViews() {
		// TODO Auto-generated method stub
		iv_addLable.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		tv_publish.setOnClickListener(this);
	}

	private void initialViews() {
		// TODO Auto-generated method stub
		flowLayout = (FlowLayout) findViewById(R.id.flow_layout_view);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_addLable = (ImageView) findViewById(R.id.iv_addLable);
		tv_publish = (TextView) findViewById(R.id.tv_publish);
		et_lable = (EditText) findViewById(R.id.et_lable);
		ll_hotLabel = (LinearLayout) findViewById(R.id.ll_hotLabel);
	}

	// 处理获取到的数据
	private void processData(String response) {
		JSONObject jsonObject;
		try {
			Log.d(tag, "标签内容" + response);
			jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("hotTag");
			int size = jsonArray.length();
			Log.d(tag, "size=" + size);
			for (int i = 0; i < size; i++) {
				String lab = jsonArray.getString(i);
				Log.d(tag, "lab=" + lab);
				hotLabel.add(lab);
			}
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化热门标签
	public void initFlowLayout() {
		Log.i(tag, "设置热门标签");
		if (hotLabel.isEmpty())
			ll_hotLabel.setVisibility(View.GONE);
		else {
			HotLabelOnClickListener m = new HotLabelOnClickListener();
			for (int i = 0; i < hotLabel.size(); i++) {
				TextView view = (TextView) inflater.inflate(
						R.layout.textview_style, flowLayout, false);
				view.setText(hotLabel.get(i));
				flowLayout.addView(view);
				view.setOnClickListener(m);
			}
		}
		flowLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	// 监听热门标签被点击的事件
	class HotLabelOnClickListener implements OnClickListener {
		private boolean flag = false;// 判断标签是否在已经选择的列表里面

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			String lable = (String) tv.getText();
			if (ischeckedHotLabel.contains(lable)) {
				flag = false;
				ischeckedHotLabel.remove(lable);
				v.setBackground(getResources()
						.getDrawable(R.drawable.bg_hottag));
			} else {
				ischeckedHotLabel.add(lable);
				v.setBackground(getResources().getDrawable(
						R.drawable.bg_isselected_hottag));
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.iv_addLable:
			if (!et_lable.getText().toString().isEmpty()) {
				String str = et_lable.getText().toString();
				TextView view = (TextView) inflater.inflate(
						R.layout.textview_style, flowLayout, false);
				HotLabelOnClickListener m = new HotLabelOnClickListener();
				view.setText(str);
				flowLayout.addView(view);
				view.setOnClickListener(m);
				view.setBackground(getResources().getDrawable(
						R.drawable.bg_isselected_hottag));
				ischeckedHotLabel.add(str);
				et_lable.setText("");
				Toast.makeText(this, "添加标签成功", Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(this, "请输入文字", Toast.LENGTH_SHORT).show();
			break;
		case R.id.iv_return:
			AlertDialog.Builder back = new AlertDialog.Builder(this);
			back.setTitle("提示框")
					.setMessage("确定返回上一个界面？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									BBSChooseLabelView.this.finish();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}
							});

			back.create().show();
			break;
		case R.id.tv_publish:
			sendDataToServer();
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (response.equals("success")) {
				Toast.makeText(this, "发帖成功",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(BBSChooseLabelView.this,BBSMainView.class);
				startActivity(intent);
			}
			break;
		}
	}

	private void sendDataToServer() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			String getResponse;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 传递帖子内容
				Map<Object, Object> params = new HashMap<Object, Object>();
				params.put("type", str_type);
				params.put("title", str_title);
				params.put("content", str_content);
				params.put("department", selectedDept);
				if (aiteID.size() != 0) {
					int size = aiteID.size() - 1;
					String str = "";
					for (int i = 0; i < size; i++) {
						str = str + aiteID.get(i) + "*#";
					}
					str = str + aiteID.get(size);
					params.put("aiteID", str);
				}
				if (ischeckedHotLabel.size() != 0) {
					int size = ischeckedHotLabel.size() - 1;
					String str = "";
					for (int i = 0; i < size; i++) {
						str = str + ischeckedHotLabel.get(i) + "*#";
					}
					str = str + ischeckedHotLabel.get(size);
					params.put("label", str);
				}
				if (selectedPic.size() != 1) {
					int photoSize = selectedPic.size() - 1;
					Bitmap[] files = new Bitmap[photoSize];
					Log.d(tag, "photoSize==" + photoSize);
					for (int i = 0; i < photoSize; i++) {
						String path = selectedPic.get(i).getPath();
						Log.d(tag, "path==" + path);
						files[i] = BitmapFactory.decodeFile(path);
					}
					getResponse = send.post(subURL + "essay/publish.do",
							params, files, cookie);
				} else {
					getResponse = send.post(subURL + "essay/publish.do",
							params, null, cookie);
				}
				try {
					JSONObject object = new JSONObject(getResponse);
					response = object.getString("tip");
					Log.d(tag, "respone==" + response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void processPostData() {
		// TODO Auto-generated method stub

	}
}
