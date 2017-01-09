package com.atm.chatonline.bbs.util;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atm.charonline.recuit.ui.RecuitMainView;
import com.atm.charonline.recuit.ui.RecuitNavigationPublishPost;
import com.atm.charonline.recuit.ui.RecuitNavigationSearch;
import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.activity.bbs.BBSPublishPostView;
import com.atm.chatonline.bbs.activity.bbs.NavigationSearch;
import com.atm.chatonline.bbs.activity.login.LoginView;
import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.bbs.commom.NewMessage;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.ui.ChatMainActivity;
import com.atm.chatonline.chat.ui.CreateGroupActivity;
import com.atm.chatonline.chat.ui.SearchFriendListActivity;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.schoolnews.ui.RecommendActivity;
import com.atm.chatonline.schoolnews.ui.SchoolNewsActivity;
import com.atm.chatonline.setting.ui.SettingView;
import com.atm.chatonline.usercenter.activity.usercenter.UserCenter;
import com.atm.chatonline.usermsg.ui.MyMessageActivity;
import com.example.studentsystem01.R;
import com.jauker.widget.BadgeView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @类 com.atm.charonline.bbs.util ---SlidingTitleLayout
 * @作用
 * @作者 陈小二
 * @时间 2015-10-25
 * 
 */
public class SlidingTitleLayout extends LinearLayout implements OnClickListener{

	private SlidingMenu slidingMenu;
	
	private ImageView imgViewPerCenter, imgViewEdit, imgViewSearch;
	private ImageView imgViewChatSearch,imgViewMore;
	private ImageView imgViewInvite;
	private ImageView news_iv,msg_iv;
	private LinearLayout l1,l2,l3,l4,l5,l6,l7;
	private Activity activity;
	private String ACTIVITY;
	private String BBS="com.atm.chatonline.bbs.activity.bbs.BBSMainView";
	private String RECUIT="com.atm.charonline.recuit.ui.RecuitMainView";
	private String CHAT="com.atm.chatonline.chat.ui.ChatMainActivity";
	private String SCHOOLNEWS="com.atm.chatonline.schoolnews.ui.SchoolNewsActivity";
	private String MY_MSG="com.atm.chatonline.usermsg.ui.MyMessageActivity";
	private String USER_CENTER="com.atm.chatonline.usercenter.activity.usercenter.UserCenter";
	Intent intent1,intent2,intent3,intent0,intent4,intent5,intent6,intent7,intentRecommend;
	Intent intentCreateGroup,intentSearch;//对应于聊天的小窗口
	private int i=0;
	private Button btnexit;
	private String tag ="SlidingTitleLayout";
	private CircleImageView headImage;
	private String subPath="atm_getUserHead.action";
	private String headSubPath;
	private TextView txt_name;
	public static Handler handler;
	private boolean isLoadNewMessage=false;
	private NewMessage newMessage;
	private BadgeView backgroundDrawableBadge1,backgroundDrawableBadge2;
	
//	private MtitlePopupWindow popupWindow1;
	/**
	 * @param context
	 * @param attrs
	 */
	public SlidingTitleLayout(Context context, AttributeSet attrs) {
		
		super(context, attrs);
//		handler = new Handler() {
//			public void handlerMessage(Message msg) {
//				switch(msg.what) {
//				case Config.SLIDINGMENUTOGGLE:
//					slidingMenu.toggle(false);
//					LogUtil.d("调用~~~~~~~~~~~~~~~~~~handler开启toggle"); 	
//					break;
//				default:
//					break;
//				}
//			}
//		};
		LogUtil.p(tag, "初始化标题栏");
		// 初始化标题栏
		activity = (Activity) getContext();
		ACTIVITY=activity.getLocalClassName().toString();
		Log.i(tag, "ACTIVITY"+ACTIVITY);
		
		if(ACTIVITY.equals(BBS)||ACTIVITY.equals(RECUIT)){
			LogUtil.p(tag, "BBSMainView是这个");
			LayoutInflater.from(context).inflate(R.layout.slidingtitle, this);
			
			imgViewEdit = (ImageView) findViewById(R.id.imgView_edit);
			imgViewEdit.setOnClickListener(this);
			imgViewSearch = (ImageView) findViewById(R.id.imgView_search);
			imgViewSearch.setOnClickListener(this);
			
		}else if(ACTIVITY.equals(CHAT)){
			LogUtil.p(tag, "ChatMainActivity是这个");
			LayoutInflater.from(context).inflate(R.layout.chat_main_head, this);
//			imgViewMore = (ImageView) findViewById(R.id.imgView_more);
//			imgViewMore.setOnClickListener(this);
			imgViewChatSearch = (ImageView) findViewById(R.id.imgView__chat_search);
			imgViewChatSearch.setOnClickListener(this);
//			initPopupWindow();
			
		}else if(ACTIVITY.equals(SCHOOLNEWS)){
			LogUtil.p(tag, "SchoolNewsActivity是这个");
			LayoutInflater.from(context).inflate(R.layout.school_news_head, this);
			imgViewInvite = (ImageView) findViewById(R.id.imgView_invite);
			imgViewInvite.setOnClickListener(this);
		}
		imgViewPerCenter = (ImageView) findViewById(R.id.imgView_person_center);
		
		imgViewPerCenter.setOnClickListener(this);
		
		
		
		InItSlidingMenu();
		
		intent2 = new Intent(getContext(), ChatMainActivity.class);
		intent1 = new Intent(getContext(), BBSMainView.class);
		intent3 = new Intent(getContext(), RecuitMainView.class);
		intent0 = new Intent(getContext(),LoginView.class);
		intent4 = new Intent(getContext(),SchoolNewsActivity.class);
		intent5 = new Intent(getContext(),UserCenter.class);
		intent6 = new Intent(getContext(),SettingView.class);
		intent7=new Intent(getContext(),MyMessageActivity.class);
		intentRecommend = new Intent(getContext(),RecommendActivity.class);
		headImage.setOnClickListener(this);
		intentCreateGroup = new Intent(getContext(),CreateGroupActivity.class);
		intentSearch = new Intent(getContext(),SearchFriendListActivity.class);
	}
	
	/**
	 * 初始化滑动菜单
	 * 
	 * */

	void InItSlidingMenu() {
		slidingMenu = new SlidingMenu(getContext());
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffset(4 / 5);
		slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// 设置触摸范围
											//(Activity) getContext()对的吗？					// TOUCHMODE_FULLSCREEN（全局）
//		slidingMenu.attachToActivity(activity,SlidingMenu.RIGHT);// SLIDING_CONTENT
		slidingMenu.setMenu(R.layout.sliding_menu_view);

		l1 = (LinearLayout) slidingMenu.findViewById(R.id.bbs);
		l2 = (LinearLayout) slidingMenu.findViewById(R.id.chat);
		l3 = (LinearLayout) slidingMenu.findViewById(R.id.recuit);
		l4 = (LinearLayout) slidingMenu.findViewById(R.id.news);
		l5 = (LinearLayout) slidingMenu.findViewById(R.id.user_center);
		l6 = (LinearLayout) slidingMenu.findViewById(R.id.setting);
		l7=(LinearLayout)slidingMenu.findViewById(R.id.my_message_tab);
		news_iv=(ImageView)slidingMenu.findViewById(R.id.btn_news_iv);
		msg_iv=(ImageView)slidingMenu.findViewById(R.id.btn_msg_iv);
		headImage = (CircleImageView) slidingMenu.findViewById(R.id.btn_headimage);
		LogUtil.p(tag, "头像初始化");
		btnexit = (Button) slidingMenu.findViewById(R.id.btn_slide_exit);
		//经过讨论，先暂时去掉
//		txt_bbs = (TextView)slidingMenu.findViewById(R.id.btn_bbs);
//		
//		txt_chat = (TextView)slidingMenu.findViewById(R.id.btn_chat);
//		
//		txt_recuit = (TextView)slidingMenu.findViewById(R.id.btn_recuit);
//		txt_news = (TextView)slidingMenu.findViewById(R.id.btn_news);
//		txt_personal = (TextView)slidingMenu.findViewById(R.id.btn_personal);
//		
//		txt_msg = (TextView)slidingMenu.findViewById(R.id.btn_msg);
//		txt_setting = (TextView)slidingMenu.findViewById(R.id.btn_settings);
//		
//		btnexit.setTextSize(BaseActivity.fontSize);
//		
//		LogUtil.p(tag, "111BaseActivity.fontSize:"+BaseActivity.fontSize);
//		txt_bbs.setTextSize(BaseActivity.fontSize);
//		LogUtil.p(tag, "222");
//		txt_chat.setTextSize(BaseActivity.fontSize);
//		LogUtil.p(tag, "333");
//		txt_recuit.setTextSize(BaseActivity.fontSize);
//		txt_news.setTextSize(BaseActivity.fontSize);
//		txt_personal.setTextSize(BaseActivity.fontSize);
//		LogUtil.p(tag, "44");
//		txt_msg.setTextSize(BaseActivity.fontSize);
//		txt_setting.setTextSize(BaseActivity.fontSize);
		
		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);
		l5.setOnClickListener(this);
		l6.setOnClickListener(this);
		l7.setOnClickListener(this);
		btnexit.setOnClickListener(this);
		
		

	}
	
	public void showBadgeView(NewMessage newMessage){
		this.newMessage=newMessage;
		LogUtil.i("showbadge");
		if(backgroundDrawableBadge1==null){
			backgroundDrawableBadge1=new BadgeView(getContext());
		}
		if(backgroundDrawableBadge2==null){
			backgroundDrawableBadge2=new BadgeView(getContext());
		}
		setBadgeViewParams(backgroundDrawableBadge1, newMessage);
		setBadgeViewParams(backgroundDrawableBadge2, newMessage);
        backgroundDrawableBadge1.setTargetView(imgViewPerCenter);
        backgroundDrawableBadge2.setTargetView(msg_iv);
      
      
	}
	
	
	
	public void hideBadgeView(){
		if(backgroundDrawableBadge1!=null){
			backgroundDrawableBadge1.setVisibility(View.GONE);
			
		}
		if(backgroundDrawableBadge2!=null){
			backgroundDrawableBadge2.setVisibility(View.GONE);
			
		}
	}
	
	
	private void setBadgeViewParams(BadgeView backgroundDrawableBadge,NewMessage newMessage){
		
		if(newMessage!=null){
			if(newMessage.getSum()>99){
				backgroundDrawableBadge.setText("99+");
			}else{
				backgroundDrawableBadge.setText(newMessage.getSum()+"");
			}
			
	        backgroundDrawableBadge.setBadgeGravity(Gravity.RIGHT);
	        backgroundDrawableBadge.setBackgroundResource(R.drawable.bg_badge);
	        backgroundDrawableBadge.setPadding(2, 2, 2, 2);
	        backgroundDrawableBadge.setSingleLine();
			backgroundDrawableBadge.setWidth(35);
			backgroundDrawableBadge.setHeight(35);
			backgroundDrawableBadge.setTextSize(8F);
			backgroundDrawableBadge.setGravity(Gravity.CENTER);
			backgroundDrawableBadge.setBadgeMargin(0, 3, 0, 0);
		    backgroundDrawableBadge.setVisibility(View.VISIBLE);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imgView_person_center:
			LogUtil.p(tag,"imgView_person_center被点了");
			if(i==0) {
				LogUtil.p(tag, "imgView_person_center进来");
				//每点击之前获取不到activity，所以只能在点击之后在设置改变量
				slidingMenu.attachToActivity(activity,SlidingMenu.RIGHT);
				//设置侧拉框的头像
				if(ConfigUtil.nums == 0) {
				Thread thread = new Thread(new Runnable() {
					public void run() {
						BBSConnectNet bBSConnectNet = new BBSConnectNet(subPath,ConfigUtil.getCookie());
						String response = bBSConnectNet.getResponse();
						try {
							JSONObject obj = new JSONObject(response);
							if(obj.getString("userHead")!=null) {
								headSubPath = obj.getString("userHead");
								LogUtil.p("userHead", headSubPath);
							}else {
								LogUtil.p("userHead", "null");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Environment.getExternalStorageDirectory()+"/ATM/userhead"
//				ConfigUtil.setHead(new ReceivePhoto(headSubPath).getPhoto());
//				headImage.setImageDrawable(ConfigUtil.getHead());
				headImage.setImageBitmap(FileUtil.getBitmap(Environment.getExternalStorageDirectory()+"/ATM/userhead/"+BaseActivity.getSelf().getUserID()+".jpg"));
				ConfigUtil.nums++;}
				else {
//					headImage.setImageDrawable(ConfigUtil.getHead());
					headImage.setImageBitmap(FileUtil.getBitmap(Environment.getExternalStorageDirectory()+"/ATM/userhead/"+BaseActivity.getSelf().getUserID()+".jpg"));
				}
				i++;
			}
			slidingMenu.toggle();
			LogUtil.d("slidingMenu.toggle();");
			break;
		case R.id.imgView_edit:
			LogUtil.p(tag,"imgView_edit被点了");
			if(ACTIVITY.equals(BBS)) {
			activity.startActivity(new Intent(getContext(), BBSPublishPostView.class));}
			if(ACTIVITY.equals(RECUIT)) {
			activity.startActivity(new Intent(getContext(), RecuitNavigationPublishPost.class));}
			break;
		case R.id.imgView_search:
			LogUtil.p(tag,"imgView_search被点了");
			if(ACTIVITY.equals(BBS)) {
			activity.startActivity(new Intent(getContext(), NavigationSearch.class));}
			if(ACTIVITY.equals(RECUIT))
			activity.startActivity(new Intent(getContext(), RecuitNavigationSearch.class));
			break;
			//对应聊天界面右上角的小窗口，有建群和搜人-----修改了：改为只有搜索（2016-3-18）
		case R.id.imgView__chat_search:
//			popupWindow.showAsDropDown(v);
			activity.startActivity(intentSearch);
			break;
//			
		case R.id.bbs:
			LogUtil.p(tag,"bbs被点了");
			if(ACTIVITY.equals(BBS)) 	//如果是本来的界面就直接关闭侧拉框
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent1);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			
			break;
		case R.id.chat:
			LogUtil.p(tag,"chat被点了");
			if(ACTIVITY.equals(CHAT)) 
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent2);
			activity.overridePendingTransition(R.anim.slide_in_right1, R.anim.slide_out_left);
			if(ACTIVITY.equals(BBS))  //如果是从bbs切换到其他界面，就先把bbs的侧拉框关掉
				slidingMenu.toggle(false);
			break;
		case R.id.recuit:
			LogUtil.p(tag,"recuit被点了");
			if(ACTIVITY.equals(RECUIT)) 
				slidingMenu.toggle(false);
			activity.startActivity(intent3);
			activity.overridePendingTransition(R.anim.slide_in_right1, R.anim.slide_out_left);
			if(ACTIVITY.equals(BBS))  //如果是从bbs切换到其他界面，就先把bbs的侧拉框关掉
				slidingMenu.toggle(false);
		
			break;
		case R.id.user_center:
			LogUtil.p(tag,"user_center被点了");
			if(ACTIVITY.equals(USER_CENTER))
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent5);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//			if(ACTIVITY.equals(BBS))  //如果是从bbs切换到其他界面，就先把bbs的侧拉框关掉
//				slidingMenu.toggle(false);
			break;
//
		case R.id.news:
			LogUtil.p(tag,"news被点了");
			if(ACTIVITY.equals(SCHOOLNEWS)) 
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent4);
			activity.overridePendingTransition(R.anim.slide_in_right1, R.anim.slide_out_left);
			if(ACTIVITY.equals(BBS))  //如果是从bbs切换到其他界面，就先把bbs的侧拉框关掉
				slidingMenu.toggle(false);
			LogUtil.p(tag, "SCHOOLNEWS跳转");
			break;
		
		case R.id.imgView_invite:
			LogUtil.p(tag,"推荐校友被点了");
			activity.startActivity(intentRecommend);
			break;
			
		case R.id.btn_slide_exit:
			LogUtil.p(tag,"btn_slide_exit被点了");
			new Thread(exitRunnable).start();
			break;
			
		case R.id.btn_headimage:
			LogUtil.p(tag,"btn_headimage头像被点了");
			activity.startActivity(intent5);
			break;
			
		case R.id.setting:
			LogUtil.p(tag,"设置被点击");
			activity.startActivity(intent6);
			break;
		case R.id.my_message_tab:
			LogUtil.p(tag, "我的消息选项卡被点击了");
			if(newMessage!=null&&newMessage.getSum()>0){
				Bundle bundle=new Bundle();
				bundle.putSerializable("newMessage", newMessage);
				intent7.putExtras(bundle);
			}
			if(ACTIVITY.equals(MY_MSG))
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent7);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		}
		
	}
	
	Runnable exitRunnable = new Runnable() {

		@Override
		public void run() {
			Log.i("---->>>>>>", "userId = " + BaseActivity.getSelf().getUserID());

			if(BaseActivity.con==null){
				LogUtil.p(tag, "con为空");
			}else{
			BaseActivity.con.shutDownSocketChannel();
			}
//			File data=new File("/data/data/"+getPackageName().toString()+"/shared_prefs","data.xml");
//			File count=new File("/data/data/"+getPackageName().toString()+"/shared_prefs","count.xml");
			
			
			File data=new File("/data/data/"+activity.getPackageName()+"/shared_prefs","data.xml");
			File count=new File("/data/data/"+activity.getPackageName()+"/shared_prefs","count.xml");
			if(data.exists()==true){
				
				data.delete();
				Log.i(tag, "data删除成功");
			}
			else{
				Log.i(tag, "data 删除不成功");
			}
			if(count.exists()==true){
				count.delete();
				Log.i(tag, "count删除成功");
			}
			else{
				Log.i(tag, "count 删除不成功");
			}
			
			while(BaseActivity.queue.size()!=1){
				LogUtil.p(tag,"exit--queue.size:"+BaseActivity.queue.size());
				BaseActivity.queue.removeLast();
				LogUtil.p(tag, "exit--queue:"+BaseActivity.queue.toString());
			}
			redirectTo();
		}

	};
	
	//2015-10-24之前注释
		private void redirectTo() {
			LogUtil.p(tag, "main view queue size="+BaseActivity.queue.size());
			intent0.putExtra("login", Config.BE_OFF_LOGIN);
			activity.startActivity(intent0);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			
			LogUtil.p(tag, "main view queue size="+BaseActivity.queue.size());
			
		}
		
		//initPopupWindow()被注释掉的原因是因为我把搜索群和新建群都给弄没了
//		void initPopupWindow(){
//			popupWindow = new MtitlePopupWindow(activity);
//			popupWindow.setOnPopupWindowClickListener(new OnPopupWindowClickListener() {
//				
//				@Override
//				public void onPopupWindowItemClick(int position) {
//					//你要做的事
//					Log.i(tag, "********");
//					if(position==0){
//						Log.i(tag, "position被选中是0");
//						activity.startActivity(intentCreateGroup);
//					}
//					else if(position==1){
//						Log.i(tag, "position被选中是1");
//						activity.startActivity(intentSearch);
//					}
//				}
//			});
		
//	public static SlidingMenu getSlidingMenu() {
//		return slidingMenu;
//	}
//	public static void setToggle() {
//		slidingMenu.toggle(false);
//	}
//		public static void setToggle() {
//			new Thread(new Runnable() {
//				public void run() {
//					Message msg = new Message();
//					msg.what = Config.SLIDINGMENUTOGGLE;
//					handler.sendMessage(msg);
//					LogUtil.d("调用~~~~~~~~~~~~~~~~~~handler开启toggle");
//					
//				}
//			}).start();
//			LogUtil.d("调用~~~~~~~~~~~~~~~~~~handler开启toggle");
//		}

}