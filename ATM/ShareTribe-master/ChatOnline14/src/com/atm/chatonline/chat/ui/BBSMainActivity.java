package com.atm.chatonline.chat.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atm.chatonline.chat.adapter.ScrollPageViewAdapter;
import com.example.studentsystem01.R;




@SuppressLint("ResourceAsColor")
public class BBSMainActivity extends ActivityGroup implements OnClickListener{
//	private SlidingMenu slidingMenu;
	private RadioGroup radioGroup;
	private String title[] = { "发现", "院系","关注" };
	private LinearLayout linearLayout;
//	private final int height = 70;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private HorizontalScrollView horizontalScrollView;
	private ImageView imgViewMore;
	private TextView textView;
	private String tag="";
	
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "ChatMainActivity--------即将进入");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_main_view);
		linearLayout = (LinearLayout) findViewById(R.id.tab_view);//获取bbs_main_view的子布局，即标题栏下面的布局
		viewPager = (ViewPager) findViewById(R.id.pager);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		InItView();
		Log.i(tag, "ChatMainActivity--------已经加载了GroupListActivity和FriendListActivity");
		InItTitle();
		setSelector(0);
//		InItSlidingMenu();
		
		imgViewMore=(ImageView)findViewById(R.id.imgView_more);
		imgViewMore.setOnClickListener(this);
		
		viewPager.setAdapter(new ScrollPageViewAdapter(pageViews));//添加之后可以屏幕左右滑动
		viewPager.clearAnimation();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {//这个是点击不同卡片，跳转不同的页面

			@Override
			public void onPageSelected(int arg0) {
				setSelector(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	/**
	 *初始化滑动菜单
	 * 
	 * */
//	
//	void InItSlidingMenu(){
//		slidingMenu = new SlidingMenu(this);
//        slidingMenu.setMode(SlidingMenu.LEFT);
//        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
//        slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//设置触摸范围 TOUCHMODE_FULLSCREEN（全局） 
//        slidingMenu.attachToActivity(this, SlidingMenu.RIGHT);//SLIDING_CONTENT
//        slidingMenu.setMenu(R.layout.left_sliding_menu_view);
//	}
    
	/***
	 * 初始化小视图，即发现，院系，关注等视图
	 * 加载到pageViews
	 */
	void InItView() {	//丹霞，这里你自己要创建三个子类，分别是发现，院系，以及关注，这里我先用群聊和私聊来代替
		
		pageViews = new ArrayList<View>();
		View view01 = getLocalActivityManager().startActivity("GroupListActivity",
				new Intent(this, GroupListActivity.class)).getDecorView();
		
		View view02 = getLocalActivityManager().startActivity("FrinedListActivity",
				new Intent(this, FriendListActivity.class)).getDecorView();
		Log.i(tag, "ChatMainActivity--------开始加载GroupListActivity和FriendListActivity");
		
		View view03 = getLocalActivityManager().startActivity("FrinedListActivity",
				new Intent(this, FriendListActivity.class)).getDecorView();
		Log.i(tag, "ChatMainActivity--------开始加载GroupListActivity和FriendListActivity");
		
		
		pageViews.add(view01);
		pageViews.add(view02);
		pageViews.add(view03);
		
	}
    /***
     * 初始化radiobutton，设置每个radiobutton的长度，当刚开始初始化时，第一个角标即发现被初始化为红色，其余为灰色
     * 把所有radiobutton放在radioGroup上
     */
	@SuppressLint("ResourceAsColor")
	void InItRadioButton() {
		int width = getWindowManager().getDefaultDisplay().getWidth() / 3;//求平均长度
		for (int i = 0; i < title.length; i++) {
			RadioButton radioButton = new RadioButton(this, null,
					R.style.radioButton);
																	//radioButton.setText(title[i]);这里我省略了
			radioButton.setTextSize(17);
			radioButton.setTextColor(R.color.black);
			radioButton.setWidth(width);
//			radioButton.setHeight(height);
			radioButton.setGravity(Gravity.CENTER);
			radioGroup.addView(radioButton);
		}
	}
	 /***
     * init title
     * 
     */
	@SuppressLint("ResourceAsColor")
	void InItTitle() {
		textViews = new ArrayList<TextView>();
		int width = getWindowManager().getDefaultDisplay().getWidth() / 3;
		int height = 70;
		for (int i = 0; i < title.length; i++) {
			textView = new TextView(this);
			textView.setText(title[i]);
			textView.setTextSize(17);
			textView.setTextColor(R.color.black);
			textView.setWidth(width);
//			textView.setHeight(height - 10);
			textView.setGravity(Gravity.CENTER);
			textView.setId(i);
			textView.setOnClickListener(new TextView.OnClickListener(){//创建监听    
	            public void onClick(View v) {    
	            	setSelector(v.getId());
	            }    
	  
	        });  //每次点击textview都有触发响应，都会调用setSelector(int id),id为多少，就跳到哪个textview
			textViews.add(textView);
			// 分割线
			
			//我个人认为是这样的，子视图向父视图说明需要在父视图里面占多大。子视图：textview，父视图：是个布局LinearLayout
			//textview向入驻LinearLayout就要通过LayoutParam来传递大小。
			
			View view = new View(this);
			LinearLayout.LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.width = 1;
			layoutParams.height = height - 40;
			layoutParams.gravity = Gravity.CENTER;
			view.setLayoutParams(layoutParams);
			view.setBackgroundColor(R.color.gray);
			linearLayout.addView(textView);
//			if (i != title.length - 1) {
//				linearLayout.addView(view);
//			}

		}
	}

	/***
	 * 选中效果
	 * 个人觉得，这个是起初始化作用，所以一般先赋角标为0的textview，然后角标为0的textview先变成红色，其余的依次弄成灰色
	 * 全部textview都有弄
	 */
	public void setSelector(int id) {
		for (int i = 0; i < title.length; i++) {
			if (id == i) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.grouplist_item_bg_normal);
				textViews.get(id).setBackgroundDrawable(
						new BitmapDrawable(bitmap));
				textViews.get(id).setTextColor(Color.GREEN);//选中则显示红色
				viewPager.setCurrentItem(i);
			}

			else {
				textViews.get(i).setBackgroundDrawable(new BitmapDrawable());
				textViews.get(i).setTextColor(R.color.black);
			}
		}
	}

	
	

	
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.imgView_more) {
			//slidingMenu.toggle(true);
		}
	}
	
	
	

}
