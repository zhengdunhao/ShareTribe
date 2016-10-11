package com.atm.chatonline.usermsg.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atm.chatonline.usermsg.bean.Notification;
import com.example.studentsystem01.R;

public class SystemMsgAdapter extends BaseAdapter {

	private List<Notification> list;
	private Context context;
	private int resId;
	
	public SystemMsgAdapter(Context context, int resource,
			List<Notification> objects) {
		super();
		this.list=objects;
		this.context=context;
		this.resId=resource;	
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
		return convertView;
	}



}
class ViewHolder{
 TextView title,time,content;
}

