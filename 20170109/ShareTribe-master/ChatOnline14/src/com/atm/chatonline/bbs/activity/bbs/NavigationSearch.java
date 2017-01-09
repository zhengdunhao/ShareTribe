/**
 * 
 */
package com.atm.chatonline.bbs.activity.bbs;

import java.util.ArrayList;

import com.atm.chatonline.bbs.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;

/**
 * @类 com.test.BBS ---BBSSearch
 * @作用
 * @作者 陈小二
 * @时间 2015-8-21
 * 
 */
@SuppressLint("ResourceAsColor")
public class NavigationSearch extends BaseActivity implements
	OnClickListener,OnTouchListener{
	
		private RadioGroup radioGroup;
		private String title[] = { "帖子", "用户" };
		private LinearLayout linearLayout;
		
		private ArrayList<TextView> textViews;
		private ViewPager viewPager;
		private ArrayList<View> pageViews;
		private HorizontalScrollView horizontalScrollView;
		private TextView textView;
		private EditText searchText;
		private Button btnBack;
		private String [] relativePath = {"atm_searchEssay.action","atm_searchPeople.action"};
		private int width=0;
		private int nums=0;

		private InputMethodManager mInputMethodManager;
		private String searchInfo;
		private View view01;
		private View view02;
		private ExtendsIntent[] intents;
		private ScrollPageViewAdapter viewAdapter;

		private String tag="SearchActivity";

		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.bbs_search_view);
			intents = new ExtendsIntent[20];
			
			width = getWindowManager().getDefaultDisplay().getWidth();//获取屏幕的长度
			mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			
			linearLayout = (LinearLayout) findViewById(R.id.tab_view);// 获取bbs_main_view的子布局，即标题栏下面的布局
			viewPager = (ViewPager) findViewById(R.id.pager);
			horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
			
			horizontalScrollView.setPadding(width/3, 0, 0, 0);//让活动栏居中，width/3是根据屏幕的长度以及按钮的长度算出
			InItView();
			Log.i(tag,"ChatMainActivity--------已经加载了GroupListActivity和FriendListActivity");
			InItTitle();
			setSelector(0);
			
			btnBack = (Button)findViewById(R.id.btn_back);
			btnBack.setOnClickListener(this);

			viewAdapter = new ScrollPageViewAdapter(pageViews);
			viewPager.setAdapter(viewAdapter);// 添加之后可以屏幕左右滑动
			viewPager.clearAnimation();
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {// 这个是点击不同卡片，跳转不同的页面
//
//						@Override
						public void onPageSelected(int arg0) {
							
//							choose=arg0;
							setSelector(arg0);
//							Log.i(tag, "我onPageSelected被点击了");
//							
//							if(flag==true){
//								if(click==0){
//									Log.i(tag, "onPageSelected----choose:"+choose);
//									new Thread(runnable).start();
//									
//									
//								}else if(click==1){
//									Log.i(tag, "onPageSelected----choose:"+choose);
//									new Thread(runnable).start();
//									
//								}
//							}
							
							
						}

						@Override
						public void onPageScrolled(int arg0, float arg1, int arg2) {
							
						}

						@Override
						public void onPageScrollStateChanged(int arg0) {
							
						}
					});
			
			viewPager.setOnClickListener(this);
			//滑动切换选项卡时，系统自带的软件盘会隐藏
			viewPager.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					closeInput(NavigationSearch.this.getCurrentFocus());//系统自带的软件盘会隐藏
					return false;
				}
				
			});
			
		
			searchText = (EditText)findViewById(R.id.bbs_search_edit);
			
			searchText.setOnTouchListener(this);
			Log.i(tag, "999999999");
			
			//监听搜索框
			searchText.setOnEditorActionListener(new OnEditorActionListener(){
				public boolean onEditorAction(TextView v,int actionId,KeyEvent event){
					if(actionId == EditorInfo.IME_ACTION_SEARCH){ //判断软键盘是否点了搜索按钮，如果是则启动runnable，把内容发给后台，并同时把软键盘给隐藏
						closeInput(v);
						searchInfo = searchText.getText().toString();
						LogUtil.p(tag,
								"nums--------"+nums);
						if(nums!=0) {
//							pageViews.clear();
//							viewAdapter.notifyDataSetChanged();
//							
//							LogUtil.p(tag,
//									"nums--------pageViews.clear();"+nums);
//							if(pageViews.isEmpty())
//							LogUtil.p("pageViews.isEmpty()",
//									"==true");

							 Message msg4 = new Message();
							 msg4.what = Config.SEARCHESSAY;
							 BBSListView.setId(searchInfo);
							 BBSListView.getHandler().sendMessage(msg4);

							 Message msg5 = new Message();
							 msg5.what = Config.SEARCHESSAY;
							 SearchUserView.setId(searchInfo);
							 SearchUserView.getHandler().sendMessage(msg5);
							
						}else {
						LogUtil.p(tag,
								"view01 nums--------"+nums);
							view01 = getLocalActivityManager().startActivity(
									"SearchActivity"+nums, new Intent(getExtendsIntent(0))).getDecorView();
							nums++;
							LogUtil.p(tag,
									"view02 nums--------"+nums);
							view02 = getLocalActivityManager().startActivity(
									"SearchFrinedListActivity"+nums,new Intent(getExtendsIntent(1))).getDecorView();
							LogUtil.p(tag,
								"ChatMainActivity--------开始加载SearchGroupListActivity和SearchFriendListActivity");

							LogUtil.p(tag,
								"pageViews.isEmpty()nums--------"+pageViews.isEmpty());
						pageViews.add(view01);
						pageViews.add(view02);
							LogUtil.p(tag,
								"pageViews.isEmpty()nums--------"+pageViews.isEmpty());
						viewAdapter.notifyDataSetChanged();
						nums++;
						LogUtil.p(tag,
								"nums--------"+nums);}
						return true;
					}
					return false;
				}
			});
			
		}
		public ExtendsIntent getExtendsIntent(int i) {
			Log.d("顺序","getExtendsIntent");
			if(i==0)
			intents[nums] = new ExtendsIntent(this,BBSListView.class,
					searchInfo, relativePath[i], "" , 1);
			else 
				intents[nums] = new ExtendsIntent(this,SearchUserView.class,
						searchInfo, relativePath[i], "" , 1);
			return intents[nums];
		}
		//点击标题栏，软键盘会隐藏
		public boolean onTouchEvent(MotionEvent event){
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				if(NavigationSearch.this.getCurrentFocus()!=null){
					if(NavigationSearch.this.getCurrentFocus().getWindowToken()!=null){
						closeInput(NavigationSearch.this.getCurrentFocus());
						LogUtil.p(tag, "状态栏被点击");
					}
				}
			}
			return super.onTouchEvent(event);
		}

		void InItView() {
//			String searchInfo = searchText.getText().toString();
//			ExtendsIntent intent1 = new ExtendsIntent(this,NavigationSearch.class,
//					searchInfo, relativePath[0], "" , 1);
//			ExtendsIntent intent2 = new ExtendsIntent(this,NavigationSearch.class,
//					searchInfo, relativePath[1], "" , 1);
			pageViews = new ArrayList<View>();
//			view01 = getLocalActivityManager().startActivity(
//					"SearchGroupListActivity", new Intent(this,NavigationSearchPost.class)).getDecorView();
//			view02 = getLocalActivityManager().startActivity(
//					"SearchFrinedListActivity",new Intent(this,NavigationSearchUser.class)).getDecorView();
//			Log.i(tag,
//					"ChatMainActivity--------开始加载SearchGroupListActivity和SearchFriendListActivity");
//			pageViews.add(view01);
//			pageViews.add(view02);
			
		}

		@SuppressLint("ResourceAsColor")
		void InItRadioButton() {
			int width = getWindowManager().getDefaultDisplay().getWidth() / 6;// 求平均长度
			for (int i = 0; i < title.length; i++) {
				RadioButton radioButton = new RadioButton(this, null,
						R.style.radioButton);
				radioButton.setTextSize(17);
				radioButton
						.setTextColor(R.color.black);
				radioButton.setWidth(width);
				radioButton.setGravity(Gravity.CENTER);
				radioGroup.addView(radioButton);
			}
		}

		@SuppressLint("ResourceAsColor")
		void InItTitle() {
			textViews = new ArrayList<TextView>();
			int width = getWindowManager().getDefaultDisplay().getWidth() / 6;
			int height = 70;
			for (int i = 0; i < title.length; i++) {
				textView = new TextView(this);
				textView.setText(title[i]);
				textView.setTextSize(17);
				textView.setTextColor(R.color.black);
				textView.setWidth(width);
				textView.setGravity(Gravity.CENTER);
				textView.setId(i);
				textView.setOnClickListener(new TextView.OnClickListener() {// 创建监听
					public void onClick(View v) {
						setSelector(v.getId());
					}

				}); // 每次点击textview都有触发响应，都会调用setSelector(int
					// id),id为多少，就跳到哪个textview
				textViews.add(textView);
				// 分割线

				// 我个人认为是这样的，子视图向父视图说明需要在父视图里面占多大。子视图：textview，父视图：是个布局LinearLayout
				// textview向入驻LinearLayout就要通过LayoutParam来传递大小。

				View view = new View(this);
				LinearLayout.LayoutParams layoutParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.width = 1;
				layoutParams.height = height - 40;
				layoutParams.gravity = Gravity.CENTER;
				view.setLayoutParams(layoutParams);
				
				view.setBackgroundColor(R.color.gray);
				linearLayout.addView(textView);
				

			}
		}
		public void setSelector(int id) {
			for (int i = 0; i < title.length; i++) {
				if (id == i) {
//					Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//							R.drawable.grouplist_item_bg_normal1);
//					textViews.get(id).setBackgroundDrawable(
//							new BitmapDrawable(bitmap));
					textViews.get(id).setBackgroundResource(R.drawable.radiobutton);
//					textViews.get(id).setBackgroundColor(Color.GREEN);
					
					textViews.get(id).setTextColor(Color.WHITE);//选中则显示绿色
					
					
					viewPager.setCurrentItem(i);
					
					
				}

				else {
					textViews.get(i).setBackgroundDrawable(new BitmapDrawable());
					textViews.get(i).setTextColor(R.color.black);
				}
			}
		}
//		
//		@SuppressLint("ResourceAsColor")
//		Runnable runnable = new Runnable(){	//只对在输入法按搜索按钮有效，对直接按卡片切换无效
//			@SuppressLint("ResourceAsColor")
//			public void run(){
//				String searchTxt = searchText.getText().toString();
//				Log.i(tag, "搜索的内容是:"+searchTxt);
//				if(!searchTxt.equals("")){
//				if(choose==0){		//找群
////					try {
////						con.findCrowd(searchTxt);
////					} catch (IOException e) {
//						// TODO Auto-generated catch block
////						e.printStackTrace();
////					}
//					click=1;
//				}else if(choose==1){	//找人
////					con.findUser(searchTxt);
//					click=0;
//				}
//				Log.i(tag, "searchTxt:"+searchTxt);
//				}
//				
//			}
//		};
		
//		
		public void processMessage(Message msg){
//			if(msg.what==1){
//				Bundle bundle =msg.getData();
//				ArrayList  groupList = bundle.getParcelableArrayList("groupList");
//				ArrayList<Group> listGroup = (ArrayList<Group>)groupList.get(0);
//				if(listGroup.size()!=0){
//					Log.i(tag, "Bundle传递ArrayList是成功的");
//				}
//				
//				if(tabActivity.getLocalActivityManager().getActivity("SearchGroupListActivity")!=null){
//					Log.i(tag, "SearchGroupListActivity不为空");
//					SearchGroupListActivity group = (SearchGroupListActivity)tabActivity.getLocalActivityManager().getActivity("SearchGroupListActivity");
//					group.showGroupList(listGroup);
//				}
//			}
//			
		}
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			
			case R.id.search_edit:    //输入框触摸实现
				mInputMethodManager.showSoftInput(searchText, 0);
				Log.i(tag, "333333333333333");
				break;
			}
			return false;
		}
		
		

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_back:
				NavigationSearch.this.onBackPressed();
				break;
//			case R.id.pager:
//				Log.i(tag, "被点了");
//				break;

			}
		}
		
		//这个貌似用不到
		public void switchInput(View v){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
		//显示软键盘
		public void openInput(View v){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(findViewById(R.id.search_edit), InputMethodManager.SHOW_FORCED);
		}
		//隐藏软键盘
		public boolean closeInput(View v){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
		
	}
