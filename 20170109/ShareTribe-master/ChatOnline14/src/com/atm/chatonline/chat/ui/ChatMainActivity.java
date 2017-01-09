package com.atm.chatonline.chat.ui;

/**
 * 
 * 私聊的聊天窗口
 * */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.commom.NewMessage;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SharedPreferenceUtils;
import com.atm.chatonline.bbs.util.SlidingTitleLayout;
import com.atm.chatonline.chat.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.chat.info.ChatMessage;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.info.Group;
import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.chat.util.MtitlePopupWindow;
import com.atm.chatonline.chat.util.MtitlePopupWindow.OnPopupWindowClickListener;
import com.atm.chatonline.schoolnews.ui.SchoolNewsActivity;
import com.atm.chatonline.setting.ui.SettingView;
import com.atm.chatonline.usercenter.activity.usercenter.UserCenter;
import com.example.studentsystem01.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ResourceAsColor")
public class ChatMainActivity extends BaseActivity implements OnClickListener,
		ReceiveInfoListener {
	private SlidingMenu slidingMenu;
	private RadioGroup radioGroup;
	private String title[] = { "聊天", "关注" };
	private LinearLayout linearLayout;
	// private final int height = 70;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private HorizontalScrollView horizontalScrollView;
	private ImageView imgViewMore, imgViewCenter;
	private TextView textView;
	private String tag = "ChatMainActivity";
	private String userId;
	private List<Group> GroupList;
	private List<Friend> FriendList;
	private MtitlePopupWindow popupWindow;
	private boolean flag = true;
	public static SlidingTitleLayout slidingTitleLayout;
	private static String STATE = "M";

	protected void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "ChatMainActivity--------即将进入");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_main_view);
		try {
			if (con == null) {
				Log.i(tag, "con为null");
				new Thread(loginAgainRunnable).start();
			} else {
				Log.i(tag, "con不为null");
				con.addReceiveInfoListener(STATE, ChatMainActivity.this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.i(tag, "con.addReceiveInfoListener走完");
		SharedPreferences pref = getSharedPreferences("User", MODE_PRIVATE);
		// userId=pref.getString("userID", "");
		userId = BaseActivity.getSelf().getUserID();
		LogUtil.p(tag, "很重要:userId:" + userId);
		gueryDataBase();// 查询本地数据库
		Log.i("@@@@@@", "queue size =" + queue.size());
		linearLayout = (LinearLayout) findViewById(R.id.tab_view);// 获取bbs_main_view的子布局，即标题栏下面的布局
		viewPager = (ViewPager) findViewById(R.id.pager);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		slidingTitleLayout=(SlidingTitleLayout)findViewById(R.id.slidingtitlelayout);
		slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
		InItView();

		Log.i(tag,
				"ChatMainActivity--------已经加载了GroupListActivity和FriendListActivity");
		InItTitle();
		setSelector(0);
		initPager();

		popupWindow = new MtitlePopupWindow(this);
		popupWindow
				.setOnPopupWindowClickListener(new OnPopupWindowClickListener() {

					@Override
					public void onPopupWindowItemClick(int position) {
						// 你要做的事
						Log.i(tag, "********");
						if (position == 0) {
							Log.i(tag, "position被选中是0");
							Intent intent = new Intent(ChatMainActivity.this,
									CreateGroupActivity.class);
							startActivity(intent);
						} else if (position == 1) {
							Log.i(tag, "position被选中是1");
							Intent intent = new Intent(ChatMainActivity.this,
									SearchActivity.class);
							startActivity(intent);
						}
					}
				});
		// InItSlidingMenu();
		// initWidget();

		if (flag) {
			new Thread(runnable).start();
		}
		Log.i(tag, flag + "");

	}

	// 给切换控件设置监听
	private void initWidget() {
		LinearLayout l1 = (LinearLayout) findViewById(R.id.bbs);
		// LinearLayout l2 = (LinearLayout) findViewById(R.id.chat);
		LinearLayout l3 = (LinearLayout) findViewById(R.id.recuit);
		LinearLayout l4 = (LinearLayout) findViewById(R.id.user_center);
		LinearLayout l5 = (LinearLayout) findViewById(R.id.news);
		l1.setOnClickListener(this);
		// l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);
		l5.setOnClickListener(this);
	}

	/**
	 * 根据用户id在本地数据库查询用户相关的群,目前查询群列表，私信列表还未写
	 */
	@SuppressWarnings("unchecked")
	private void gueryDataBase() {
		try {
			userId = BaseActivity.getSelf().getUserID();
			LogUtil.p(tag, "gueryDataBase--很重要:userId:" + userId);
			Map<String, Object> map = dbUtil.queryGroupList(userId);
			GroupList = (List<Group>) map.get("groupList");
			flag = (Boolean) map.get("flag");
			Log.i(tag, flag + "2");
			Log.i(tag, "群数量 =" + GroupList.size());
			if (GroupList != null && GroupList.size() > 0) {
				for (int i = 0; i < GroupList.size(); i++) {
					Log.i(tag, "gueryDataBase()---有群");
					Group group = GroupList.get(i);
					Bitmap bm = FileUtil.getBitmap(FileUtil.APP_PATH + "/group"
							+ "/" + userId + "/" + group.getGroupId() + ".jpg");
					group.setBm(bm);
				}
			}
			Log.i(tag, "---gueryDataBase()---userId:" + userId);
			FriendList = dbUtil.queryPersonalChatList(userId);
			if (FriendList == null) {
				Log.i("friendlist", "friend is null");
			} else {
				Log.i("friendlist", "friend is not null");
				LogUtil.p(tag, "friendlist.size:" + FriendList.size());
			}
			if (FriendList != null && FriendList.size() > 0) {
				Log.i(tag, "gueryDataBase()---走了进来");
				for (Friend friend : FriendList) {
					Bitmap bm = FileUtil.getBitmap(FileUtil.APP_PATH
							+ "/friend" + "/" + userId + "/"
							+ friend.getFriendID() + ".jpg");
					friend.setBm(bm);
				}
			}
			Log.i(tag, "gueryDataBase()走完了");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 初始化群列表，私信列表 加载到pageViews
	 * 
	 * @throws InterruptedException
	 */
	void InItView() {
		try {
			Log.i("*************", "*********************");
			pageViews = new ArrayList<View>();
			View view01, view02;// view01:聊天，view02:关注

			if (getLocalActivityManager().getActivity("ChatAttentionActivity") != null) {
				Log.i(tag, "ChatAttentionActivity不为null");
				ChatAttentionActivity chatAtt = (ChatAttentionActivity) getLocalActivityManager()
						.getActivity("ChatAttentionActivity");
				// group.upDate(GroupList);
				view02 = chatAtt.getWindow().getDecorView();
			} else {
				Intent intent = new Intent(this, ChatAttentionActivity.class);
				// ArrayList list=new ArrayList();
				// list.add(GroupList);
				// intent.putExtra("userId", userId);
				// intent.putParcelableArrayListExtra("list", list);
				view02 = getLocalActivityManager().startActivity(
						"GroupListActivity", intent).getDecorView();
			}

			if (getLocalActivityManager().getActivity("FriendListActivity") != null) {
				Log.i(tag, "FriendListActivity不为null");
				Log.i(tag, "friendlist size=" + FriendList.size());
				FriendListActivity friend = (FriendListActivity) getLocalActivityManager()
						.getActivity("FriendListActivity");
				friend.upDate(FriendList);
				if (FriendList.size() > 0) {
					Log.i(tag, "FriendList.friendID:######"
							+ FriendList.get(0).getFriendID());
				} else {
					Log.i(tag, "大于0");
				}
				view01 = friend.getWindow().getDecorView();
			} else {
				Intent intent = new Intent(this, FriendListActivity.class);
				ArrayList list = new ArrayList();
				Log.i(tag, "friendlist size=" + FriendList.size());
				list.add(FriendList);
				intent.putExtra("userId", userId);
				intent.putParcelableArrayListExtra("list", list);
				view01 = getLocalActivityManager().startActivity(
						"FriendListActivity", intent).getDecorView();
			}

			Log.i(tag,
					"ChatMainActivity--------开始加载GroupListActivity和FriendListActivity");
			pageViews.add(view01);
			pageViews.add(view02);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initPager() {
		// imgViewMore=(ImageView)findViewById(R.id.imgView_more);
		// imgViewMore.setOnClickListener(this);
		//
		// imgViewCenter = (ImageView)findViewById(R.id.imgView_person_center);
		// imgViewCenter.setOnClickListener(this);
		try {
			viewPager.setAdapter(new ScrollPageViewAdapter(pageViews));// 添加之后可以屏幕左右滑动
			viewPager.clearAnimation();
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {// 这个是点击不同卡片，跳转不同的页面

						@Override
						public void onPageSelected(int arg0) {
							setSelector(arg0);
						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {

						}

						@Override
						public void onPageScrollStateChanged(int arg0) {

						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 初始化radiobutton，设置每个radiobutton的长度，当刚开始初始化时，第一个角标即发现被初始化为红色，其余为灰色
	 * 把所有radiobutton放在radioGroup上
	 */
	@SuppressLint("ResourceAsColor")
	void InItRadioButton() {
		int width = getWindowManager().getDefaultDisplay().getWidth() / 2;// 求平均长度
		for (int i = 0; i < title.length; i++) {
			RadioButton radioButton = new RadioButton(this, null,
					R.style.radioButton);
			// radioButton.setText(title[i]);这里我省略了
			radioButton.setTextSize(17);
			radioButton.setTextColor(R.color.black);
			radioButton.setWidth(width);
			// radioButton.setHeight(height);
			radioButton.setGravity(Gravity.CENTER);
			radioGroup.addView(radioButton);
		}
	}

	/***
	 * init title
	 * 
	 */
	@SuppressLint("ResourceAsColor")
	void InItTitle() {
		textViews = new ArrayList<TextView>();
		int width = getWindowManager().getDefaultDisplay().getWidth() / 2;
		int height = 70;
		for (int i = 0; i < title.length; i++) {
			textView = new TextView(this);
			textView.setText(title[i]);
			textView.setTextSize(17);
			textView.setTextColor(R.color.black);
			textView.setWidth(width);
			// textView.setHeight(height - 10);
			textView.setGravity(Gravity.CENTER);
			textView.setId(i);
			textView.setOnClickListener(new TextView.OnClickListener() {// 创建监听
				public void onClick(View v) {
					setSelector(v.getId());
				}

			}); // 每次点击textview都有触发响应，都会调用setSelector(int
				// id),id为多少，就跳到哪个textview
			textViews.add(textView);
			// 分割线

			// 我个人认为是这样的，子视图向父视图说明需要在父视图里面占多大。子视图：textview，父视图：是个布局LinearLayout
			// textview向入驻LinearLayout就要通过LayoutParam来传递大小。

			View view = new View(this);
			LinearLayout.LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.width = 1;
			layoutParams.height = height - 40;
			layoutParams.gravity = Gravity.CENTER;
			view.setLayoutParams(layoutParams);
			view.setBackgroundColor(R.color.gray);
			linearLayout.addView(textView);
			// if (i != title.length - 1) {
			// linearLayout.addView(view);
			// }

		}
	}

	/***
	 * 选中效果 个人觉得，这个是起初始化作用，所以一般先赋角标为0的textview，然后角标为0的textview先变成红色，其余的依次弄成灰色
	 * 全部textview都有弄
	 */
	public void setSelector(int id) {
		for (int i = 0; i < title.length; i++) {
			if (id == i) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.grouplist_item_bg_normal);
				textViews.get(id).setBackgroundDrawable(
						new BitmapDrawable(bitmap));
				textViews.get(id).setTextColor(Color.GREEN);// 选中则显示红色
				viewPager.setCurrentItem(i);
			}

			else {
				textViews.get(i).setBackgroundDrawable(new BitmapDrawable());
				textViews.get(i).setTextColor(R.color.black);
			}
		}
	}

	public void onClick(View v) {

	}

	/**
	 * 执行与网络相关任务的子线程
	 */
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			LogUtil.p(tag, "看群------userID:"
					+ BaseActivity.getSelf().getUserID());
			con.sendFindMyGroup(BaseActivity.getSelf().getUserID());
			// con.getOfflineMessage(userId);
		}
	};

	Runnable loginAgainRunnable = new Runnable() {
		public void run() {
			BaseActivity.con = Communication.newInstance();
			con.addReceiveInfoListener(STATE, ChatMainActivity.this);
		}
	};

	/**
	 * 供后台调用的继承于WoliaoActivity类的方法，用于与界面进行交互
	 */
	@Override
	public void processMessage(Message msg) {
		
		gueryDataBase();
		Log.i(tag, "chatmainActivity 被调用");
		Bundle bundle = msg.getData();

		int result = bundle.getInt("result");
		if (msg.what == 0) {// 0表示后台返回的是群列表的查询结果。1表示返回的是查询私信列表返回的结果
			if (result == Config.SUCCESS) {
				ArrayList list = bundle.getParcelableArrayList("groupList");
				GroupList = (List<Group>) list.get(0);
				insertGroupList(GroupList);
			} else if (result == Config.FAILED) {
				showToast("服务器出错");
			} else {
				showToast("您还没有群哦");
			}
		} else if (msg.what == Config.SEND_NOTIFICATION) {
			Log.i(tag, "新消息通知");
			sendNotifycation();
		} else if (msg.what == 1) {
			ChatMessage chatMessage = (ChatMessage) bundle
					.getSerializable("chatMessage");
			boolean isAdd = true;
			if (FriendList == null) {
				Log.i("friendlist", "friend is null");
			} else {
				Log.i("friendlist", "friend is not null");
			}
			for (Friend friend : FriendList) {// 判断这个人是否已经在列表项

				// 判断朋友是否为空*****************************//
				if (friend.getFriendID() == null) {
					Log.i(tag, "friend.getFriendID()为空");
					isAdd = false;
					continue;
				} else if (friend.getFriendID().equals(
						chatMessage.getFriendID())) {
					Log.i(tag, "friend.getFriendID()为不空");
					isAdd = false;
					continue;
				}
			}
			if (isAdd) {
				Log.i(tag, chatMessage.getNickName());
				FriendList.add(new Friend(chatMessage.getFriendID(),
						chatMessage.getNickName(), FileUtil
								.getBitmap(FileUtil.APP_PATH + "/friend" + "/"
										+ userId + "/"
										+ chatMessage.getFriendID() + ".jpg")));
			}
			BaseActivity.saveMessagesToDB(chatMessage.getSelfID(),
					chatMessage.getFriendID(), chatMessage.getNickName(),
					Config.MESSAGE_FROM, chatMessage.getType(),
					chatMessage.getTime(), chatMessage.getContent());
			if (FriendList.size() > 0) {
				Log.i(tag, "FriendList.friendID:%%%%%%%%%"
						+ FriendList.get(0).getFriendID());
			} else {
				Log.i(tag, "大于0");
			}
		}


		InItView();// 更新列表
		Log.i(tag, "InItView()更新");
	}

	public void showToast(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 返回ChatMainActivity时调用，用于重新唤醒该Activity
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		gueryDataBase();
		Log.i(tag, "onRestart 被调用");
		InItView();
	}

	/**
	 * 将从服务器返回的群列表插入数据库，当且仅当第一次注册登录或删除本地数据库后登录调用
	 * 
	 * @param groupList
	 */
	public void insertGroupList(List<Group> groupList) {
		for (Group group : groupList) {
			// 先将图片存入本地sd卡
			File file = FileUtil.createFile(userId,
					Integer.parseInt(group.getGroupId()));
			FileUtil.saveBitmap(file, group.getBm());
			// 将该群信息插入数据库
			saveToDB(userId, Integer.parseInt(group.getGroupId()),
					group.getGroupName());
		}
	}

	/**
	 * 保存数据到数据库
	 * 
	 * @param userId
	 * @param groupId
	 * @param groupName
	 */
	public void saveToDB(String userId, int groupId, String groupName) {
		ContentValues values = new ContentValues();
		values.put("user_Id", userId);
		values.put("group_Id", groupId);
		values.put("groupName", groupName);
		dbUtil.insertGroupInfo(values);
	}

	//按两次才退出程序
//		 public boolean onKeyDown(int keyCode, KeyEvent event) {
//	         if (keyCode == KeyEvent.KEYCODE_BACK) {
//	        	 onBackPressed();
//	                 return true;
//	         }
//	         return super.onKeyDown(keyCode, event);
//	 }


	/**
	 * 将私聊的离线消息存入数据库
	 * 
	 * @param userID
	 * @param friendID
	 * @param direction
	 * @param type
	 * @param time
	 * @param content
	 */
	// public void saveMessagesToDB(String userID,String friendID,String
	// nickName,int direction,int type,String time,String content){
	// Log.i(tag, "短信添加到messages成功，把短信存入本地的数据库的方法被调用");
	// // DatabaseUtil db = new DatabaseUtil(this);
	// ContentValues values = new ContentValues();
	// values.put("self_Id",userID);
	// values.put("friend_Id",friendID);
	// values.put("friend_nickName", nickName);
	// values.put("direction",direction);
	// values.put("type",type);
	// values.put("time",time);
	// values.put("content",content);
	// values.put("showTime",0);
	// dbUtil.insertMessages(values);
	// Log.i(tag, "刚把短信插入本地数据库，我来检查一下");
	// }
	@Override
	public boolean isChatting(Object info) {
		ChatMessage chatMessage = (ChatMessage) info;// 将object转为消息
		Log.i(tag, "PersonChatActivity----chatMessage.getContent："
				+ chatMessage.getContent());
		Log.i(tag, "PersonChatActivity----chatMessage.getFriendID()："
				+ chatMessage.getFriendID());
		Log.i(tag, "PersonChatActivity----chatMessage.getFriendID()"
				+ chatMessage.getFriendID());
		return true;
	}	
	
//	@Override
//    public void onBackPressed() {
////      super.onBackPressed();
//        finish();
//    }
	

	
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		if(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext())==null){
			slidingTitleLayout.hideBadgeView();
		}else{
			slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
		}
	}


}
