package com.atm.chatonline.chat.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.atm.chatonline.bbs.bean.BBS;
import com.atm.chatonline.bbs.bean.Data;
import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.ReceivePhoto;
import com.atm.chatonline.usercenter.adapter.NoteAdapter;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PeopleCollectedNote extends BaseActivity implements OnClickListener{

	private int bbsNums = 0;
	private int count = 0;
	private String response;
	private TextView title;
	private Button btnBack;
	private String userID,friendID,friendNickName;
	private PullToRefreshListView plv;
	private ProgressBar pro;
	private String cookie;
	private List<BBS> bbsList = new ArrayList<BBS>();
	private NoteAdapter noteAdapter;
	private BBSConnectNet bBSConnectNet;
	private Handler handler;
	private String tag ="PeopleCollectedNote";
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.people_published_note);
		initUI();
		initIntent();
		initParams();
		new GetDataTask().execute();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根

				switch (msg.what) {
				case 1:
					initView();
					break;
				case 2:
					pro.setVisibility(View.GONE);
					Toast.makeText(PeopleCollectedNote.this.getApplicationContext(),
							"没有帖子了，别再刷新哦！", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					pro.setVisibility(View.GONE);
					Toast.makeText(PeopleCollectedNote.this.getApplicationContext(),
							"你还没有发过帖子，赶紧去发帖子吧！！", Toast.LENGTH_SHORT).show();
					break;
				}

			}

		};
	}
	
	void initUI(){
		btnBack = (Button)findViewById(R.id.btn_back);
		title = (TextView)findViewById(R.id.title);
		btnBack.setOnClickListener(this);
		pro = (ProgressBar)findViewById(R.id.myprogressbar_note);
		plv = (PullToRefreshListView)findViewById(R.id.lv_mybbs);
	}
	void initIntent(){
		try{
			Intent intent = getIntent();
			userID = intent.getStringExtra("userID");
			friendID = intent.getStringExtra("friendID");
			friendNickName = intent.getStringExtra("nickName");
			title.setText(friendNickName+"收藏的帖子");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	void initParams() {
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		LogUtil.p(tag, "cookie:"+cookie);
	}
	void initView(){
		noteAdapter = new NoteAdapter(PeopleCollectedNote.this,
				R.layout.my_bbs_note_list_item, bbsList);
		plv.setAdapter(noteAdapter);

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
		plv.getLoadingLayoutProxy(false, true).setPullLabel("正在加载...");
		plv.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载ing");
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
				ExtendsIntent intent = new ExtendsIntent(PeopleCollectedNote.this,
						BBSPostDetailView.class, bbsList.get(position - 1)
								.getEssayId(), null, null, 1);
				Log.i(tag, "帖子被点击111");
				Log.i(tag, bbsList.get(position - 1).getEssayId());
				startActivity(intent);
				// Log.i(tag, "queue.size:"+WoliaoBaseActivity.queue.size());
				Log.i(tag, "帖子被点击2");
			}

		});
		Log.i(tag, "走完initView");
		// 如果是第一次加载则替换view，如果不是，则更新view
		/*
		 * if(count==0){ count++; setContentView(v); }
		 */

		// textView.setVisibility(View.GONE);
		pro.setVisibility(View.GONE);
		plv.setVisibility(View.VISIBLE);
		
	}

	/**
	 * 异步加载数据
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		public GetDataTask() {
			Log.i(tag, "+++++++first");
		}

		protected String doInBackground(Void... params) {
			// 下拉或上拉停住，等待2秒
			Log.i(tag, "+++++++second");
			// 2015.10.9这个我改了一下，doInBackground是处理耗时问题，所以把loadData放在这里，避免出现ANR问题---郑
			// Thread.sleep(2000);
			try {
				Log.i(tag, "+++++++third");
				loadData();
				// loadPhoto();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			Log.i(tag, "update bbslistview !");
			noteAdapter.notifyDataSetChanged();
			plv.onRefreshComplete();
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
					bBSConnectNet = new BBSConnectNet("", friendID, bbsNums,
							"essay_collectedEssay.action", cookie);

					Log.i(tag, "getResponseFromNet+BBSConnectNet结束");
					response = bBSConnectNet.getResponse();
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
			Log.d(tag, "loadData+1");

			Data data = new Gson().fromJson(response, Data.class);

			Log.d(tag, "loadData+3");

			addData(data);
			// 将BBSFirst中的BBS列表项加到BBS bean类中
			for (BBS bbs : bbsList) {
				Log.i(tag, "img path" + bbs.getHeadImagePath().toLowerCase());
			}
			Log.d(tag, "loadData走完");
			if (count == 0) {
				handler.sendEmptyMessage(1);
				count++;
			}
		}
	}
	

	/**
	 * 加载数据 将BBSFirst中的BBS列表项加到BBS bean类中，并获取图片
	 * 
	 * @param dataFromJson
	 *            : BBSFirst的实例化
	 */
	private void addData(Data data) {

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
			Log.i(tag, "获取标签名和标签颜色---111");
			bbs.setLabName0(bbs.getLabName().split("\\*#"));
			Log.i(tag, "获取标签名和标签颜色---222");
			String[] colors = bbs.getLabColor().split("\\*");
			int[] color = new int[colors.length];
			for (int i = 0; i < colors.length; i++) {
				color[i] = Color.parseColor(colors[i]);
			}
			bbs.setLabColor0(color);
			Log.i(tag, "获取标签名和标签颜色---333");
		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.btn_back:
			this.onBackPressed();
			break;
		}
	}

}
