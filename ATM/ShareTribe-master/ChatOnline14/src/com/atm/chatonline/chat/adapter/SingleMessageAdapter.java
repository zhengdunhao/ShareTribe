package com.atm.chatonline.chat.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atm.chatonline.chat.info.Friend;
import com.example.studentsystem01.R;



public class SingleMessageAdapter extends ArrayAdapter<Friend>{
	private int resourceId;
	
	private String tag="SingleMessageAdapter";
	
	
	
	public SingleMessageAdapter(Context context, int textViewResourceId,
			List<Friend> objects) {
		super(context,textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	
	public View getView(int position,View convertView,ViewGroup parent){
		Friend friend = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.signature = (TextView)view.findViewById(R.id.personal_content);
			viewHolder.school = (TextView)view.findViewById(R.id.school_content);
			viewHolder.department = (TextView)view.findViewById(R.id.department_content);
			viewHolder.publishNoteTitle = (TextView)view.findViewById(R.id.publish_note_title);
			viewHolder.publishNoteDetail = (TextView)view.findViewById(R.id.publish_note_detail);
			viewHolder.collectNoteTitle = (TextView)view.findViewById(R.id.collect_note_title);
			viewHolder.collectNoteDetail = (TextView)view.findViewById(R.id.collect_note_details);
			view.setTag(viewHolder);
			
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		viewHolder.signature.setText(friend.getSignature());
		viewHolder.school.setText(friend.getSchool());
		viewHolder.department.setText(friend.getDepartment());
		viewHolder.publishNoteTitle.setText(friend.getPublishNoteTitle());
		viewHolder.publishNoteDetail.setText(friend.getPublishNoteDetail());
		viewHolder.collectNoteTitle.setText(friend.getCollectNoteTitle());
		viewHolder.collectNoteDetail.setText(friend.getCollectNoteDetail());
		
		
		return view;
	}
//	this.friendID = friendID;
//	this.nickName = nickName;
//	this.sex = sex;
//	this.attentions = attentions;
//	this.followers = followers;
//	this.signature = signature;
//	this.school = school;
//	this.department = department;
//	this.publishNoteNum = publishNoteNum;
//	this.publishNoteTitle = publishNoteTitle;
//	this.publishNoteDetail = publishNoteDetail;
//	this.collectNoteNum = collectNoteNum;
//	this.collectNoteTitle = collectNoteTitle;
//	this.collectNoteDetail = collectNoteDetail;
//	this.bm = bm;
	class ViewHolder{
		TextView signature;
		TextView school;
		TextView department;
		TextView publishNoteTitle;
		TextView publishNoteDetail;
		TextView collectNoteTitle;
		TextView collectNoteDetail;
	}
	
}
