package com.atm.chatonline.chat.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.chat.info.GroupChatMessage;
import com.atm.chatonline.chat.util.TimeUtil;
import com.example.studentsystem01.R;

public class GroupChatAdapter extends BaseAdapter {

	 protected static final String TAG = "ChattingAdapter";
	 private Context context;
	 private List<GroupChatMessage> groupChatMessages;
	 private String lastTime;
	
	 public GroupChatAdapter(Context context, List<GroupChatMessage> messages) {
	  super();
	  this.context = context;
	  this.groupChatMessages = messages;
	 }
	 public int getCount() {
	  return groupChatMessages.size();
	 }
	 public Object getItem(int position) {
	  return groupChatMessages.get(position);
	 }
	 public long getItemId(int position) {
	  return position;
	 }
	 public View getView(int position, View convertView, ViewGroup parent) {
	  ViewHolder holder = null;
	  GroupChatMessage message = groupChatMessages.get(position);
	  int direction=message.getDirection();
	  if (convertView == null || (holder = (ViewHolder) convertView.getTag()).flag != message.getDirection()) {
	   holder = new ViewHolder();
	   if (direction == GroupChatMessage.MESSAGE_FROM) {
		   
	    holder.flag = GroupChatMessage.MESSAGE_FROM;
	    convertView = LayoutInflater.from(context).inflate(R.layout.chatfrom_list_view, null);
	   } else {
	    holder.flag = GroupChatMessage.MESSAGE_TO;
	    convertView = LayoutInflater.from(context).inflate(R.layout.chatto_list_view, null);
	   }
	   holder.time=(TextView) convertView.findViewById(R.id.chat_time);
	   holder.head=(ImageView) convertView.findViewById(R.id.chat_head_pic);
	   holder.text = (TextView) convertView.findViewById(R.id.chat_content);
	   convertView.setTag(holder);
	  }else{
		  holder = (ViewHolder)convertView.getTag();
	  }
	  
	  String time=message.getTime();
		if(time!=null && !"".equals(time)){
			String content=message.getContent();
			if(message.equals(groupChatMessages.get(0))){
				Log.i(TAG, "chatMessage是第一条，我的内容是:"+content);
				String relativeTime=TimeUtil.compareTime(time,time,1);
				holder.time.setText(relativeTime);
				Log.i(TAG, "我要显示时间:");
				Log.i(TAG, "chatMessage是第一条，我永远走的是1");
				Log.i(TAG, "relativeTime="+relativeTime+" 时间");
			}else{
					lastTime=groupChatMessages.get(groupChatMessages.size()-2).getTime();
					Log.i(TAG, "chatMessage不是第一条,我的内容是:"+content);
					String relativeTime=TimeUtil.compareTime(time,lastTime,message.getShowTime());
					
					Log.i(TAG, "relativeTime="+relativeTime+" 时间");
					holder.time.setText(relativeTime);
					if(message.getShowTime()==0){
					if(relativeTime.equals("")){
						message.setShowTime(-1);
						Log.i(TAG, "chatMessage不是第一条，我永远走的是-1");
					}
					else{
						message.setShowTime(1);
						Log.i(TAG, "chatMessage不是第一条，我永远走的是1");
					}
					}else{
						Log.i(TAG, "我的内容是:"+content+"我骄傲，不用重新走，我的showTime是:"+message.getShowTime());
					}
					
				
			}
		}
		Log.i(TAG, "ChatAdapter-----我在变化");
		Log.i(TAG, "我要显示内容:"+message.getContent());
	  
	  holder.head.setBackgroundResource(message.getHeadImg());
	  holder.text.setText(message.getContent());
	  return convertView;
	 }
	//优化listview的Adapter
	 static class ViewHolder {
	  ImageView head;
	  TextView text;
	  TextView time;
	  int flag;
	 }

}
