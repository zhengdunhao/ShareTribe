package com.atm.chatonline.bbs.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atm.chatonline.bbs.bean.BBS;
import com.example.studentsystem01.R;





public class MyBbsAdapter extends BaseAdapter {
	private View v;
	private Context context;
	private int resID;
	private List<BBS> list;
	private String tag="NoteAdapter";
	public MyBbsAdapter(Context context,int resID,List<BBS> list){
		this.context=context;
		this.list=list;
		this.resID=resID;
	}
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	public BBS getItem(int position) {
		
		// TODO 自动生成的方法存根
		if(position>=0){
			return list.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder viewHolder=null;
		BBS bbs=getItem(position);
		View view;
		if(convertView == null){
			
			view = LayoutInflater.from(context).inflate(resID, null);
//			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			Log.i(tag, "getView()------0000");
			viewHolder = new ViewHolder();
			Log.i(tag, "getView()------1111");
			viewHolder.zanNumber = (TextView) view.findViewById(R.id.zanNumber);
			viewHolder.time = (TextView) view.findViewById(R.id.note_time);
			
			viewHolder.comNumber = (TextView) view.findViewById(R.id.comNumber);
			viewHolder.title = (TextView) view.findViewById(R.id.note_title);
			viewHolder.content = (TextView) view.findViewById(R.id.note_content);
			
			Log.i(tag, "getView()------5555555");
			int [] labs = {R.id.label1,R.id.label2,R.id.label3};
			for(int i = 0 ; i < viewHolder.lab.length ; i++){
				viewHolder.lab[i] = (TextView) view.findViewById(labs[i]);
			}
			view.setTag(viewHolder);//将ViewHolder存储在view中
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
		}
		Log.i(tag,bbs.getClickGoodNum());
		viewHolder.zanNumber.setText(bbs.getClickGoodNum());
		viewHolder.time.setText(bbs.getPublishTime());
		viewHolder.title.setText("【"+bbs.getEssayType()+"】"+bbs.getTitle());
		viewHolder.comNumber.setText(bbs.getReplyNum());
		/*viewHolder.title.setText("【"+bbs.getEssayType()+"】"+bbs.getTitle());*/
		viewHolder.content.setText(bbs.getSomeContent());
		Log.i(tag, "getView()------333333");
		//viewHolder.headImage.setImageDrawable(bbs.getHeadImage());
		Log.i(tag, "getView()------888888");
		//加载标签
		for(int i=0;i<bbs.getLabName0().length;i++){
			viewHolder.lab[i].setText(bbs.getLabName0()[i]);
			viewHolder.lab[i].setBackgroundColor(bbs.getLabColor0()[i]);
			viewHolder.lab[i].setVisibility(View.VISIBLE);
		}
		for(int i = 2 ; i >= bbs.getLabName0().length ; i--){
			viewHolder.lab[i].setVisibility(View.INVISIBLE);
		}
		Log.i(tag, "getView()走完了");
		return view;
		
	}
	static class ViewHolder {
		TextView title,content,time,zanNumber,comNumber;
		TextView [] lab=new TextView[3];
		
	}
	
}
