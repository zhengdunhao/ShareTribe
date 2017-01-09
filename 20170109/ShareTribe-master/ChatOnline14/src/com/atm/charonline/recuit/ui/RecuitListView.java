/**
 * 
 */
package com.atm.charonline.recuit.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;

import com.atm.charonline.recuit.util.BBSConnectNet;
import com.atm.charonline.recuit.util.ExtendsIntent;
import com.atm.chatonline.bbs.activity.bbs.BBSListView;
import com.atm.chatonline.bbs.commom.MyToast;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.recuit.adapter.RecuitAdapter;
import com.atm.chatonline.recuit.bean.Data;
import com.atm.chatonline.recuit.bean.Recuit;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @类 com.atm.charonline.recuit.ui ---RecuitListView
 * @作用 该类用于所需招聘列表的显示
 * @作者 陈小二
 * @时间 2015-9-23
 * 
 */

public class RecuitListView extends Activity {

	//传递给服务器端所需的参数
	private static String id;
	private static String tip = "";
	public String getTip() {
		return tip;
	}

	public static void setTip(String tip) {
		RecuitListView.tip = tip;
	}

	private String relativePath;
	
	private BBSConnectNet bBSConnectNet;
	private List<Recuit> recuitList = new ArrayList<Recuit>();
	private RecuitAdapter recuitAdapter;
	private PullToRefreshListView plv;
	private int recuitNums = 0;
	private String response;
	private String cookie;
	private TextView recuit_wait;
	private String tag = "RecuitListView";
	private static Handler handler;
	
	private boolean isNeedCache = true;
	
	public static Handler getHandler() {
		return handler;
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recuit_list_view);
		LogUtil.p("class","recuitListView");
		initParams();
		initView();
//		new GetDataTask().execute();
		try {
			initData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
					case Config.FULLTIME:
						recuitNums = 0;
						tip="全职";
						new GetDataTask().execute();
						break;
					case Config.ALLKIND:
						recuitNums = 0;
						tip="全部";
						new GetDataTask().execute();
						break;
					case Config.PARTTIME:
						recuitNums = 0;
						tip="兼职";
						new GetDataTask().execute();
						break;
					case Config.INTERNSHIP:
						recuitNums = 0;
						tip="实习";
						new GetDataTask().execute();
						break;
					case Config.SEARCHESSAY:
						recuitNums = 0;
						new GetDataTask().execute();
						break;
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

		LogUtil.p("tip",tip);
		LogUtil.p("id",id);
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		Log.d("initParams()cookie",cookie);
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		
		LogUtil.d("initView");

        recuit_wait = (TextView) findViewById(R.id.recuit_wait);
		recuit_wait.setVisibility(View.GONE);
        Spinner sp = (Spinner) findViewById(R.id.sp_workType);

        // 实例化PullToRefreshListView，并配置适配器
		plv = (PullToRefreshListView) findViewById(R.id.recuit_lv_home);
		recuitAdapter = new RecuitAdapter(this,R.layout.recuit_list_item,recuitList);
		plv.setAdapter(recuitAdapter);

		//为该列表设置上拉下拉监听
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				recuitNums = 0;
				new GetDataTask().execute();
			}
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
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
				ExtendsIntent intent = new ExtendsIntent( RecuitListView.this , RecuitPostDetailView.class ,
						recuitList.get(position - 1).getReInfoId(), null , null , 1);
				Log.d("essayId", recuitList.get(position - 1).getReInfoId());
				startActivity(intent);
				
			}
        	
		});
        
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
				bBSConnectNet = new BBSConnectNet(tip , id ,recuitNums,relativePath,cookie);
				Log.d("顺序","getResponseFromNet+RecuitConnectNet结束");
				response = bBSConnectNet.getResponse();
				LogUtil.d(response);
				LogUtil.d("bBSConnectNet.getResponse()结束");
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			LogUtil.p("getResponseFromNet","线程被打断");
			e.printStackTrace();
		}
		LogUtil.p("444444","555");
		return response;
	}

	/**
	 * 异步加载数据
	 */
	private class GetDataTask extends AsyncTask<Void , Void , String>{
		
		
		public GetDataTask(){
		}
		
		protected String doInBackground(Void... params) {

			//下载数据
			try {
				isNeedCache = true;
				loadData();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
		protected void onPostExecute(String result){
//			recuit_wait.setVisibility(View.GONE);
			//刷新列表
			recuitAdapter.notifyDataSetChanged();
			plv.onRefreshComplete();
		}
	}

	/**
	 * 异步更新的加载数据
	 * @param page2
	 * @throws JSONException 
	 */
	public void loadData() throws JSONException {
		
		LogUtil.p("顺序","loadData");
		
		if(recuitNums == 0){
			//先把列表清空
			recuitList.clear();
		}
		//缓存数据
				LogUtil.d("有无数据？");
				if(tip!="全部"&&tip!="全职"&&tip!="兼职"&&tip!="实习") {
					LogUtil.d("tip不等于全部全职兼职实习");
					isNeedCache = false;
				}
				if(isNeedCache) {
					String cacheDate = judgeDataCache();
					if(!TextUtils.isEmpty(cacheDate)) {
						LogUtil.d("调用了RecuitListView，有数据");
						response = cacheDate;
					}else {
						LogUtil.d("无数据");
						response = getResponseFromNet();
						//如果是主界面请求数据：将请求的数据保存下来
						saveData(response);
					}
				}else {
					response = getResponseFromNet();
				}
		LogUtil.p("顺序","loadData+1");
		
		Data data = new Gson().fromJson(response,Data.class);
		if(data.getTip()!=null) {
		switch(data.getTip()) {
		case "没有结果":
			LogUtil.p(tag,"data.getTip():"+data.getTip());
			MyToast toast=MyToast.makeText(getApplicationContext(), data.getTip(), Toast.LENGTH_SHORT);
			toast.show();
			break;
		default:
			addData(data);
		}}
		Log.d(tag,"loadData+3");
		
	}
	
	/**
	 * @return 
	 * 
	 */
	private String judgeDataCache() {
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			in = openFileInput("recuitData");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while((line = reader.readLine())!= null) {
				content.append(line);
			}
			if(content.toString()!=null) {
				LogUtil.d("此处已有数据，数据为：");
				LogUtil.d(content.toString());
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(reader != null) {
				try {
					reader.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}
	
	/**
	 * @param response2 
	 * 
	 */
	private void saveData(String response2) {
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = openFileOutput("recuitData", Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(response2);
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(writer != null) {
					writer.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 加载数据
	 * 将RecuitFirst中的Recuit列表项加到Recuit bean类中，并获取图片
	 * @param dataFromJson : RecuitFirst的实例化
	 */
	private void addData(Data data) {

		LogUtil.p("顺序","addData");
		
		//将RecuitFirst中的Recuit列表项加到Recuit bean类中
		for(Recuit recuit :data.getRecuit()){
			if(!recuitList.contains(recuit)){
				recuitList.add(recuit);
				recuitNums++;
			}
		}
	}

}
