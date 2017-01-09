package com.atm.chatonline.bbs.activity.bbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.BBSHttpClient;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.activity.bbs ---BBSReportView
 * @作用 该类用于显示举报界面
 * @作者 钟YD
 * @时间 2015-8-24
 * 
 */

public class BBSReportView extends BaseActivity implements OnClickListener {
	private ImageView iv_return;
	private Button btn_commit;
	private String response;
	private CheckBox cb_sex, cb_ad, cb_against, cb_head, cb_other;
	private String essayId;
	private Intent intent;
	private List<String> reason = new ArrayList<String>();
	private String reportReason = "";
	private EditText et_other;
	private String cookie;
	private final int REPORT = 0;
	private String subPath = "atm_report.action";
	private Handler handler;
	private final int REPORT_SUCCESS = 1;
	private Context context;
	private String tag = "BBSReportView";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report_view);
		initViews();
		getCookie();
		setListenerForView();
		intent = getIntent();
		context = getApplicationContext();
		essayId = intent.getStringExtra("essayId");
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case REPORT_SUCCESS:
					Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
					BBSReportView.this.finish();
					break;
				}				
			}
		};
	}

	private void getCookie() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
	}

	private void setListenerForView() {
		// TODO Auto-generated method stub
		iv_return.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
		cb_ad.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				String str = cb_ad.getText().toString();
				if (arg1) {
					reason.add(str);
				} else if (reason.contains(str)) {
					reason.remove(str);
				}
			}
		});
		cb_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				String str = cb_sex.getText().toString();
				if (arg1) {
					reason.add(str);
				} else if (reason.contains(str)) {
					reason.remove(str);
				}
			}
		});
		cb_head.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				String str = cb_head.getText().toString();
				if (arg1) {
					reason.add(str);
				} else if (reason.contains(str)) {
					reason.remove(str);
				}
			}
		});
		cb_other.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1)
					et_other.setEnabled(true);
				else
					et_other.setEnabled(false);
			}
		});
		cb_against.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				String str = cb_against.getText().toString();
				if (arg1) {
					reason.add(str);
				} else if (reason.contains(str)) {
					reason.remove(str);
				}
			}
		});
	}

	private void initViews() {
		// TODO Auto-generated method stub
		iv_return = (ImageView) findViewById(R.id.iv_return);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		cb_sex = (CheckBox) findViewById(R.id.cb_sex);
		cb_ad = (CheckBox) findViewById(R.id.cb_ad);
		cb_against = (CheckBox) findViewById(R.id.cb_against);
		cb_head = (CheckBox) findViewById(R.id.cb_head);
		cb_other = (CheckBox) findViewById(R.id.cb_other);
		et_other = (EditText) findViewById(R.id.et_other);
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.iv_return:
			BBSReportView.this.finish();
			break;

		case R.id.btn_commit:
			if (reason.size() != 0 || cb_other.isChecked()) {
				if (cb_other.isChecked()
						&& TextUtils.isEmpty(et_other.getText()) && et_other.getText().toString().length()==0) {
					AlertDialog.Builder build = new AlertDialog.Builder(this);
					build.setTitle("提示框")
							.setMessage("请在输入框内填入原因！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									});
					build.create().show();
				} else {
					reason.add(et_other.getText().toString());
					int size = reason.size() - 1;
					for (int i = 0; i < size; i++) {
						reportReason = reportReason + reason.get(i) + "*#";
					}
					reportReason = reportReason + reason.get(size);
					sendDataToServer();
				}
			}
			else {
				AlertDialog.Builder build = new AlertDialog.Builder(
						this);
				build.setTitle("提示框")
						.setMessage("请选择原因！")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {

									}
								});
				build.create().show();
			}

			break;
		}

	}

	private void sendDataToServer() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					BBSHttpClient client = new BBSHttpClient(cookie, subPath,
							REPORT, essayId, reportReason);
					String getResponse = client.getResponse();
					JSONObject object = new JSONObject(getResponse);
					response = object.getString("tip");
					Log.d(tag, "response==" + response);
					Message msg = new Message();
					if(response.equals("success")){
						msg.what = REPORT_SUCCESS;
						handler.sendMessage(msg);
					}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
