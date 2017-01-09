package com.atm.chatonline.bbs.activity.bbs;

import java.io.Serializable;
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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AbsListView.LayoutParams;
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

import com.atm.chatonline.bbs.adapter.ExpressionPagerAdapter;
import com.atm.chatonline.bbs.adapter.PhotoAdapter;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.GridViewItemWithDelete;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.PhotoItem;
import com.atm.chatonline.bbs.util.SendDataToServer;
import com.atm.chatonline.chat.ui.AttentionActivity;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.activity.bbs ---BBSPublishPostView
 * @作用 该类用于显示发贴界面
 * @作者 钟YD
 * @时间 2015-8-24
 * 
 */
public class BBSPublishPostView extends BaseActivity implements OnClickListener {

	private LinearLayout ll_exp, ll_photo;
	private ImageView album, expression, photo_one, aite, iv_return,
			iv_addLable;
	private TextView next;
	private EditText title, content;
	private Spinner spinner;
	private String str_title, str_type, str_content;
	private String cookie, tag = "BBSPublishPostView", userID = BaseActivity
			.getSelf().getUserID();
	private static String response;
	// private InputMethodManager mInputMethodManager;
	private ViewPager exp_pager;
	private ExpressionPagerAdapter pagerAdapter;
	private List<View> view;
	private List<Map<String, Object>> listItems1, listItems2;
	private SimpleAdapter adapter1, adapter2;
	private GridView grid1, grid2;
	private View viewPager1, viewPager2;
	private boolean isFaceShow = false, isPhotoShow = false;
	private Resources res;
	private Bitmap myBitmap;
	private Uri uri;
	private static final String CHARSET = "utf-8"; // 设置编码
	// private byte[] mContent;
	private String subURL = UriAPI.SUB_URL;
	private String[] description1, description2, type;
	private SendDataToServer send = new SendDataToServer();
	private int contentCursor;
	private int[] imageIds1 = { R.drawable.exp01, R.drawable.exp2,
			R.drawable.exp3, R.drawable.exp4, R.drawable.exp5, R.drawable.exp6,
			R.drawable.exp7, R.drawable.exp8, R.drawable.exp9,
			R.drawable.exp10, R.drawable.exp11, R.drawable.exp12,
			R.drawable.exp13, R.drawable.exp14, R.drawable.exp15,
			R.drawable.exp16, R.drawable.exp17, R.drawable.exp18,
			R.drawable.exp19, R.drawable.delete, };
	private int[] imageIds2 = { R.drawable.exp22, R.drawable.exp23,
			R.drawable.exp24, R.drawable.exp25, R.drawable.exp26,
			R.drawable.exp27, R.drawable.exp28, R.drawable.exp29,
			R.drawable.exp30, R.drawable.exp31, R.drawable.exp32,
			R.drawable.exp33, R.drawable.delete, };

	private GridView gv_bottom;
	private List<PhotoItem> selectedPic = new ArrayList<PhotoItem>();
	private List<String> aiteID = new ArrayList<String>();
	private Context context;
	private PhotoAdapter select_adap;
	private PhotoItem photoItem;
	private int size;
	private static final int REQUEST_PHOTO = 1;
	// private static final int REQUEST_NEXT = 2;
	private static final int REQUEST_AITE = 3;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_publish_post);
		context = getApplicationContext();
		initView();
		getArray();// 从资源文件夹下获取已定义好的数组
		// mInputMethodManager = (InputMethodManager)
		// getSystemService(INPUT_METHOD_SERVICE);
		accomplishExpBoard();// 实现表情面板
		setListenerForViews();
		setAdapterForPhotos();
		setParamsForGridView();
		setCookie();// 获取cookie

	}

	private void setAdapterForPhotos() {
		// TODO Auto-generated method stub
		photoItem = new PhotoItem(R.drawable.add_pic, 1);
		selectedPic.add(photoItem);
		select_adap = new PhotoAdapter(context, null, selectedPic) {
			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return 2;
			}

			@Override
			public int getItemViewType(int position) {
				// TODO Auto-generated method stub
				return selectedPic.get(position).getFlag();
			}

			@Override
			public View getView(final int position, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				GridViewItemWithDelete grid = null;
				if (selectedPic.get(position).getFlag() == 1) {
					Log.d(tag, "selectedPic.size()==" + selectedPic.size()
							+ ",positon==" + position);
					grid = new GridViewItemWithDelete(context, true);
					grid.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT, arg2.getHeight() - 10));
					grid.iv_photo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent albumIntent = new Intent(
									BBSPublishPostView.this,
									BBSSelectPhotosView.class);
							startActivityForResult(albumIntent, REQUEST_PHOTO);
						}
					});
					return grid;
				} else {
					if (arg1 == null && getItemViewType(position) == 0) {
						grid = new GridViewItemWithDelete(context);
						grid.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								arg2.getHeight() - 10));
						
					} else {
						grid = (GridViewItemWithDelete) arg1;
						grid.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								arg2.getHeight() - 10));
					
					}
					grid.iv_delete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method
							// stub
							Toast.makeText(context, "Delete",
									Toast.LENGTH_SHORT).show();
							selectedPic.remove(position);
							Log.d(tag, "position=" + position);
							select_adap.notifyDataSetChanged();
						}
					});

				}

				Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
						context.getContentResolver(), selectedPic.get(position)
								.getPhotoID(), Thumbnails.MICRO_KIND, null);
				grid.mySetBitmap(bitmap);
				return grid;
			}
		};

		gv_bottom.setAdapter(select_adap);
	}

	private void setParamsForGridView() {
		// TODO Auto-generated method stub
		size = selectedPic.size();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		Log.d(tag, "density==" + density);
		int allWidth = (int) (160 * size * density);
		int itemWidth = (int) (100 * density);
		Log.d(tag, "itemWidth==" + itemWidth);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		gv_bottom.setLayoutParams(params);
		gv_bottom.setColumnWidth(itemWidth);
		Log.d(tag, "gv_bottom.height ==" + gv_bottom.getHeight());
		gv_bottom.setNumColumns(size);
	}

	private void setCookie() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
	}

	private void setListenerForViews() {
		// TODO Auto-generated method stub
		album.setOnClickListener(this);
		expression.setOnClickListener(this);
		next.setOnClickListener(this);
		aite.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		title.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (isFaceShow) {
					ll_exp.setVisibility(View.GONE);
					isFaceShow = false;
				}
				return false;
			}
		});
		content.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (isFaceShow) {
					ll_exp.setVisibility(View.GONE);
					isFaceShow = false;
				}
				return false;
			}
		});
		// 类型的监听事件
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				str_type = type[position];
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// 点了面板中的表情
		grid1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogUtil.p(tag, "点grid1");
				// TODO Auto-generated method stub
				int index1 = Math.max(content.getSelectionStart(), 0);
				LogUtil.p(tag, "index1：" + index1);
				String oriContent1 = content.getText().toString();
				LogUtil.p(tag, "oriContent1：" + oriContent1);
				StringBuilder sBuilder1 = new StringBuilder(oriContent1);

				if (arg2 == 20) {// “删除”对应的下标
					if (content.getSelectionStart() > 0) {
						int selection = content.getSelectionStart();
						String text2 = oriContent1.substring(selection - 1,
								selection);
						Log.d(tag, "text2==" + text2);
						if (")".equals(text2)) {// 当删的是表情的时候，整块删掉
							int start = oriContent1.lastIndexOf("#");
							if (oriContent1.charAt(start + 1) == '(') {
								int end = selection;
								content.getText().delete(start, end);
							} else
								content.getText().delete(selection - 1,
										selection);
						} else {
							content.getText().delete(selection - 1, selection);
						}
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
				LogUtil.p(tag, "点grid2");
				// TODO Auto-generated method stub
				int index2 = Math.max(content.getSelectionStart(), 0);
				String oriContent2 = content.getText().toString();
				StringBuilder sBuilder2 = new StringBuilder(oriContent2);
				if (arg2 == 12) {
					if (content.getSelectionStart() > 0) {
						int selection = content.getSelectionStart();
						String text2 = oriContent2.substring(selection - 1,
								selection);
						Log.d(tag, "text2==" + text2);
						if (")".equals(text2)) {// 当删的是表情的时候，整块删掉
							int start = oriContent2.lastIndexOf("#");
							if (oriContent2.charAt(start + 1) == '(') {
								int end = selection;
								content.getText().delete(start, end);
							} else
								content.getText().delete(selection - 1,
										selection);
						} else {
							content.getText().delete(selection - 1, selection);
						}
					}
				} else {
					sBuilder2.insert(index2, description2[arg2]);
					content.setText(sBuilder2.toString());
					content.setSelection(index2 + description2[arg2].length());
				}

			}
		});

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
		grid1.setAdapter(adapter1);// 如果注释，有第一页，但没有表情
		grid2.setAdapter(adapter2);

		view = new ArrayList<View>();
		view.add(viewPager1);// 如果注释，就没有第一页，表情就不可能有
		view.add(viewPager2);
		pagerAdapter = new ExpressionPagerAdapter(view);
		exp_pager.setAdapter(pagerAdapter);
	}

	// 从资源文件夹下获取已定义好的数组
	private void getArray() {
		// TODO Auto-generated method stub
		res = BBSPublishPostView.this.getResources();
		type = res.getStringArray(R.array.type);
		description1 = res.getStringArray(R.array.description1);
		description2 = res.getStringArray(R.array.description2);
	}

	// arg0 = requestCode请求码,arg1 = resultCode结果码
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		// ContentResolver contentResolver = getContentResolver();
		switch (arg0) {
		case REQUEST_AITE:
			if (arg1 == RESULT_OK) {// 从关注界面获得数据
				if (arg2.getStringExtra("friendID") != null) {
					aiteID.add(arg2.getStringExtra("friendID"));
					// i++;
					contentCursor = content.getSelectionStart();// 获取文本当前所在光标
					content.getText().insert(contentCursor,
							"@" + arg2.getStringExtra("nickName") + " ");// 在当前光标处插入文本
				}
			}
			break;
		case REQUEST_PHOTO:
			// 从相册返回的数据
			if (arg2 != null) {
				try {
					List<PhotoItem> temp_selectedPic = (ArrayList<PhotoItem>) arg2
							.getSerializableExtra("selectedPic");
					selectedPic
							.addAll(selectedPic.size() - 1, temp_selectedPic);
					select_adap.notifyDataSetChanged();
					setParamsForGridView();
					isPhotoShow = true;
				} catch (Exception e) {
					// TODO Auto-generatedcatch block
					e.printStackTrace();
				}
			}
			break;
		}
	}

	// 初始化控件
	@SuppressWarnings("deprecation")
	private void initView() {
		// TODO Auto-generated method stub
		next = (TextView) findViewById(R.id.next);
		album = (ImageView) findViewById(R.id.album);
		aite = (ImageView) findViewById(R.id.aite);
		expression = (ImageView) findViewById(R.id.expression);
		ll_exp = (LinearLayout) findViewById(R.id.ll_expression);
		spinner = (Spinner) findViewById(R.id.spinner);
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);
		exp_pager = (ViewPager) findViewById(R.id.exp_pager);
		photo_one = (ImageView) findViewById(R.id.photo_one);
		ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		gv_bottom = (GridView) findViewById(R.id.gv_bottom);
		iv_addLable = new ImageView(context);
		iv_addLable.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.add_pic));
		iv_addLable.setLayoutParams(new LayoutParams(125, 125));

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.album:// 相册
			if (!isPhotoShow) {
				if (isFaceShow)
					ll_exp.setVisibility(View.GONE);
				ll_photo.setVisibility(View.VISIBLE);
				isPhotoShow = true;
			} else {
				ll_photo.setVisibility(View.GONE);
				isPhotoShow = false;
			}
			break;
		case R.id.expression:
			if (!isFaceShow) {
				if (isPhotoShow)
					ll_photo.setVisibility(View.GONE);
				/*
				 * mInputMethodManager.hideSoftInputFromWindow(
				 * content.getWindowToken(), 0);
				 */
				// try {
				// Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				ll_exp.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				ll_exp.setVisibility(View.GONE);
				isFaceShow = false;
			}
			break;
		case R.id.aite:
			Intent aiteIntent = new Intent(BBSPublishPostView.this,
					AttentionActivity.class);
			aiteIntent.putExtra("userID", userID);
			aiteIntent.putExtra("fromActivity", Config.BBSPOSTDETAILVIEW);
			startActivityForResult(aiteIntent, REQUEST_AITE);
			break;
		case R.id.iv_return:
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("提示框")
					.setMessage("确定退出当前编辑贴？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									BBSPublishPostView.this.finish();
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
			build.create().show();
			break;
		case R.id.next:
			if (!title.getText().toString().equals("")
					&& !content.getText().toString().equals("")) {
				Intent departmentIntent = new Intent(BBSPublishPostView.this,
						BBSChooseDepartmentView.class);
				str_title = title.getText().toString();
				str_content = content.getText().toString();
				departmentIntent.putExtra("str_title", str_title);
				departmentIntent.putExtra("str_content", str_content);
				departmentIntent.putExtra("str_type", str_type);
				if (!aiteID.isEmpty())
					departmentIntent.putStringArrayListExtra("aiteID",
							(ArrayList<String>) aiteID);
				if (!selectedPic.isEmpty()) {
					departmentIntent.putExtra("selectedPic",
							(Serializable) selectedPic);
				}
				startActivity(departmentIntent);
			} else {
				// 标题或者内容为空时，弹出提示框
				AlertDialog.Builder nextBuild = new AlertDialog.Builder(this);
				nextBuild
						.setTitle("提示框")
						.setMessage("请完善所有内容再点击下一步")
						.setNeutralButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
				nextBuild.create().show();
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
				params.put("aiteID", aiteID);
				params.put("selectedPic", selectedPic);
				// 传图片
				ArrayList<Bitmap> list = new ArrayList<Bitmap>();

				if (myBitmap != null) {

					list.add(myBitmap);
					Bitmap[] files = (Bitmap[]) list.toArray(new Bitmap[list
							.size()]);
					getResponse = send.post(subURL + "essay/publish.do",
							params, files, cookie);
				} else {
					getResponse = send.post(subURL + "essay/publish.do",
							params, null, cookie);
				}
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

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根

	}
}
