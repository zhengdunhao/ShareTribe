package com.atm.chatonline.chat.ui;
/**
 * 查看群资料（包括群成员）
 * author--李
 */
import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.atm.chatonline.chat.adapter.GroupMemberAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;



/**
 * @author Jackbing
 *
 */
public class GroupChatLookDataActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	private final static String tag="GroupChatLookDataActivity";
	private String groupIds;//群id
	private String[] manager=new String[]{"群设置","管理群"};
	private int[] images=new int[]{R.drawable.group_look_image_item4};//邀请的图片
	private String[] name=new String[]{" "};//邀请的图片下方显示空格或不显示字符串
	private Handler handler;
	private GridView grid;
	private Button btnOnBack;
	private LinearLayout settingLayout,managerLayout;
	private List<Friend> listImgItems;//群成员列表
	private GroupMemberAdapter memberAdapter;//GridView的Adapter
	private Friend friend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_look_data_view);
		initComponent();
		groupIds=getIntent().getStringExtra("groupId");
		initData(groupIds);
		grid=(GridView) findViewById(R.id.group_member_gridview);
		listImgItems=new ArrayList<Friend>();
		friend=new Friend(name[0],BitmapFactory.decodeResource(getResources(), R.drawable.group_look_image_item4));
		listImgItems.add(friend);
		initAdapter();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0){
					initAdapter();
				}
			}
		};
	}
	
	/**
	 * 
	 */
	private void initComponent() {
		btnOnBack=(Button) findViewById(R.id.look_group_data_back);
		settingLayout=(LinearLayout) findViewById(R.id.group_setting_layout);
		managerLayout=(LinearLayout) findViewById(R.id.group_manager_layout);
		grid=(GridView) findViewById(R.id.group_member_gridview);
		btnOnBack.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
		managerLayout.setOnClickListener(this);
		grid.setOnItemClickListener(this);
		
	}
	private void initData(String groupIds) {//访问服务器返回
		//先将groupId转为整形，服务器接收的群id是整形数据
		new Thread(runnable).start();
	}
	
	
	public void initAdapter(){
		memberAdapter=new GroupMemberAdapter(GroupChatLookDataActivity.this,listImgItems);
		grid.setAdapter(memberAdapter);
		memberAdapter.notifyDataSetChanged();
	}
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			int groupId=Integer.parseInt(groupIds);
			con.sendLookGroupData(groupId);
		}
		
	};
	@Override
	public void processMessage(Message msg) {
		Bundle bundle=msg.getData();
		int result=bundle.getInt("result");
		if(result==Config.SUCCESS){
			ArrayList arrayList=bundle.getParcelableArrayList("arrayList");
			listImgItems=(ArrayList<Friend>)arrayList.get(0);
			listImgItems.add(friend);
			handler.sendEmptyMessage(0);
			
		}else if(result==Config.FAILED)
		{
			Log.i("GroupChatLookDataActivity", "查看群资料失败");
		}else{
			Log.i("GroupChatLookDataActivity", "查找不到该群的信息");
		}
		
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.look_group_data_back){
			this.onBackPressed();
		}else if(id==R.id.group_setting_layout){
			Log.i(tag, "群设置被点击");
		}else if(id==R.id.group_manager_layout){
			Log.i(tag, "管理群被点击");
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			Log.i(tag, "群成员列表被点击");
	}
	
}
