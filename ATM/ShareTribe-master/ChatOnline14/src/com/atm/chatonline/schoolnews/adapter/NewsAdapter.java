package com.atm.chatonline.schoolnews.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.schoolnews.bean.News;
import com.example.studentsystem01.R;

public class NewsAdapter extends ArrayAdapter<News>{
	private int resourceId;
	
	private String tag="NewsAdapter";
	
	public NewsAdapter(Context context, int textViewResourceId,
			List<News> objects) {
		super(context,textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	
	public View getView(int position,View convertView,ViewGroup partent){
		final News news = getItem(position);
		View view;
		final ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.newsImage = (ImageView)view.findViewById(R.id.news_img);
			viewHolder.mainTitle = (TextView)view.findViewById(R.id.main_title);
			viewHolder.viceTitle = (TextView)view.findViewById(R.id.vice_title);
			viewHolder.viewCount = (TextView)view.findViewById(R.id.view_count);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		
		
		viewHolder.newsImage.setImageDrawable(news.getNewsImage());
		
		
		viewHolder.mainTitle.setText(news.getMainTitle());
		
		viewHolder.viceTitle.setText(news.getViceTitle());
		
		Log.i(tag, "viewcount:"+news.getViewCount());
		viewHolder.viewCount.setText(news.getViewCount());
		
		return view;
	}
	
	class ViewHolder{
		ImageView newsImage;
		TextView mainTitle;
		TextView viceTitle;
		TextView viewCount;
	}

}
