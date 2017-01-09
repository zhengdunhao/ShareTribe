package com.atm.charonline.recuit.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.activity.bbs ---BBSReportView
 * @作用 该类用于显示举报界面
 * @作者 钟YD
 * @时间 2015-8-24
 * 
 */

public class RecuitReportView extends BaseActivity {
	private ImageView iv_return;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report_view);

		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RecuitReportView.this.finish();
			}
		});
	}
	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}
}
