package com.atm.chatonline.usermsg.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.atm.chatonline.usermsg.bean.Notification;
import com.example.studentsystem01.R;

public class SystemMsgAdapter extends BaseAdapter{

	private List<Notification> list;
	private List<Integer> indexOfItem;
	private Context context;
	private int resId;
	private boolean isShowCheckBox;
	
	public SystemMsgAdapter(Context context, int resource,
			List<Notification> objects,boolean isShowCheckBox) {
		super();
		this.list=objects;
		this.context=context;
		this.resId=resource;	
		this.isShowCheckBox=isShowCheckBox;
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	
	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}
	
	

	public boolean isShowCheckBox() {
		return isShowCheckBox;
	}

	public void setShowCheckBox(boolean isShowCheckBox) {
		this.isShowCheckBox = isShowCheckBox;
	}

	
	public List<Integer> getIndexOfItem() {
		return indexOfItem;
	}

	public void setIndexOfItem(List<Integer> indexOfItem) {
		this.indexOfItem = indexOfItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Notification notify=list.get(position);
		ViewHolder viewHolder;
		
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(resId, null);
			viewHolder=new ViewHolder();
			viewHolder.title=(TextView) convertView.findViewById(R.id.systemmsg_title);
			viewHolder.time=(TextView) convertView.findViewById(R.id.systemmsg_time);
			viewHolder.content=(TextView) convertView.findViewById(R.id.systemmsg_content);
			viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.check_btn);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		System.out.println("-->>:"+notify.getContent().getTitle());
		System.out.println("-->>:"+notify.getContent().getContent());
		System.out.println("-->>:"+notify.getContent().getTime());
		viewHolder.title.setText(notify.getContent().getTitle());
		viewHolder.content.setText(notify.getContent().getContent());
		viewHolder.time.setText(notify.getContent().getTime());
		if(!isShowCheckBox){
			viewHolder.checkBox.setVisibility(View.GONE);
		}else{
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		}
		
		
		return convertView;
	}
	
	public class ViewHolder{
		public TextView title,time,content;
		public CheckBox checkBox;
	}
}




