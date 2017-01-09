package com.atm.chatonline.usermsg.ui;
/**
 *
 * 我的消息里面评论（包括回复）我的界面
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.afinal.simplecache.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.ApplyAdapter;
import com.atm.chatonline.usermsg.adapter.ApplyAdapter.ViewHolder;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.bean.ApplyMessageData;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.atm.chatonline.usermsg.util.MyMsgReceivePhoto;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ApplyMsg extends BaseActivity implements OnClickListener{

	private List<ApplyMessage> list;
	private String userId;
	private ApplyAdapter adapter;
	private PullToRefreshListView plv;
	private ProgressBar pro;
	private ACache mCache;
	private boolean hasCache=false;
	private String tag="Applymsg";
	private TextView applymsg_hint;
	private ApplyMessageData data;//包含了评论消息的列表
	private TextView delete;//删除，一开始不会显示
	private boolean isShowCheckBox=false;
	private Map<Integer,Integer> map=new HashMap<Integer,Integer>();
	//private SparseIntArray arr=new SparseIntArray();//用SparseIntArray替换Map
	//private Integer type=0;//0--评论，1--@我，2--系统消息
	
	//发送获取我的评论消息的请求进程,为了之后避免因为Activity销毁后子线程没有被销毁，所以把这个线程写在前面
	Runnable myMsgRunnable=new Runnable() {
			
			@Override
			public void run() {
				Log.i(tag, "获取评论消息");
				ApplyMsg.con.reqMyMsg(userId,1);	
			}
	};
	

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_applymsg_view);
		Log.e("www", "apply");
		initUI();
		initData();
	}
	
	private void initData() {
		
		mCache=ACache.get(getApplicationContext());
		//这里还没有判断con是否存在，假设已经存在
		//获取userId
		userId=BaseActivity.getSelf().getUserID();
		//发送请求到服务端,理论上应该先查询数据缓存，如果有则先加载缓存，显示，然后再获取新的消息再刷新；如果没有缓存则显示正在刷洗进度条，然后再加载下载的数据
		initCache();
		//获取评论消息
		new Thread(myMsgRunnable).start();	
	}

	/**
	 * 获取组件对象，设置监听等初始化工作
	 */
	private void initUI() {
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.applymsg_probar);
		applymsg_hint=(TextView) findViewById(R.id.applymsg_hint);
		delete=(TextView) findViewById(R.id.delete_or_not);
		btn.setOnClickListener(this);
		delete.setOnClickListener(this);

	}

	/**
	 * 获取缓存
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	private void initCache() {
		data=getCacheData();
		
		if(data!=null){
			//如果list的大小大于0，则显示数据
			list=data.getApplyMessage();
			LogUtil.i("list size="+list.size());
			for(ApplyMessage applyMeesage:list){
				applyMeesage.getContent().setHeadImage(mCache.getAsBitmap(applyMeesage.getContent().getUserId())
						);
			}
			hasCache=true;
			initAdapter();
		}else{
			Log.i("applyMsg", "[message cache  ]:null");
		}
		
		
	}

	private void initAdapter() {
		
		plv=(PullToRefreshListView)findViewById(R.id.apply_msg_list_view);
		adapter=new ApplyAdapter(getApplicationContext(), R.layout.apply_msg_listview_item, list,isShowCheckBox); 
		plv.setAdapter(adapter);
		
		//刷新，发送是否有新消息的请求 Config.ishaveNewMessage
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				new Thread(myMsgRunnable).start();	
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
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
		
		//为item添加监听
		plv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(!isShowCheckBox){
					ExtendsIntent intent = new ExtendsIntent(ApplyMsg.this,
							BBSPostDetailView.class, list.get(position - 1)
									.getContent().getEssayId(), null, null, 1);
					startActivity(intent);
				}else{
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
	
	protected void isShowCheckBox(boolean isShow) {
		adapter.setShowCheckBox(isShow);
		adapter.notifyDataSetChanged();
		
	}

	/**
	 * 获取缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ApplyMessageData getCacheData(){
		return (ApplyMessageData) mCache.getAsObject(CacheUtils.APPLY_MSG);
//		CacheData  data =cacheManager.getCache(CacheUtils.APPLY_MSG);
//		if(data==null){
//			return null;
//		}
//		return (List<ApplyMessage>) data.getData();
	}
	
	/**
	 * 保存缓存
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(ApplyMessageData data){
		mCache.put(CacheUtils.APPLY_MSG, data);
//		CacheData cacheData=new CacheData(CacheUtils.APPLY_MSG, data);
//		cacheManager.addCache(cacheData);
	}
	
	/**
	 * 等待后台返回消息，并构造评论消息的List，包括构造接收消息的时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message msg) {
		Bundle bundle =msg.getData();
		String json=bundle.getString("MyMessage");
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArr=(JSONArray) jsonObject.get("message");
			
			if(jsonArr.length()>0){//有新消息
				
				//如果已经有缓存，则把新消息添加到后面，否则则直接直接转换为data
				
				if(data==null){
					data=new Gson().fromJson(json, ApplyMessageData.class);
					
					//消息的时间是最早的先发过来的，所以要逆序排序
					List<ApplyMessage> temp=(List<ApplyMessage>)data.getApplyMessage();
					if(list==null){
						list=new ArrayList<ApplyMessage>();
					}
					for(int i=temp.size()-1;i>=0;i--){
						list.add(temp.get(i));
					}
					data.setApplyMessage(list);
				}else{//把消息添加到后面
					ApplyMessageData temp=new Gson().fromJson(json, ApplyMessageData.class);
					
					if(list!=null){
						for(int i=0;i<temp.getApplyMessage().size();i++){
							if(!list.contains(temp.getApplyMessage().get(i))){
								list.add(0, temp.getApplyMessage().get(i));
							}
							
						}
					}
					
					data.setApplyMessage(list);
				}
				new GetPhotoTask().execute();
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
	
	
	/**
	 * 异步加载头像
	 */
	@SuppressWarnings("unused")
	private class GetPhotoTask extends AsyncTask<Void, Void, String> {

		public GetPhotoTask() {
		}

		protected String doInBackground(Void... params) {
			loadPhoto();
			return null;
		}

		

		protected void onPostExecute(String result) {
			//保存缓存
			addCacheData(data);
			LogUtil.i("111111111111");
			//如果有缓存则更新数据，否则创建数据适配器
			if(hasCache){
				adapter.notifyDataSetChanged();
			}else{
				initAdapter();
				hasCache=true;
			}
			plv.onRefreshComplete();
		}
	}

	//下载头像图片，顺便更新头像
	public void loadPhoto(){
		MyMsgReceivePhoto recPho=new MyMsgReceivePhoto();
		for(ApplyMessage applyMeesage:list){
			applyMeesage.getContent().setHeadImage(recPho.getPhoto(mCache, applyMeesage.getContent().getUserId(), applyMeesage.getContent().getAvatar()));
		}
		//设置为null,避免长时间占用内存，减少oom的产生概率
		recPho=null;
		LogUtil.i("111111111111^^^^^^^^^^^^^");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			LogUtil.i("click back button!");
			finish();
			break;
		case R.id.delete_or_not:
			LogUtil.i("indexOfItem 的长度="+map.size());
			Stack<ApplyMessage> temp=new Stack<ApplyMessage>();
			for (Map.Entry<Integer, Integer> m : map.entrySet()) {
				LogUtil.i("indexOfItem 的position="+m.getValue());
				temp.push(list.get(m.getValue()-1));
			}
			
			while(!temp.isEmpty()){
				ApplyMessage obj=temp.pop();
				list.remove(obj);
				data.getApplyMessage().remove(obj);
			}
			addCacheData(data);
			map.clear();
			isShowCheckBox=false;
			adapter.setShowCheckBox(isShowCheckBox);//取消显示checkbox
			LogUtil.i("list size="+list.size());
			adapter.notifyDataSetChanged();//更新数据
			delete.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
		
}
