package com.atm.chatonline.bbs.activity.bbs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atm.chatonline.bbs.bean.Datas;
import com.atm.chatonline.bbs.bean.User;
import com.atm.chatonline.bbs.adapter.UserAdapter;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.ReceivePhoto;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 * @类 com.atm.chatonline.activity.bbs ---BBSListView
 * @作用 该类用于所需帖子列表的显示
 * @作者 陈小二
 * @时间 2015-8-16
 * 
 */
public class SearchUserView extends BaseActivity implements
	OnClickListener{

	//传递给服务器端所需的参数
	private static String id;
	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		SearchUserView.id = id;
	}
	private String tip = "";
	private String relativePath;
	
	private BBSConnectNet bBSConnectNet;
	private List<User> userList = new ArrayList<User>();
	private UserAdapter userAdapter;
	private PullToRefreshListView plv;
	private int userNums = 0;
	private String response;
	private String cookie;
	private LinearLayout layout;
	private static TextView bbsWait;
	private int first=0;
	private static Handler handler;
	public static Handler getHandler() {
		return handler;
	}

	public static void setHandler(Handler handler) {
		SearchUserView.handler = handler;
	}

	public static TextView getBbsWait() {
		return bbsWait;
	}

	public void setBbsWait(TextView bbsWait) {
		this.bbsWait = bbsWait;
	}
	private TextView bbsSearchGone;
	private Button btnBack;
	

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_list_view);
		initParams();
        //进入等待加载界面。。
		initView();
        bbsWait = (TextView) findViewById(R.id.bbs_wait);
		if( tip.equals("depart")) {
			new GetDataTask().execute();
			new GetPhotoTask().execute();
        }
		else {
//		new GetDataTask().execute();
//		new GetPhotoTask().execute();
		bbsWait.setVisibility(View.GONE);
		try {
			initData();
			loadPhoto();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		}
		handler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
					case Config.SEARCHESSAY:
						userNums = 0;
						new GetDataTask().execute();
						new GetPhotoTask().execute();
//						try {
//							initData();
//							loadPhoto();
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						userAdapter.notifyDataSetChanged();
//						LogUtil.d("userAdapter.notifyDataSetChanged();");
						
						break;
					case Config.SEARCHUSER:
						userAdapter.notifyDataSetChanged();
						break;
					case 0101:
						bbsSearchGone.setVisibility(View.VISIBLE);
				}
			}
		};
	}
	
	/**
	 * 初始化参数：
	 * 获取由Bundle传入的参数。
	 * 该参数是用于传递给服务器端所需的参数。
	 * 四个参数：id;tip;relativePath
	 */
	private void initParams() {
		
		LogUtil.d("initParams");
		
		Bundle bundle = this.getIntent().getExtras();
		this.relativePath = bundle.getString("relativePath");
		this.id = bundle.getString("id");
		this.tip = bundle.getString("tip");

		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		LogUtil.d("cookie:"+cookie);
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		
        LogUtil.d("进入initView");

        //搜索不存在时显示。
        bbsSearchGone = (TextView) findViewById(R.id.bbs_search_gone);
        //判断是否有标题栏
        if( tip.equals("depart")) {
        	LogUtil.d("tip.equals(depart");
        	layout = (LinearLayout) findViewById(R.id.title);
        	layout.setVisibility(View.VISIBLE);
        }

		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		// 实例化PullToRefreshListView，并配置适配器
		plv = (PullToRefreshListView) findViewById(R.id.lv_home);
		userAdapter = new UserAdapter(this,R.layout.bbs_search_user_list_item,userList);
		plv.setAdapter(userAdapter);

		//为该列表设置上拉下拉监听
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				userNums = 0;
				new GetDataTask().execute();
				new GetPhotoTask().execute();
			}
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
				new GetPhotoTask().execute();
			}
		});
		
		//设置PullRefreshListView上提加载时的加载提示
        plv.getLoadingLayoutProxy(false, true).setPullLabel("上拉，上拉，上拉，O(∩_∩)O哈哈~");
        plv.getLoadingLayoutProxy(false, true).setRefreshingLabel("伦家正在努力加载ing。↖(^ω^)↗");
        plv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开啦，我就加载，(*^__^*) 嘻嘻……");
        // 设置PullRefreshListView下拉加载时的加载提示
        plv.getLoadingLayoutProxy(true, false).setPullLabel("下拉，下拉，下拉，O(∩_∩)O哈哈~");
        plv.getLoadingLayoutProxy(true, false).setRefreshingLabel("伦家正在努力刷新ing。↖(^ω^)↗");
        plv.getLoadingLayoutProxy(true, false).setReleaseLabel("松开啦，我就刷新，(*^__^*) 嘻嘻……");
	
        plv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//传递essayid给帖子。
				LogUtil.d("帖子被点击");
				ExtendsIntent intent = new ExtendsIntent( SearchUserView.this , BBSListView.class ,
						userList.get(position - 1).getUserId(), "essay_publishedEssay.action" , "depart" , 1);
				LogUtil.d("帖子被点击111");
				if(userList.get(position-1).getUserId()!=null)
				LogUtil.d(userList.get(position - 1).getUserId());
				else
					LogUtil.d("用户Id不存在");
				startActivity(intent);
				LogUtil.d("queue.size:"+BaseActivity.queue.size());
				LogUtil.d("帖子被点击2");
			}
        	
		});
        LogUtil.d("走完initView");
        
	}

	/**
	 * 初始化数据
	 * @throws JSONException 
	 */
	private void initData() throws JSONException {
		
		LogUtil.d("initData");
		loadData();
	}

	/**
	 * 从服务器端获取数据
	 * @return response：json的数据
	 */
	private String getResponseFromNet() {
		LogUtil.d("getResponseFromNet");
		Thread thread = new Thread(new Runnable(){
			public void run(){
				bBSConnectNet = new BBSConnectNet(tip , id ,userNums,relativePath,cookie);
				LogUtil.d("getResponseFromNet+BBSConnectNet结束");
				response = bBSConnectNet.getResponse();
				LogUtil.d("Gson:"+response);
				LogUtil.d("bBSConnectNet.getResponse()结束");
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			LogUtil.d("getResponseFromNet-线程被打断");
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 异步加载数据
	 */
	private class GetDataTask extends AsyncTask<Void , Void , String>{
		
		
		public GetDataTask(){
		}
		
		protected String doInBackground(Void... params) {
					try {
						//下载数据
						loadData();
					} catch (JSONException e) {
						e.printStackTrace();
					}
			return null;
		}
	
		protected void onPostExecute(String result){
			//刷新列表
			bbsWait.setVisibility(View.GONE);
			LogUtil.d("userAdapter.notifyDataSetChanged();");
			userAdapter.notifyDataSetChanged();
			plv.onRefreshComplete();
			Message msg = new Message();
			msg.what = Config.MAINVIEW_UPDATEUI;
//			if(tip=="first"&&first==1) {
//			BBSMainView.getHandler().sendMessage(msg);
//			first++;
//			LogUtil.d(first+"");
//			}
//			Log.i(tag, "bbsAdapter.notifyDataSetChanged();_______3");
		}
	}

	/**
	 * 异步加载数据
	 */
	private class GetPhotoTask extends AsyncTask<Void , Void , String>{
		
		
		public GetPhotoTask(){
		}
		
		protected String doInBackground(Void... params) {
			loadPhoto();
			return null;
		}
	
		protected void onPostExecute(String result){
			//刷新列表
			
			userAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * 异步更新的加载数据
	 * @param page2
	 * @throws JSONException 
	 */
	public void loadData() throws JSONException {
		
		LogUtil.d("loadData");
		
		if(userNums == 0){
			//先把列表清空
			LogUtil.d("bbsList.clear()");
			userList.clear();
		}
		response = getResponseFromNet();
		LogUtil.d("loadData+1");
		JSONObject obj = new JSONObject(response);
		String array = obj.getString("user");
		LogUtil.p("user", array);
		getUsers(array);
//		List<User> users = new Gson().fromJson(array, new TypeToken<List<User>>() {}.getType());
//		Datas data = new Gson().fromJson(response,Datas.class);
//		LogUtil.p("data", data.toString());
		//先判断，再进行加载
//		switch(data.getTip()) {
//		case "没有结果":
//			bbsSearchGone.setVisibility(View.VISIBLE);
//			LogUtil.d("data.getTip():"+data.getTip());
////			MyToast toast=MyToast.makeText(getApplicationContext(), data.getTip(), Toast.LENGTH_SHORT);
////			toast.show();
//			break;
//		default:
//			if(data.getUser().isEmpty()) {
//		if(users.isEmpty()) {
////				LogUtil.p("data.getUser().size()",data.getUser().size()+"");
////				Message msg = new Message();
////				msg.what=0101;
////				handler.sendMessage(msg);
//
//				bbsSearchGone.setVisibility(View.VISIBLE);
//				}else {
//					bbsSearchGone.setVisibility(View.INVISIBLE);
////			addData(data);
//					addDatas(users);
//			}
//		}
		LogUtil.d("loadData+3");
		
		/*//获取照片。
		for(BBS bbs: bbsList){
			bbs.setHeadImage(new ReceivePhoto(bbs.getHeadImagePath()).getPhoto());
			//获取标签名和标签颜色
			bbs.setLabName0(bbs.getLabName().split("\\*#"));
			String[] colors = bbs.getLabColor().split("\\*");
			int[] color = new int[colors.length];
			for(int i = 0 ; i < colors.length; i++){
				color[i] = Color.parseColor(colors[i]);
			}
			bbs.setLabColor0(color);
		}*/
		LogUtil.d("loadData走完");
	}
	
	/**
	 * @param array
	 */
	private void getUsers(String array) {
		try {
			JSONArray jsonArray = new JSONArray(array);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				User user = new User();
				user.setdName(obj.getString("dName")) ;
				user.setHeadImagePath(obj.getString("headImagePath"));
				user.setNickname(obj.getString("nickname"));
				user.setUserId(obj.getString("userId"));
				if(!userList.contains(user))
					userList.add(user);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取照片。
	 */
	public void loadPhoto() {
		int count=1;
		for(User user: userList){
			LogUtil.d( "第"+count+++"条记录");
			user.setHeadImage(new ReceivePhoto(user.getHeadImagePath()).getPhoto());
		}
	}

	/**
	 * 加载数据
	 * 将BBSFirst中的BBS列表项加到BBS bean类中，并获取图片
	 * @param dataFromJson : BBSFirst的实例化
	 */
	private void addData(Datas data) {

		LogUtil.d("addData");
		if(data.getUser()!=null)
		LogUtil.p("data.getUser()",data.getUser().size()+"");
		//将BBSFirst中的BBS列表项加到BBS bean类中
		for(User user :data.getUser()){
			if(!userList.contains(user)){
				userList.add(user);
				userNums++;
				LogUtil.d( "userNums:"+userNums);
			}
		}

		LogUtil.d( "bbsList.size()"+userList.size());
	}
	/**
	 * 加载数据
	 * 将BBSFirst中的BBS列表项加到BBS bean类中，并获取图片
	 * @param dataFromJson : BBSFirst的实例化
	 */
	private void addDatas(List<User> users) {

		LogUtil.d("addData");
		if(!users.isEmpty())
		LogUtil.p("data.getUser()",users.size()+"");
		//将BBSFirst中的BBS列表项加到BBS bean类中
		for(User user :users){
			if(!userList.contains(user)){
				userList.add(user);
				userNums++;
				LogUtil.d( "userNums:"+userNums);
			}
		}

		LogUtil.d( "bbsList.size()"+userList.size());
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			SearchUserView.this.onBackPressed();
			break;
		}
	}
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

}
