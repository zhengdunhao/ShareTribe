package com.atm.charonline.recuit.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.activity.bbs.BBSChooseDepartmentView;
import com.atm.chatonline.bbs.adapter.ExpressionPagerAdapter;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.charonline.recuit.ui ---RecuitPublishPostView
 * @作用 该类用于显示发贴界面
 * @作者 LT
 * @时间 2015-8-24
 * 
 */
public class RecuitPublishPostView extends BaseActivity implements OnClickListener {

	private LinearLayout ll_function, ll_exp;
	private ImageView album, department, expression;
	private TextView sendPost,applyType,applyDirection,applySalary,applyInformation;
	private EditText title, content, information;
	private Uri imageUri;
	private Spinner spinner,spWork;
	private String str_title, str_department, str_type, str_label = "",
			str_content;
	private String cookie;
	private InputMethodManager mInputMethodManager;
	private ViewPager exp_pager;
	private ExpressionPagerAdapter pagerAdapter;
	private List<View> view;
	private List<Map<String, Object>> listItems1, listItems2;
	private SimpleAdapter adapter1, adapter2;
	private GridView grid1, grid2;
	private View viewPager1, viewPager2;
	private boolean isFaceShow = false;
	private Resources res;
	private String[] description1, description2, type;
	private int[] imageIds1 = { R.drawable.exp1, R.drawable.exp2,
			R.drawable.exp3, R.drawable.exp4, R.drawable.exp5, R.drawable.exp6,
			R.drawable.exp7, R.drawable.exp8, R.drawable.exp9,
			R.drawable.exp10, R.drawable.exp11, R.drawable.exp12,
			R.drawable.exp13, R.drawable.exp14, R.drawable.exp15,
			R.drawable.exp16, R.drawable.exp17, R.drawable.exp18,
			R.drawable.exp19,  R.drawable.delete, };
	private int[] imageIds2 = { R.drawable.exp22, R.drawable.exp23,
			R.drawable.exp24, R.drawable.exp25, R.drawable.exp26,
			R.drawable.exp27, R.drawable.exp28, R.drawable.exp29,
			R.drawable.exp30, R.drawable.exp31, R.drawable.exp32,
			R.drawable.exp33, R.drawable.delete, };

	public static final int CROP_PHOTO = 2;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recuit_publish_post);

		initView();
		getArray();// 从资源文件夹下获取已定义好的数组

		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		accomplishExpBoard();// 实现表情面板

		initEvent();

		// 获取cookie
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		// 类型的监听事件
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				str_type = type[position];
				LogUtil.d("111111111");
				LogUtil.d(str_type);
				if(str_type.equals("求职")) {
					LogUtil.d(str_type+"1");
					applyType.setText("求职类型");
					applyDirection.setText("求职方向");
					applySalary.setText("期望薪资");
					applyInformation.setText("个人介绍");
					information.setHint("请填写个人介绍以及相关经历或项目经验。");
					
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		spWork.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				str_type = type[position];
				LogUtil.d("111111111");
				LogUtil.d(str_type);
				if(str_type.equals("求职")) {
					LogUtil.d(str_type+"1");
					applyType.setText("求职类型");
					applyDirection.setText("求职方向");
					applySalary.setText("期望薪资");
					applyInformation.setText("个人介绍");
					information.setHint("请填写个人介绍以及相关经历或项目经验。");
					
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	//设置监听事件
	private void initEvent() {
		// TODO Auto-generated method stub
		album.setOnClickListener(this);
		expression.setOnClickListener(this);
		department.setOnClickListener(this);
		sendPost.setOnClickListener(this);
	}

	// 实现表情面板
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
				int index1 = Math.max(content.getSelectionStart(), 0);
				String oriContent1 = content.getText().toString();
				StringBuilder sBuilder1 = new StringBuilder(oriContent1);
				if (arg2 == 20) {
					if (content.getSelectionStart() > 0) {
						int selection = content.getSelectionStart();
						String text2 = oriContent1.substring(selection - 1);
						if (")".equals(text2)) {
							int start = oriContent1.lastIndexOf("#");
							int end = selection;
							content.getText().delete(start, end);
						}
						// input.getText().delete(selection - 1, selection);
					}
				} else {
					sBuilder1.insert(index1, description1[arg2]);
					content.setText(sBuilder1.toString());
					content.setSelection(index1 + description1[arg2].length());
				}
			}
		});

		grid2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int index2 = Math.max(content.getSelectionStart(), 0);
				String oriContent2 = content.getText().toString();
				StringBuilder sBuilder2 = new StringBuilder(oriContent2);
				if (arg2 == 12) {
					if (content.getSelectionStart() > 0) {
						int selection = content.getSelectionStart();
						String text2 = oriContent2.substring(selection - 1);
						if (")".equals(text2)) {
							int start = oriContent2.lastIndexOf("#");
							int end = selection;
							content.getText().delete(start, end);
						}
						// input.getText().delete(selection - 1, selection);
					}
				} else {
					sBuilder2.insert(index2, description2[arg2]);
					content.setText(sBuilder2.toString());
					content.setSelection(index2 + description2[arg2].length());
				}

			}
		});
	}

	// 从资源文件夹下获取已定义好的数组
	private void getArray() {
		// TODO Auto-generated method stub
		res = RecuitPublishPostView.this.getResources();
		type = res.getStringArray(R.array.type);
		description1 = res.getStringArray(R.array.description1);
		description2 = res.getStringArray(R.array.description2);
	}

	// 接收从ChooseDepartmentView返回的数据
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			if (arg1 == 0) {
				str_department = arg2.getStringExtra("department");
			}
			break;
		}
	}

	// 将数据传给服务器
	private void jsonDemo() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				BBSConnectNet get = new BBSConnectNet(str_type, str_title,
						str_content, str_label, str_department, cookie);
			}
		}).start();

	}

	//初始化控件
	private void initView() {
		// TODO Auto-generated method stub
		sendPost = (TextView) findViewById(R.id.btn_publsh);
		album = (ImageView) findViewById(R.id.album);
		department = (ImageView) findViewById(R.id.department);
		expression = (ImageView) findViewById(R.id.expression);
		ll_function = (LinearLayout) findViewById(R.id.ll_function);
		ll_exp = (LinearLayout) findViewById(R.id.ll_expression);
		spinner = (Spinner) findViewById(R.id.spinner);
		spWork = (Spinner) findViewById(R.id.sp_work);
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);
		exp_pager = (ViewPager) findViewById(R.id.exp_pager);
		
		
		applyType = (TextView) findViewById(R.id.tv_type);
		applyDirection = (TextView) findViewById(R.id.tv_direction);
		applySalary = (TextView) findViewById(R.id.tv_salary);
		applyInformation = (TextView) findViewById(R.id.tv_information);
		information = (EditText) findViewById(R.id.txt_info);
				
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.album:
			File outputImage = new File(
					Environment.getExternalStorageDirectory(),
					"output_image.jpg");
			try {
				if (outputImage.exists()) {
					outputImage.delete();
				}
				outputImage.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			imageUri = Uri.fromFile(outputImage);
			Intent albumIntent = new Intent("android.intent.action.GET_CONTENT");
			albumIntent.setType("image/*");
			albumIntent.putExtra("crop", true);
			albumIntent.putExtra("scale", true);
			albumIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(albumIntent, CROP_PHOTO);
			break;
		case R.id.expression:
			if (!isFaceShow) {
				mInputMethodManager.hideSoftInputFromWindow(
						content.getWindowToken(), 0);
				try {
					Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ll_exp.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				ll_exp.setVisibility(View.GONE);
				isFaceShow = false;
			}
			break;
		case R.id.department:
			Intent departmentIntent = new Intent(RecuitPublishPostView.this,
					BBSChooseDepartmentView.class);
			startActivityForResult(departmentIntent, 0);
			break;
		case R.id.btn_publsh:
			if (title.getText().toString() != ""
					&& content.getText().toString() != "") {
				Toast.makeText(RecuitPublishPostView.this, "发帖成功",
						Toast.LENGTH_SHORT).show();
				str_title = title.getText().toString();
				str_content = content.getText().toString();
				jsonDemo();// 将数据传给服务器
				finish();

			} else {
				// 标题或者内容为空时，弹出提示框
				AlertDialog.Builder build = new AlertDialog.Builder(this);
				build.setTitle("提示框")
						.setMessage("请完善所有内容再点击发贴")
						.setNeutralButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}

								});
				build.create().show();
			}
			break;
		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}
}
