package com.atm.chatonline.bbs.activity.login;

import android.os.Bundle;
import android.os.Message;
import android.view.Window;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

public class ImgExample extends BaseActivity{
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.img_example);
		
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}
}
