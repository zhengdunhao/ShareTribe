package com.atm.chatonline.usercenter.activity.usercenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.atm.chatonline.bbs.bean.BBS;
import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.bbs.adapter.BBSAdapter;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.ReceivePhoto;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usercenter.bean.MyLabelOfBBS;
import com.atm.chatonline.usercenter.util.HandleLabel;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class LabelOfBBS extends BaseActivity implements OnClickListener{
	private String userTag,cookie,tag="LabelOfBBS",response,relativePath="essay_tagEssay.action";
	//private ProgressBar probar;
	private BBSAdapter bbsAdapter;
	private PullToRefreshListView plv;
	private Button btnBack;
	private int bbsNums=0;
	private TextView failedTextView,textview_wait;
	private List<BBS> bbsList= new ArrayList<BBS>();
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_center_labelofbbs_main_view);
		initParams();
		initView();
		initData();
		
	}

	public void initParams(){
		Intent intent=getIntent();
		cookie=intent.getStringExtra("cookie");
		userTag=intent.getStringExtra("userTag");
		Log.i(tag, "userTag:"+userTag+",  cookie:"+cookie);
	}
	private void initData() {
		Log.i(tag, "initData");
		try {
			loadData();
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void initView(){
		//probar=(ProgressBar)findViewById(R.id.label_of_bbs_pro);
		TextView title=(TextView)findViewById(R.id.labelofbbs_title);
		textview_wait=(TextView)findViewById(R.id.textview_wait);
		btnBack=(Button)findViewById(R.id.labelofbbs_btn_back);
		failedTextView=(TextView)findViewById(R.id.load_failed_textview);
		plv=(PullToRefreshListView)findViewById(R.id.user_center_labelofbbs_lv);
		
		bbsAdapter = new BBSAdapter(this, R.layout.bbs_list_item, bbsList);
		plv.setAdapter(bbsAdapter);
		plv.setVisibility(View.VISIBLE);
		// 为该列表设置上拉下拉监听
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				bbsNums = 0;
				new GetDataTask().execute();
			}

			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});

		// 设置PullRefreshListView上提加载时的加载提示
		plv.getLoadingLayoutProxy(false, true).setPullLabel(
				"上拉，上拉，上拉，O(∩_∩)O哈哈~");
		plv.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"伦家正在努力加载ing。↖(^ω^)↗");
		plv.getLoadingLayoutProxy(false, true).setReleaseLabel(
				"松开啦，我就加载，(*^__^*) 嘻嘻……");
		// 设置PullRefreshListView下拉加载时的加载提示
		plv.getLoadingLayoutProxy(true, false).setPullLabel(
				"下拉，下拉，下拉，O(∩_∩)O哈哈~");
		plv.getLoadingLayoutProxy(true, false).setRefreshingLabel(
				"伦家正在努力刷新ing。↖(^ω^)↗");
		plv.getLoadingLayoutProxy(true, false).setReleaseLabel(
				"松开啦，我就刷新，(*^__^*) 嘻嘻……");

		plv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 传递essayid给帖子。
				Log.i(tag, "帖子被点击");
				ExtendsIntent intent = new ExtendsIntent(LabelOfBBS.this,
						BBSPostDetailView.class, bbsList.get(position - 1)
								.getEssayId(), null, null, 1);
				Log.i(tag, "帖子被点击111");
				Log.i(tag, bbsList.get(position - 1).getEssayId());
				startActivity(intent);
				Log.i(tag, "queue.size:" + BaseActivity.queue.size());
				Log.i(tag, "帖子被点击2");
			}

		});
		btnBack.setOnClickListener(this);
		title.setText(userTag);
	}

	
	/**
	 * 异步加载数据
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		public GetDataTask() {
			Log.i(tag, "+++++++first");
		}

		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						// 下载数据
						loadData();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				Log.d(tag, "GetDataTask-线程被打断");
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			// 刷新列表
			//probar.setVisibility(View.GONE);
			plv.onRefreshComplete();
			bbsAdapter.notifyDataSetChanged();
			Log.i(tag, "bbsAdapter.notifyDataSetChanged();_______3");
		}
	}
	
	
	
	/**
	 * 从服务器端获取数据
	 * 
	 * @return response：json的数据
	 */
	private String getResponseFromNet() {
		Log.i(tag, "getResponseFromNet");
		Thread thread = new Thread(new Runnable() {
			public void run() {
				HandleLabel handleLabel=new HandleLabel();
				Log.i(tag, "label:"+userTag);
				handleLabel.startLoadBBS(UriAPI.SUB_URL+relativePath, cookie, userTag, bbsNums);
				response=handleLabel.getResponse();
				Log.i(tag, "Gson:" + response);
				Log.i(tag, "bBSConnectNet.getResponse()结束");
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.d(tag, "getResponseFromNet-线程被打断");
			e.printStackTrace();
		}
		Log.i(tag, "555");
		return response;
	}
	
	/**
	 * 异步更新的加载数据
	 * 
	 * @param page2
	 * @throws JSONException
	 */
	public void loadData() throws JSONException {

		Log.d(tag, "loadData");

		if (bbsNums == 0) {
			// 先把列表清空
			Log.d(tag, "bbsList.clear()");
			bbsList.clear();
		}
		response = getResponseFromNet();
		Log.i(tag, "loadData-response:" + response);
		MyLabelOfBBS labelofbbs=new Gson().fromJson(response,MyLabelOfBBS.class);
		String tip=labelofbbs.getTip();
		if(tip.equals("success")){
			/*if(probar.getVisibility()==View.VISIBLE){
				probar.setVisibility(View.GONE);
			}*/
			if(textview_wait.getVisibility()==View.VISIBLE){
				textview_wait.setVisibility(View.GONE);
			}
		}else{
			/*if(probar.getVisibility()==View.VISIBLE){
				probar.setVisibility(View.GONE);
			}*/
			if(textview_wait.getVisibility()==View.VISIBLE){
				textview_wait.setVisibility(View.GONE);
			}
			if(failedTextView.getVisibility()==View.GONE){
				failedTextView.setVisibility(View.VISIBLE);
			}
		}
		addData(labelofbbs);
		Log.i(tag,"已经初始化");
		//handler.sendEmptyMessage(1);
	}

	/**
	 * 加载数据 将BBSFirst中的BBS列表项加到BBS bean类中，并获取图片
	 * 
	 * @param dataFromJson
	 *            : BBSFirst的实例化
	 */
	private void addData(MyLabelOfBBS data ) {

		Log.d(tag, "addData");

		// 将BBSFirst中的BBS列表项加到BBS bean类中
		for (BBS bbs : data.getBbs()) {
			if (!bbsList.contains(bbs)) {
				bbsList.add(bbs);
				bbsNums++;
			}
		}
		Log.i(tag, "bbsList.size()" + bbsList.size());
		int count = 1;
		// 获取照片。
		for (BBS bbs : bbsList) {
			Log.i(tag, "第" + count++ + "条记录");
			bbs.setHeadImage(new ReceivePhoto(bbs.getHeadImagePath())
					.getPhoto());
			// 获取标签名和标签颜色
			// Log.i(tag, "获取标签名---"+bbs.getLabName());
			bbs.setLabName0(bbs.getLabName().split("\\*#"));
			// Log.i(tag, "获取标签颜色---"+bbs.getLabColor());
			String[] colors = bbs.getLabColor().split("\\*");
			int[] color = new int[colors.length];
			for (int i = 0; i < colors.length; i++) {
				color[i] = Color.parseColor(colors[i]);
			}
			bbs.setLabColor0(color);
			// Log.i(tag, "获取标签名和标签颜色---333");
		}
	}
	
	
	@Override
	public void processMessage(Message msg) {
	

	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.labelofbbs_btn_back:
			this.onBackPressed();
			break;
		}
		
	}

}
