/**
 * 
 */
package com.atm.charonline.recuit.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.LogUtil;
import com.atm.charonline.bbs.util.SendDataToServer;
import com.atm.chatonline.bbs.activity.bbs.BBSPublishPostView;
import com.atm.chatonline.bbs.commom.ToastUtil;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.activity.bbs ---NavigationPublishPost
 * @作用 BBS导航条的发帖功能
 * @作者 陈小二
 * @时间 2015-8-18
 * 
 */
public class RecuitNavigationPublishPost extends BaseActivity implements
OnClickListener{
	private String work,type,worktype,location,phone,salary,info,response,getResponse,cookie;
	private Spinner spWork,spType;
	private EditText edWorkType,edLocation,edPhone,edInfo,edSalary;
	private TextView publish;
	private Button btnBack;
	private SendDataToServer send = new SendDataToServer();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recuit_publish_post);
		
		//初始化控件
		spWork = (Spinner) findViewById(R.id.sp_work);
		spType = (Spinner) findViewById(R.id.sp_type);
		edWorkType = (EditText) findViewById(R.id.txt_worktype);
		edLocation = (EditText) findViewById(R.id.txt_location);
		edPhone = (EditText) findViewById(R.id.txt_phone);
		edSalary = (EditText) findViewById(R.id.txt_salary);
		edInfo = (EditText) findViewById(R.id.txt_info);
		publish = (TextView) findViewById(R.id.btn_publsh);
		btnBack = (Button) findViewById(R.id.btn_back);
		
		//设置监听
		btnBack.setOnClickListener(this);
		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(edWorkType.getText().toString().equals("")) {
					LogUtil.d("工作类型为空");
					new ToastUtil().show(getApplication(), "工作类型不能为空");	
				}else {
					worktype = edWorkType.getText().toString();
					LogUtil.d("工作类型为" + worktype);
					if(edSalary.getText().toString().equals("")) {
						LogUtil.d("薪资为空");
						new ToastUtil().show(getApplication(), "薪资不能为空");	
					}else {
						salary = edSalary.getText().toString();
						LogUtil.p("薪资",salary);
					if(edPhone.getText().toString().equals("")) {
						LogUtil.d("联系方式为空");
						new ToastUtil().show(getApplication(), "联系方式不能为空");	
					}else {
						phone = edPhone.getText().toString();
						LogUtil.p("联系电话",phone);
						if(edLocation.getText().toString().equals("")) {
							LogUtil.d("地址为空");
							new ToastUtil().show(getApplication(), "地址不能为空");	
						}else {
					location = edLocation.getText().toString();
					LogUtil.p("地址",location);
						if(edInfo.getText().toString().equals("")) {
							LogUtil.d("简介为空");
							new ToastUtil().show(getApplication(), "简介不能为空");	
						}else {
							info = edInfo.getText().toString();
							LogUtil.p("简介",info);
							work = spWork.getSelectedItem().toString();
							LogUtil.p("招聘？求职",work);
							type = spType.getSelectedItem().toString();
							LogUtil.p("类型",type);
							sendDataToServer();// 将数据传给服务器
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}// 让主线程睡眠1秒，等待参数response

							LogUtil.d( "response:"+response);
							try{
								if (response.equals("success")) {
									Toast.makeText(RecuitNavigationPublishPost.this, "发布成功",
											Toast.LENGTH_SHORT).show();
									finish();
								} else {
									Toast.makeText(RecuitNavigationPublishPost.this, response,
											Toast.LENGTH_SHORT).show();
								}
							}catch(NullPointerException e){
								return ;
							}
						}}}
					}
				}
			}
			
		});
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
				params.put("work", work);
				params.put("reTypeName", type);
				params.put("woTypeName", worktype);
				params.put("salary", salary);
				params.put("telephone", phone);
				params.put("workAddress", location);
				params.put("reContent", info);		
				// 获取cookie
				SharedPreferences pref = getSharedPreferences("data",
						Context.MODE_PRIVATE);
				cookie = pref.getString("cookie", "");
				params.put("cookie",cookie);		

				getResponse = send.post(UriAPI.SUB_URL + "recuit_publish.action",
							params, null, cookie);
			
				try {
					JSONObject object = new JSONObject(getResponse);
					response = object.getString("tip");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		AlertDialog.Builder back = new AlertDialog.Builder(this);
		back.setTitle("提示框")
				.setMessage("退出当前编辑？")
				.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								RecuitNavigationPublishPost.this.finish();
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
	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			AlertDialog.Builder back = new AlertDialog.Builder(this);
			back.setTitle("提示框")
					.setMessage("退出当前编辑？")
					.setPositiveButton("退出",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									RecuitNavigationPublishPost.this.finish();
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
		}
	}
	/* (non-Javadoc)
	 * @see com.atm.chatonline.chat.ui.BaseActivity#processMessage(android.os.Message)
	 */
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
}
