package com.atm.chatonline.usermsg.ui;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atm.charonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.SystemMsgAdapter;
import com.atm.chatonline.usermsg.bean.Notification;
import com.atm.chatonline.usermsg.bean.SystemMessageData;
import com.atm.chatonline.usermsg.util.CacheData;
import com.atm.chatonline.usermsg.util.CacheManager;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 * 我的消息里面系统通知界面
 */

public class SystemMsg extends BaseActivity implements OnClickListener{

	//private View mView;
	private PullToRefreshListView plv;
	private List<Notification> list;
	private String userId;
	private SystemMsgAdapter adapter;
	private ProgressBar pro;
	private Handler handler;
	private CacheManager cacheManager;
	private boolean hasCache=false;
	private String tag="Applymsg";
	private TextView applymsg_hint;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_systemmsg_view);
		initView();
		userId=BaseActivity.getSelf().getUserID();
		
		initCache();
		//initAdapter();
		//获取系统消息
		new Thread(myMsgRunnable).start();
		
	}
	
	private void initView(){
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.systemmsg_probar);
		applymsg_hint=(TextView) findViewById(R.id.systemmsg_hint);
		btn.setOnClickListener(this);
		plv=(PullToRefreshListView)findViewById(R.id.systemmsg_list_view);
	}
	@SuppressWarnings({ "deprecation", "static-access" })
	private void initCache() {
		cacheManager=CacheManager.getInstance();
		cacheManager.init(getApplicationContext());
		list=getCacheData();
		LogUtil.i("list:"+(list==null));
		if(list!=null&&list.size()>0){
			//如果list的大小大于0，则显示数据
			LogUtil.i("list size="+list.size());
//			for(Notification applyMeesage:list){
//				applyMeesage.getContent().setHeadImage(new BitmapDrawable(FileUtil.ByteToBitmap(((byte[])cacheManager.getCache(applyMeesage.getContent().getUserId()).getData()))));
//			}
			hasCache=true;
			initAdapter();
		}
	
		
	}
	private void initAdapter() {
		
		adapter=new SystemMsgAdapter(getApplicationContext(), R.layout.usermsg_systemmsg_listview_item, list);
		plv.setAdapter(adapter);
		
		//刷新，发送是否有新消息的请求 Config.ishaveNewMessage
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
						
					//获取评论消息
					new Thread(myMsgRunnable).start();
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
						//获取评论消息
					new Thread(myMsgRunnable).start();
						
				}
			});
		// 设置PullRefreshListView上提加载时的加载提示
				plv.getLoadingLayoutProxy(false, true).setPullLabel(
								"上拉加载...");
				plv.getLoadingLayoutProxy(false, true).setRefreshingLabel(
								"正在加载...");
				plv.getLoadingLayoutProxy(false, true).setReleaseLabel(
								"释放加载...");
						// 设置PullRefreshListView下拉加载时的加载提示
				plv.getLoadingLayoutProxy(true, false).setPullLabel(
								"下拉加载...");
				plv.getLoadingLayoutProxy(true, false).setRefreshingLabel(
						        "正在加载...");
				plv.getLoadingLayoutProxy(true, false).setReleaseLabel(
								"释放加载...");
				plv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						ExtendsIntent intent = new ExtendsIntent(ApplyMsg.this,
//								BBSPostDetailView.class, list.get(position - 1)
//										.getContent().getEssayId(), null, null, 1);
//						startActivity(intent);
						Intent intent=new Intent(SystemMsg.this,SystemMessageDetail.class);
						Bundle bundle=new Bundle();
						bundle.putSerializable("SystemMessage", list.get(position).getContent());
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
				
				pro.setVisibility(View.GONE);
	}

	/**
	 * 获取缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Notification> getCacheData(){
		CacheData  data =cacheManager.getCache(CacheUtils.SYSTEM_MSG);
		if(data==null){
			return null;
		}
		return (List<Notification>) data.getData();
	}
	
	/**
	 * 保存缓存
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(List<Notification> data){
		CacheData cacheData=new CacheData(CacheUtils.SYSTEM_MSG, data);
		cacheManager.addCache(cacheData);
	}
	
	@Override
	public void processMessage(Message msg) {
		
		Bundle bundle =msg.getData();
		String json=bundle.getString("MyMessage");
		
		LogUtil.i(json);
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArr=(JSONArray) jsonObject.get("message");
			plv.onRefreshComplete();//不管有没有新消息，都要结束刷新
			if(jsonArr.length()>0){//有新消息
				SystemMessageData data=new Gson().fromJson(json, SystemMessageData.class);
				list=(List<Notification>)data.getMessage();
				System.out.println(list.size());
				//new GetPhotoTask().execute();
				//initAdapter();
				//保存缓存
				addCacheData(list);
				LogUtil.i("111111111111");
				//如果有缓存则更新数据，否则创建数据适配器
				if(hasCache){
					adapter.notifyDataSetChanged();
				}else{
					initAdapter();
					hasCache=true;
				}
				
				applymsg_hint.setVisibility(View.GONE);
			}else{
				applymsg_hint.setVisibility(View.VISIBLE);
				pro.setVisibility(View.GONE);
			}
			
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	Runnable myMsgRunnable = new Runnable(){

		@Override
		public void run() {
			
			LogUtil.i("获取系统消息");
			ApplyMsg.con.reqMyMsg(userId,2);	
		}
		
	};
	
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
