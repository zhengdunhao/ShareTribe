package com.atm.chatonline.usercenter.activity.usercenter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.AttentionActivity;
import com.atm.chatonline.chat.ui.FollowersActivity;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.ui.PersonalMessageActivity;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.usercenter.util.HttpUtils;
import com.example.studentsystem01.R;
/**
 * 此类用于显示个人中心的各子功能，可以点击进入用户的基本信息，帖子，关注我的人，邀请校友等选项卡，进入此类intent不需要存放数据
 * @author Jackbing
 * @date 2016-10-2 修改
 */

public class UserCenter extends BaseActivity implements OnClickListener{
	
	final String tag="UserCenter";
	private String userId,headImgPath,nickname,cookie;
	private Handler handler;
	private TextView tvNickName,tvUserId;
	private CircleImageView civ;
	private Bitmap img;
	private ProgressBar pro;
	private Bundle bundle;
	private final int BASIC_MSG=13;//数字尽量定大点，避免与系统的变量冲突
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bg_user_center);
		pro=(ProgressBar)findViewById(R.id.progress_user_center);
		Button btnBackBeforeLoad=(Button)findViewById(R.id.btn_back);
		btnBackBeforeLoad.setOnClickListener(this);
		initData();
		
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				pro.setVisibility(View.GONE);
				switch(msg.what){
				case 1:
					initView();
					break;
				}
			}
			
		};
	}

	/**
	 * 初始化数据，获取用户的cookie，并且启动一个子线程来访问获取用户的信息（如头像,昵称）
	 */
	private void initData() {
		
		
		userId=getPreference().getUserID();
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		bundle=new Bundle();
		bundle.putString("cookie", cookie);
		new Thread(new Runnable(){

			@Override
			public void run() {
				Map<String,String> params=new HashMap<String,String>();
				params.put("userId", userId);
				
				JSONObject o=HttpUtils.post(UriAPI.BASIC_MESSAGE, params, null);
				try {
					if(!o.getString("userId").equals("")){
					getUserInfoFromJSON(o);
					handler.sendEmptyMessage(1);
					}else{
						Toast.makeText(UserCenter.this.getApplicationContext(), "服务器无响应", Toast.LENGTH_SHORT).show();
						handler.sendEmptyMessage(3);
					}
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					return;
				}
				
			}
			
		}).start();
	}

	
	public void getUserInfoFromJSON(JSONObject o){
		if(o!=null){
			Log.i(tag, "o is not null o="+o.toString());
		try {
			headImgPath=o.getString("headImagePath");
			headImgPath=UriAPI.SUB_URL+headImgPath;
			
			nickname=o.getString("nickname");
			userId=o.getString("userId");
			
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		img=HttpUtils.getBitmapFromUrl(headImgPath);
		Log.i(tag, "img is null"+(img==null));
		
		}
	}
	private void initView() {
	LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View v=inflater.inflate(R.layout.user_center_view, null);
	Button btnOnBack=(Button)v.findViewById(R.id.btn_back);
	LinearLayout basicMessage=(LinearLayout)v.findViewById(R.id.basic_message);
	LinearLayout label=(LinearLayout)v.findViewById(R.id.user_center_label);
	LinearLayout note=(LinearLayout)v.findViewById(R.id.user_center_note);
	LinearLayout beAttended=(LinearLayout)v.findViewById(R.id.user_center_be_attended);
	LinearLayout attend=(LinearLayout)v.findViewById(R.id.user_center_attend);
	tvNickName=(TextView)v.findViewById(R.id.user_center_nickname);
	tvUserId=(TextView)v.findViewById(R.id.user_center_userId);
	civ=(CircleImageView)v.findViewById(R.id.user_center_headimg);
	TextView titleTextView=(TextView)v.findViewById(R.id.title);
	titleTextView.setText(getString(R.string.user_center));
	tvNickName.setText(nickname);
	tvUserId.setText(userId);
	civ.setImageBitmap(img);
	
	btnOnBack.setOnClickListener(this);
	basicMessage.setOnClickListener(this);
	label.setOnClickListener(this);
	note.setOnClickListener(this);
	beAttended.setOnClickListener(this);
	attend.setOnClickListener(this);
	setContentView(v);
	}
	  
	public void onClick(View v){
		int id=v.getId();
		switch(id){
		case R.id.user_center_attend:
			Intent atIntent=new Intent(this,AttentionActivity.class);
			startIntent(atIntent);
			Log.i(tag, "关注被点击");
			break;
		case R.id.user_center_be_attended:
			Intent flIntent=new Intent(this,FollowersActivity.class);
			startIntent(flIntent);
			Log.i(tag, "粉丝被点击");
			break;
		case R.id.user_center_label:
			Log.i(tag, "标签被点击");
			Intent intent=new Intent(this,MyLabel.class);
			intent.putExtra("cookie", cookie);
			startActivity(intent);
			break;
		case R.id.user_center_note:
			Log.i(tag,"帖子被点击");
			Intent intentBBs=new Intent(this,MyBbs.class);
			intentBBs.putExtras(bundle);
			startActivity(intentBBs);
			break;
		case R.id.btn_back:
			this.onBackPressed();
			break;
		case R.id.basic_message:
			startActivityForResult(new Intent(this,BasicMessage.class),0);
			break;	
		}
	}

	private void startIntent(Intent intent) {
		intent.putExtra("userId", userId);
		intent.putExtra("friendID", userId);
		intent.putExtra("nickName", nickname);
		intent.putExtra("fromActivity",Config.USER_CENTER );
		startActivityForResult(intent,0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		if(data==null){
			return;
		}
		switch(resultCode){
		case BASIC_MSG:
			userId=data.getStringExtra("userId");
			nickname=data.getStringExtra("nickName");
			img=data.getParcelableExtra("img");
			tvNickName.setText(nickname);
			tvUserId.setText(userId);
			civ.setImageBitmap(img);
			break;
		}
	}

	protected void onResume(){
		super.onResume();
		getAllChildViews(UserCenter.this);
	}
	
	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}
	
}
