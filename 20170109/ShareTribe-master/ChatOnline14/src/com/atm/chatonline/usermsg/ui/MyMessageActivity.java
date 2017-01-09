package com.atm.chatonline.usermsg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.NewMessage;
import com.atm.chatonline.bbs.util.SharedPreferenceUtils;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;
import com.jauker.widget.BadgeView;

public class MyMessageActivity extends BaseActivity implements OnClickListener{


	private TextView tv_apply,tv_reply,tv_sys;
	private NewMessage newMessage;
	private BadgeView backgroundDrawableBadge1,backgroundDrawableBadge2,backgroundDrawableBadge3;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymessage_main_view);
		initData();
		intiView();
		
	}
	
	


	private void showBadgeView() {
		if(newMessage.getReplySum()>0){
			backgroundDrawableBadge1=new BadgeView(this.getBaseContext());
			setBadgeViewParams(backgroundDrawableBadge1, newMessage.getReplySum());
			backgroundDrawableBadge1.setTargetView(tv_apply);
		}
		
		if(newMessage.getApplySum()>0){
			backgroundDrawableBadge2=new BadgeView(this.getBaseContext());
			setBadgeViewParams(backgroundDrawableBadge2, newMessage.getApplySum());
			 backgroundDrawableBadge2.setTargetView(tv_reply);
		}
		
		if (newMessage.getSystemSum()>0) {
			backgroundDrawableBadge3 = new BadgeView(
					this.getBaseContext());
			setBadgeViewParams(backgroundDrawableBadge3,
					newMessage.getSystemSum());
			backgroundDrawableBadge3.setTargetView(tv_sys);
		}
	
	}


private void setBadgeViewParams(BadgeView backgroundDrawableBadge,int sum){
		
		if(sum>99){
			backgroundDrawableBadge.setText("99+");
		}else{
			backgroundDrawableBadge.setText(sum+"");
		}
		
        backgroundDrawableBadge.setBadgeGravity(Gravity.RIGHT);
        backgroundDrawableBadge.setBackgroundResource(R.drawable.bg_badge);
        backgroundDrawableBadge.setPadding(2, 2, 2, 2);
        backgroundDrawableBadge.setSingleLine();
		backgroundDrawableBadge.setWidth(35);
		backgroundDrawableBadge.setHeight(35);
		backgroundDrawableBadge.setTextSize(8F);
		backgroundDrawableBadge.setGravity(Gravity.CENTER);
		backgroundDrawableBadge.setBadgeMargin(0, 3, 10, 0);
	    backgroundDrawableBadge.setVisibility(View.VISIBLE);
	}


	private void initData() {
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			newMessage=(NewMessage) bundle.getSerializable("newMessage");
		}

	}




	private void intiView() {
		Button btn=(Button) findViewById(R.id.btn_back);
		RelativeLayout rlApply=(RelativeLayout) findViewById(R.id.rl_apply_msg);
		RelativeLayout rlReply=(RelativeLayout) findViewById(R.id.rl_reply_msg);
		RelativeLayout rlSys=(RelativeLayout) findViewById(R.id.rl_system_msg);
		tv_apply=(TextView)findViewById(R.id.tv_apply_msg);
		tv_reply=(TextView)findViewById(R.id.tv_reply_msg);
		tv_sys=(TextView)findViewById(R.id.tv_sys_msg);
		
		if(newMessage!=null){
			showBadgeView();
		}
		
		rlApply.setOnClickListener(this);
		rlReply.setOnClickListener(this);
		rlSys.setOnClickListener(this);
		btn.setOnClickListener(this);
	}


	@Override
	public void processMessage(Message msg) {
		
		
	}




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_apply_msg:
			if(newMessage!=null&&newMessage.getReplySum()>0){
				newMessage.setSum(newMessage.getSum()-newMessage.getReplySum());
				newMessage.setReplySum(0);
				backgroundDrawableBadge1.setVisibility(View.GONE);
				SharedPreferenceUtils.getInstance().saveNewMessage(newMessage, getApplicationContext());
			}
			Intent intent=new Intent(this, ApplyMsg.class);
			startActivity(intent);
			break;
		case R.id.rl_reply_msg:
			if(newMessage!=null&&newMessage.getApplySum()>0){
				newMessage.setSum(newMessage.getSum()-newMessage.getApplySum());
				newMessage.setApplySum(0);
				backgroundDrawableBadge2.setVisibility(View.GONE);
				SharedPreferenceUtils.getInstance().saveNewMessage(newMessage, getApplicationContext());
			}
			Intent intent2=new Intent(this, ReplyMsg.class);
			startActivity(intent2);
			break;
		case R.id.rl_system_msg:
			if(newMessage!=null&&newMessage.getSystemSum()>0){
				newMessage.setSum(newMessage.getSum()-newMessage.getSystemSum());
				newMessage.setSystemSum(0);
				backgroundDrawableBadge3.setVisibility(View.GONE);
				SharedPreferenceUtils.getInstance().saveNewMessage(newMessage, getApplicationContext());
			}
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
