package com.atm.chatonline.bbs.activity.login;
/**
 * 该类用于，生成欢迎界面（启动界面）
 * 2015.7.21,atm--小二
 * 
 * 这次修改我没有用到线程，但启动界面可以看到
 * 2015-7-28-郑
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

public class WelcomeView extends Activity{
	private AlphaAnimation start_anima;
	View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(this, R.layout.welcome_view, null);//Inflate()作用就是将xml定义的一个布局找出来，但仅仅是找出来而且隐藏的
		setContentView(view);
		initView();
		initData();
	}
	private void initData() {
		start_anima = new AlphaAnimation(0.3f, 1.0f);
		start_anima.setDuration(2000);
		view.startAnimation(start_anima);
		start_anima.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				redirectTo();
			}
		});
	}
	
	private void initView() {
		
	}

	private void redirectTo() {
		Intent intent=new Intent(getApplicationContext(), LoginView.class);
		Bundle bundle=new Bundle();
		bundle.putInt("login", Config.FIRSTLOGIN);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
}
