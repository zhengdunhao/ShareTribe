/**
 * 
 */
package com.atm.chatonline.bbs.activity.bbs;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.activity.bbs ---NavigationPublishPost
 * @作用 BBS导航条的发帖功能
 * @作者 陈小二
 * @时间 2015-8-18
 * 
 */
public class NavigationPublishPost extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.empty);
	}
}
