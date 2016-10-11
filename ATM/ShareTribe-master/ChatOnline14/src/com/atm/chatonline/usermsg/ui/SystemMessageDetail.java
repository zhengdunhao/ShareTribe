package com.atm.chatonline.usermsg.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.atm.chatonline.usermsg.bean.NotificationContent;
import com.example.studentsystem01.R;

public class SystemMessageDetail extends Activity implements OnClickListener{

	private NotificationContent nfc;
	private TextView title,content,time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemmsg_detail_view);
		initData();
		initView();

	}

	private void initView() {
		Button btn=(Button) findViewById(R.id.btn_back);
		title=(TextView) findViewById(R.id.system_title);
		content=(TextView) findViewById(R.id.system_content);
		time=(TextView) findViewById(R.id.system_time);
		
		title.setText(nfc.getTitle());
		content.setText(nfc.getContent());
		time.setText(nfc.getTime());
		
		btn.setOnClickListener(this);
	}
	
	private void initData(){
		Bundle bundle=getIntent().getExtras();
		nfc=(NotificationContent) bundle.get("SystemMessage");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	
}
