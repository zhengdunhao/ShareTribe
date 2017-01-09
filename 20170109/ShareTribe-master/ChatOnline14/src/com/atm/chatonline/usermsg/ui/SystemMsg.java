package com.atm.chatonline.usermsg.ui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.afinal.simplecache.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.SystemMsgAdapter;
import com.atm.chatonline.usermsg.adapter.SystemMsgAdapter.ViewHolder;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.bean.Notification;
import com.atm.chatonline.usermsg.bean.NotificationContent;
import com.atm.chatonline.usermsg.bean.SystemMessageData;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 * 我的消息里面系统通知界面
 */

public class SystemMsg extends BaseActivity implements OnClickListener{

	private PullToRefreshListView plv;
	private List<Notification> list;
	private String userId;
	private SystemMsgAdapter adapter;
	private ProgressBar pro;
	private boolean hasCache=false;
	private String tag="Applymsg";
	private TextView applymsg_hint;
	private ACache mCache;
	private SystemMessageData systemMessageData;
	private TextView delete;//删除，一开始不会显示
	private boolean isShowCheckBox=false;
	private Map<Integer,Integer> map=new HashMap<Integer,Integer>();
	//private SparseIntArray arr=new SparseIntArray();//用SparseIntArray替换Map
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_systemmsg_view);
		initView();
		//testAddData();
		initData();
	}
	
	/**
	 * 初始化缓存，获取userid，以便作为网络访问的参数
	 */
	private void initData() {
		mCache=ACache.get(getApplicationContext());
		userId=BaseActivity.getSelf().getUserID();
		initCache();
		//获取系统消息
		new Thread(myMsgRunnable).start();
	}

	/**
	 * 初始化组件
	 */
	private void initView(){
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.systemmsg_probar);
		applymsg_hint=(TextView) findViewById(R.id.systemmsg_hint);
		delete=(TextView) findViewById(R.id.delete_or_not);
		plv=(PullToRefreshListView)findViewById(R.id.systemmsg_list_view);
		
		btn.setOnClickListener(this);
		delete.setOnClickListener(this);
	}
	@SuppressWarnings({ "deprecation", "static-access" })
	private void initCache() {
		systemMessageData=getCacheData();
		if(systemMessageData!=null){
			//如果list的大小大于0，则显示数据
			list=systemMessageData.getMessage();
			LogUtil.i("list size="+list.size());
			hasCache=true;
			initAdapter();
		}
	}
	
//	//测试方法，随时可以删除
//	public void testAddData(){
//		if(list==null){
//			list=new ArrayList<Notification>();
//			list.add(new Notification(1,1,"131544228",new NotificationContent("title", "bjbhjkbhjc", "78-90")));
//			
//		}
//		initAdapter();
//	}
	private void initAdapter() {
		
		adapter=new SystemMsgAdapter(getApplicationContext(), R.layout.usermsg_systemmsg_listview_item, list,isShowCheckBox);
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
							LogUtil.i("用户点击了item");
							if(!isShowCheckBox){
								LogUtil.i("tiaozhuan");
								Intent intent=new Intent(SystemMsg.this,SystemMessageDetail.class);
								Bundle bundle=new Bundle();
								bundle.putSerializable("SystemMessage", list.get(position).getContent());
								intent.putExtras(bundle);
								startActivity(intent);
								
							}else{
								LogUtil.i("判断checkbox 是否被选中");
								ViewHolder viewHolder=(ViewHolder) view.getTag();
								viewHolder.checkBox.toggle();
								if(viewHolder.checkBox.isChecked()){
									if(!map.containsKey(position)){
										map.put(position, position);
									}
								}else{
									if(map.containsKey(position)){
										map.remove(position);
									}
								}
							}
					}
				});
				plv.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// 显示checkbox，并刷新UI，显示删除的Textview
						if(!isShowCheckBox){
							isShowCheckBox=true;
							isShowCheckBox(isShowCheckBox);
						}
						delete.setVisibility(View.VISIBLE);
						return true;
					}
					
				});
				
				pro.setVisibility(View.GONE);
	}

	private void isShowCheckBox(boolean isShow){
		adapter.setShowCheckBox(isShow);
		adapter.notifyDataSetChanged();
	} 
	
	/**
	 * 获取缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private SystemMessageData getCacheData(){
		return (SystemMessageData) mCache.getAsObject(CacheUtils.SYSTEM_MSG);
	}
	
	/**
	 * 保存缓存
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(SystemMessageData data){
		mCache.put(CacheUtils.SYSTEM_MSG, data);
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
				
				if (systemMessageData==null) {
					systemMessageData=new Gson().fromJson(json, SystemMessageData.class);
					List<Notification> temp=systemMessageData.getMessage();
					if(list==null){
						list=new ArrayList<Notification>();
					}
					for(int i=temp.size()-1;i>=0;i--){
						list.add(temp.get(i));
					}
					systemMessageData.setMessage(list);
				}else{
					SystemMessageData temp=new Gson().fromJson(json, SystemMessageData.class);
					if(list!=null){
						for(int i=0;i<temp.getMessage().size();i++){
							if(!list.contains(temp.getMessage().get(i))){
								list.add(0, temp.getMessage().get(i));
							}
							
						}
					}
					
					systemMessageData.setMessage(list);
				}
				//保存缓存
				addCacheData(systemMessageData);
				
				//如果有缓存则更新数据，否则创建数据适配器
				if(hasCache){
					adapter.notifyDataSetChanged();
				}else{
					initAdapter();
					hasCache=true;
				}
				applymsg_hint.setVisibility(View.GONE);
			}else{
				if(!hasCache){
					applymsg_hint.setVisibility(View.VISIBLE);
				}else{
					applymsg_hint.setVisibility(View.GONE);
				}
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
		case R.id.delete_or_not:
			LogUtil.i("indexOfItem 的长度="+map.size());
			Stack<Notification> temp=new Stack<Notification>();
			for (Map.Entry<Integer, Integer> m : map.entrySet()) {
				LogUtil.i("indexOfItem 的position="+m.getValue());
				temp.push(list.get(m.getValue()-1));
			}
			
			while(!temp.isEmpty()){
				Notification obj=temp.pop();
				list.remove(obj);
				systemMessageData.getMessage().remove(obj);
			}
			isShowCheckBox=false;
			map.clear();
			addCacheData(systemMessageData);
			adapter.setShowCheckBox(isShowCheckBox);//取消显示checkbox
			adapter.notifyDataSetChanged();//更新数据
			delete.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
			if(isShowCheckBox){
				LogUtil.i("isshow ="+isShowCheckBox);
				isShowCheckBox=false;
				adapter.setShowCheckBox(false);
				adapter.notifyDataSetChanged();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
