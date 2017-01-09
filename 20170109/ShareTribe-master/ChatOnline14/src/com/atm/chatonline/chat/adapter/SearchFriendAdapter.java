package com.atm.chatonline.chat.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.chat.info.Friend;
import com.example.studentsystem01.R;


public class SearchFriendAdapter extends ArrayAdapter<Friend>{
	private int resourceId;
	
	private String tag="SearchFriendAdapter";
	
	public SearchFriendAdapter(Context context, int textViewResourceId,
			List<Friend> objects) {
		super(context,textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	public View getView(int position,View convertView,ViewGroup parent){
		Friend friend=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.imageID=(CircleImageView)view.findViewById(R.id.friend_img);
			viewHolder.friendNickName=(TextView)view.findViewById(R.id.friend_name);
			viewHolder.department=(TextView)view.findViewById(R.id.department);
//			viewHolder.friendSex=(TextView)view.findViewById(R.id.friend_sex);
			viewHolder.imageSex=(ImageView)view.findViewById(R.id.sex_img);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		if(friend.getBm()==null){
			Log.i(tag, "friend.getBm()为空");
		}
		
		viewHolder.imageID.setImageBitmap(friend.getBm());
		
		Log.i(tag, "friend.getFriendID"+friend.getFriendID());
		
		viewHolder.friendNickName.setText(friend.getNickName());
		Log.i(tag, "friend.getNickName"+friend.getNickName());
		
		viewHolder.department.setText(friend.getDepartment());
		Log.i(tag, "friend.getDepartment"+friend.getDepartment());
		if(friend.getSex().equals("男")){
			viewHolder.imageSex.setImageResource(R.drawable.icon_male);
		}else if(friend.getSex().equals("女")){
			viewHolder.imageSex.setImageResource(R.drawable.icon_female);
		}else{
			viewHolder.imageSex.setImageResource(R.drawable.no_sex);
			Log.i(tag, "friend.getSex()：无性别");
		}
//		viewHolder.friendSex.setText(friend.getSex());
		Log.i(tag, "friend.getSex()"+friend.getSex());
		return view;
		
	}
	
	

	class ViewHolder{
		CircleImageView imageID;
		TextView friendNickName;
		TextView department;
//		TextView friendSex;
		ImageView imageSex;
	}
}
