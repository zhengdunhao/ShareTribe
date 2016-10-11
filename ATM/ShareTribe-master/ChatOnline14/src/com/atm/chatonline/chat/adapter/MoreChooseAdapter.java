package com.atm.chatonline.chat.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.chat.info.MoreChoose;
import com.example.studentsystem01.R;


public class MoreChooseAdapter extends ArrayAdapter<MoreChoose>{
	private int resourceId;
	
	
	
	public MoreChooseAdapter(Context context,int textViewResourceId,List<MoreChoose> objects){
		super(context,textViewResourceId,objects);
		resourceId=textViewResourceId;
	}
	
	public View getView(int position,View convertView,ViewGroup parent){
		MoreChoose moreChoose=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.imageID=(ImageView)view.findViewById(R.id.more_name);
			viewHolder.name=(TextView)view.findViewById(R.id.more_item);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		viewHolder.imageID.setImageResource(moreChoose.getImageID());
		viewHolder.name.setText(moreChoose.getName());
		return view;
		
	}
	
	class ViewHolder{
		ImageView imageID;
		TextView name;
	}
}
