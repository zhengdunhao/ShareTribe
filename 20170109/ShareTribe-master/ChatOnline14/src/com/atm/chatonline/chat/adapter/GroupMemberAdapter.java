package com.atm.chatonline.chat.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.chat.info.Friend;
import com.example.studentsystem01.R;


public class GroupMemberAdapter extends BaseAdapter {
	private List<Friend> listImgItems;
	private Context context;
	public GroupMemberAdapter(Context context,List<Friend> listImgItems){
		this.context=context;
		this.listImgItems=listImgItems;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return listImgItems.size();
	}

	@Override
	public Friend getItem(int position) {
		// TODO 自动生成的方法存根
		return listImgItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend friend=getItem(position);
		//View view;
		ViewHolder viewholder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.group_look_data_image_item, null);
			viewholder=new ViewHolder();
			viewholder.memberImg=(ImageView) convertView.findViewById(R.id.group_member_img);
			viewholder.nickName=(TextView) convertView.findViewById(R.id.username);
			convertView.setTag(viewholder);
		}else{
			viewholder=(ViewHolder) convertView.getTag();
		}
		viewholder.memberImg.setImageBitmap(friend.getBm());
		viewholder.nickName.setText(friend.getNickName());
		return convertView;
	}

	static class ViewHolder{
		ImageView memberImg;
		TextView nickName;
	}
}
