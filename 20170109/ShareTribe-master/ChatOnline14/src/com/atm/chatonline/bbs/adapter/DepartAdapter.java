package com.atm.chatonline.bbs.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atm.chatonline.bbs.bean.Department;
import com.example.studentsystem01.R;

public class DepartAdapter extends ArrayAdapter<Department> implements OnClickListener  {

	private String tag ="DepartAdapter";
	private int resID;
	private int count=1;
	public DepartAdapter(Context context, int textViewResourceId, List<Department> objects) {
		
		super(context, textViewResourceId, objects);
		Log.i(tag, "objects.size:"+objects.size());
		this.resID=textViewResourceId;
	}
	
	private class ViewHolder{
		TextView dName;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
//		Log.i(tag, "getView----111");
		Department depar = getItem(position);
//		Log.i(tag, "position:"+position);
		View deparView;
		ViewHolder viewHolder;
		if(convertView == null){
			deparView = LayoutInflater.from(getContext()).inflate(resID, null);
			viewHolder = new ViewHolder();
			viewHolder.dName = (TextView) deparView.findViewById (R.id.depar);
			deparView.setTag(viewHolder);
			Log.i(tag, "count:"+count++);
		}else{
			deparView = convertView;
			viewHolder = (ViewHolder) deparView.getTag();
			Log.i(tag, "布局已经存在");
		}
//		Log.i(tag, "depar.getDname():"+depar.getDname());
		viewHolder.dName.setText(" >  " + depar.getDname());
//		Log.i(tag, "getView----2222");
		return deparView;
	
	}

	public void onClick(View v) {
		 
		
	}

}
