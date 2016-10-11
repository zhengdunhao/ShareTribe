package com.atm.chatonline.chat.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.chat.info.Friend;
import com.example.studentsystem01.R;

public class MyAttentionAdapter extends ArrayAdapter<Friend>{
private int resourceId;
	
	private String tag="AttentionAdapter";
	
	private String userID,friendID;
	private int relationship;
	
	
	public MyAttentionAdapter(Context context, int textViewResourceId,
			List<Friend> objects) {
		super(context,textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	
	public View getView(int position,View convertView,ViewGroup partent){
		final Friend friend = getItem(position);
		View view;
		final ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.imageID = (CircleImageView)view.findViewById(R.id.friend_img);
			viewHolder.friendNickName = (TextView)view.findViewById(R.id.friend_name);
			viewHolder.department = (TextView)view.findViewById(R.id.department);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		if(friend.getBm()==null){
			Log.i(tag, "friend.getBm()为空");
		}
		//头像
		viewHolder.imageID.setImageBitmap(friend.getBm());
		
		Log.i(tag, "friend.getFriendID"+friend.getOtherID());
		//昵称
		viewHolder.friendNickName.setText(friend.getNickName());
		Log.i(tag, "friend.getNickName"+friend.getNickName());
		//院系
		viewHolder.department.setText(friend.getDepartment());
		Log.i(tag, "friend.getDepartment"+friend.getDepartment());
	
	
		
		return view;
	}
	
	

	
	class ViewHolder{
		CircleImageView imageID;
		TextView friendNickName;
		TextView department;
		
	}
}
