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

import com.atm.chatonline.chat.info.Group;
import com.example.studentsystem01.R;



public class SearchGroupAdapter extends ArrayAdapter<Group>{
	private int resourceId;
	
	private String tag="SearchGroupAdapter";
	
	public SearchGroupAdapter(Context context, int textViewResourceId,
			List<Group> objects) {
		super(context,textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	public View getView(int position,View convertView,ViewGroup parent){
		Group group=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.imageID=(ImageView)view.findViewById(R.id.group_img);
			viewHolder.groupName=(TextView)view.findViewById(R.id.group_name);
			viewHolder.groupPeopleNum=(TextView)view.findViewById(R.id.group_people_number);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		viewHolder.imageID.setImageBitmap(group.getBm());
		Log.i(tag, "group.getGroupID"+group.getGroupId());
		viewHolder.groupName.setText(group.getGroupName());
		Log.i(tag, "group.getGroupName"+group.getGroupName());
		viewHolder.groupPeopleNum.setText(group.getPeopleNum());
		return view;
		
	}
	
	class ViewHolder{
		ImageView imageID;
		TextView groupName;
		TextView groupPeopleNum;
	}
}
