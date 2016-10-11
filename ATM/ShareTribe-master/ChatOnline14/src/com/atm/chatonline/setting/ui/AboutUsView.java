package com.atm.chatonline.setting.ui;

import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

public class AboutUsView extends BaseActivity implements OnClickListener{
	private String tag = "AboutUsView";
	private Button btnBack;

	private TextView title;
	private SpannableString sps = null;
	private String titleName ="关于我们";
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		try{
		setContentView(R.layout.about_us_view);
		initUI();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	void initUI(){
		try{
		btnBack = (Button)findViewById(R.id.btn_back);
		title = (TextView)findViewById(R.id.title);
		sps = new SpannableString(titleName);
		sps.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, titleName.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体 0-4字体的长度
		title.setText(sps);
		title.setTextSize(18);
		btnBack.setOnClickListener(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			this.onBackPressed();
			break;
		}
	}

}
