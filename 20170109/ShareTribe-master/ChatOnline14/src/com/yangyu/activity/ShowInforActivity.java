package com.yangyu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.sharesdk.framework.TitleLayout;

import com.example.studentsystem01.R;

/**
 * @author yangyu
 *	功能描述：显示用户信息资料
 */
public class ShowInforActivity  extends Activity implements OnClickListener {
	private TitleLayout llTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_userinfo);
		
		llTitle = (TitleLayout) findViewById(R.id.llTitle);
		llTitle.getBtnBack().setOnClickListener(this);
		llTitle.getTvTitle().setText("用户资料");
		
		TextView tvJson = (TextView) findViewById(R.id.tvJson);
		tvJson.setText(getIntent().getStringExtra("data"));
	}
	
	@Override
	public void onClick(View v) {
		if (v.equals(llTitle.getBtnBack())) {
			finish();
		}	
	}

}
