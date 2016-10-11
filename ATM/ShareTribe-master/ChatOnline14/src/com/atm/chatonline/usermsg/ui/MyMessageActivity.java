package com.atm.chatonline.usermsg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

public class MyMessageActivity extends BaseActivity implements OnClickListener{


	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymessage_main_view);
		Button btn=(Button) findViewById(R.id.btn_back);
		btn.setOnClickListener(this);
		intiView();
	}
	
	


	private void intiView() {
		RelativeLayout rlApply=(RelativeLayout) findViewById(R.id.rl_apply_msg);
		RelativeLayout rlReply=(RelativeLayout) findViewById(R.id.rl_reply_msg);
		RelativeLayout rlSys=(RelativeLayout) findViewById(R.id.rl_system_msg);
	
		rlApply.setOnClickListener(this);
		rlReply.setOnClickListener(this);
		rlSys.setOnClickListener(this);
	}


	@Override
	public void processMessage(Message msg) {
		
		
	}




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_apply_msg:
			Intent intent=new Intent(this, ApplyMsg.class);
			startActivity(intent);
			break;
		case R.id.rl_reply_msg:
			Intent intent2=new Intent(this, ReplyMsg.class);
			startActivity(intent2);
			break;
		case R.id.rl_system_msg:
			Intent intent3=new Intent(this, SystemMsg.class);
			startActivity(intent3);
			break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
		
	}

	
}
