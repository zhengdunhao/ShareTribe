/**
 * 
 */
package com.atm.chatonline.recuit.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.recuit.bean.Apply;
import com.atm.chatonline.recuit.bean.Recuit;
import com.example.studentsystem01.R;

/**
 * 
 * 
 */
public class ApplyAdapter extends ArrayAdapter<Apply> {
	private int resource;

	/**
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public ApplyAdapter(Context context, int resource, List<Apply> objects) {
		super(context, resource, objects);
		this.resource=resource;
	}
	
	class ViewHolder{
		ImageView recuitType;
		TextView workContent,publishTime,salary;
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Apply apply = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resource,null);
			viewHolder = new ViewHolder();
			viewHolder.workContent = (TextView) view.findViewById(R.id.recuit_content);
			viewHolder.publishTime = (TextView) view.findViewById(R.id.recuit_publishTime);
			viewHolder.salary = (TextView) view.findViewById(R.id.recuit_salary);
			viewHolder.recuitType = (ImageView) view.findViewById(R.id.recuit_recuitType);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		Log.d("recuitAdapter:recuit.getPublishTime()",apply.getPublishTime());
		Log.d("recuitAdapter:recuit.getSalary()",apply.getExpectSalary());
		Log.d("recuitAdapter:recuit.getWoTypeName()",apply.getWoTypeName());
		Log.d("recuitAdapter:recuit.getReTypeName()",apply.getReTypeName());
		
		viewHolder.publishTime.setText(apply.getPublishTime().toString().trim());
		viewHolder.salary.setText(apply.getExpectSalary().toString().trim());
		viewHolder.workContent.setText(apply.getWoTypeName().toString().trim());

			switch(apply.getReTypeName()) {
			case "实习":
				LogUtil.d("实习");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_internship);
				break;
			case "全职":
				LogUtil.d("全职");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_fulltime);
				break;
			case "兼职":
				LogUtil.d("兼职");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_parttime);
				break;
			default:
				LogUtil.d("全职");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_fulltime);
			}
		return view;
	}

}
