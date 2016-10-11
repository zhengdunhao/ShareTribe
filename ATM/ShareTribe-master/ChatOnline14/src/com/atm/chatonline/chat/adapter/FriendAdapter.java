package com.atm.chatonline.chat.adapter;
/**
 * 好友列表和群列表
 * */
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




public class FriendAdapter extends ArrayAdapter<Friend>{

		private int resourceId;
		private String tag="FriendAdapter";
		
		public FriendAdapter(Context context,int textViewResourceId,List<Friend> objects){
			super(context,textViewResourceId,objects);
			resourceId=textViewResourceId;
		}
		
		public View getView(int position,View convertView,ViewGroup parent){
			Friend friend=getItem(position);
			View view;
			ViewHolder viewHolder;
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceId, null);
				viewHolder = new ViewHolder();
				viewHolder.imageID=(CircleImageView)view.findViewById(R.id.group_person_image);
				viewHolder.nickName=(TextView)view.findViewById(R.id.group_person_name);
				
				//这个很重要啊
				
				view.setTag(viewHolder);
			}else{
				view=convertView;
				viewHolder=(ViewHolder)view.getTag();
			}
			viewHolder.imageID.setImageBitmap(friend.getBm());
			Log.i(tag, "getView--nickName:"+friend.getNickName());
			viewHolder.nickName.setText(friend.getNickName());
			
			return view;
			
		}
		
		class ViewHolder{
			CircleImageView imageID;
			TextView nickName;
			
		}
		
}
