/**
 * 
 */
package com.atm.chatonline.bbs.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.bbs.bean.User;
import com.atm.chatonline.bbs.adapter.BBSAdapter.ViewHolder;
import com.atm.chatonline.bbs.util.LogUtil;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.bbs.adapter ---UserAdapter
 * @作用 搜索用户列表的适配器类
 * @作者 陈小二
 * @时间 2015-11-2
 * 
 */
public class UserAdapter extends ArrayAdapter<User> {

	private int resID;
	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public UserAdapter(Context context, int textViewResourceId,
			List<User> objects) {
		super(context, textViewResourceId, objects);
		this.resID = textViewResourceId;
	}
	public class ViewHolder{
		TextView userNickName,dName;
		ImageView headImage,sex;
		
	}
	public View getView(int position, View convertView, ViewGroup parent){
		User user = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resID, null);
			viewHolder = new ViewHolder();
			viewHolder.userNickName = (TextView) view.findViewById(R.id.userID);
			viewHolder.headImage = (ImageView) view.findViewById(R.id.user_headImage);
			viewHolder.dName = (TextView) view.findViewById(R.id.dName);
			viewHolder.sex = (ImageView) view.findViewById(R.id.sex);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		if(user.getNickname() !=null)
		viewHolder.userNickName.setText(user.getNickname());
		if(user.getHeadImage() != null)
		viewHolder.headImage.setImageDrawable(user.getHeadImage());
		if(user.getdName() !=null)
		viewHolder.dName.setText(user.getdName());
		if(user.getSex() != null && user.getSex() == "女")
		viewHolder.sex.setBackgroundResource(R.drawable.comments);
		if(user.getUserId() != null )
			LogUtil.d(user.getUserId());
		return view;
		
	}
}
