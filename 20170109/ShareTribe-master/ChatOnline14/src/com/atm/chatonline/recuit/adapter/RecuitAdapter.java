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
import com.atm.chatonline.recuit.bean.Recuit;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.charonline.recuit.adapter ---RecuitAdapter
 * @作用 招聘列表的适配器
 * @作者 陈小二
 * @时间 2015-9-20
 * 
 */
public class RecuitAdapter extends ArrayAdapter<Recuit> {
	private int resource;

	/**
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public RecuitAdapter(Context context, int resource, List<Recuit> objects) {
		super(context, resource, objects);
		this.resource=resource;
	}
	
	class ViewHolder{
		ImageView recuitType,enough;
		TextView workContent,publishTime,salary,location;
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Recuit recuit = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resource,null);
			viewHolder = new ViewHolder();
			viewHolder.workContent = (TextView) view.findViewById(R.id.recuit_content);
			viewHolder.publishTime = (TextView) view.findViewById(R.id.recuit_publishTime);
			viewHolder.salary = (TextView) view.findViewById(R.id.recuit_salary);
			viewHolder.recuitType = (ImageView) view.findViewById(R.id.recuit_recuitType);
			viewHolder.location = (TextView) view.findViewById(R.id.recuit_location);
			viewHolder.enough = (ImageView) view.findViewById(R.id.recuit_enough);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		Log.d("recuitAdapter:recuit.getPublishTime()",recuit.getPublishTime());
		Log.d("recuitAdapter:recuit.getSalary()",recuit.getSalary());
		Log.d("recuitAdapter:recuit.getWorkAddress()",recuit.getWorkAddress());
		Log.d("recuitAdapter:recuit.getWoTypeName()",recuit.getWoTypeName());
		Log.d("recuitAdapter:recuit.getReTypeName()",recuit.getReTypeName());
		viewHolder.publishTime.setText(recuit.getPublishTime());
		viewHolder.salary.setText("薪资"+recuit.getSalary());
		viewHolder.workContent.setText("岗位"+recuit.getWoTypeName());
		viewHolder.location.setText(recuit.getWorkAddress());

			switch(recuit.getReTypeName()) {
			case "实习":
				LogUtil.d("图片变动：实习");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_internship);
				break;
			case "全职":
				LogUtil.d("图片变动：全职");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_fulltime);
				break;
			case "兼职":
				LogUtil.d("图片变动：兼职");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_parttime);
				break;
			default:
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_fulltime);
			}
		return view;
	}

}
