package com.atm.chatonline.chat.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.chat.info.Group;
import com.example.studentsystem01.R;

public class GroupAdapter extends ArrayAdapter<Group>{
	private int resourceId;
	public GroupAdapter(Context context, int textViewResourceId,
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
			viewHolder.imageID=(ImageView)view.findViewById(R.id.group_person_image);
			viewHolder.groupID=(TextView)view.findViewById(R.id.group_person_name);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		//viewHolder.imageID.setImageResource(group.getHeadId());//***
		//viewHolder.imageID.setImageBitmap(group.getBm());
		viewHolder.imageID.setImageDrawable(new BitmapDrawable(group.getBm()));
		viewHolder.groupID.setText(group.getGroupName());
		return view;
		
	}
	
	class ViewHolder{
		ImageView imageID;
		TextView groupID;
	}
	

}
