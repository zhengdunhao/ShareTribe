package com.atm.chatonline.usermsg.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.example.studentsystem01.R;

public class ApplyAdapter extends BaseAdapter {

	private Context mContext;
	private int resId;
	private List<ApplyMessage> list;
	private boolean isShowCheckBox;

	public ApplyAdapter(Context mContext, int resId, List<ApplyMessage> list,boolean isShowCheckBox) {
		super();
		this.mContext = mContext;
		this.resId = resId;
		this.list = list;
		this.isShowCheckBox=isShowCheckBox;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
		
	
	public boolean isShowCheckBox() {
		return isShowCheckBox;
	}

	public void setShowCheckBox(boolean isShowCheckBox) {
		this.isShowCheckBox = isShowCheckBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ApplyMessage msg=list.get(position);
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(resId, null);
			viewHolder=new ViewHolder();
			viewHolder.headImage=(CircleImageView) convertView.findViewById(R.id.applymsg_headimg);
			viewHolder.applyContent=(TextView) convertView.findViewById(R.id.applymsg_test_content);
			viewHolder.applyNickName=(TextView) convertView.findViewById(R.id.applymsg_nickname);
			viewHolder.applyTime=(TextView) convertView.findViewById(R.id.applyMsgTime);
			viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.check_btn);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.headImage.setImageBitmap(msg.getContent().getHeadImage());
		viewHolder.applyContent.setText(msg.getContent().getEssayTitle());
		viewHolder.applyNickName.setText(msg.getContent().getNickname());
		viewHolder.applyTime.setText(msg.getContent().getReplyTime());
		if(!isShowCheckBox){
			viewHolder.checkBox.setVisibility(View.GONE);
			
		}else{
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		}
		viewHolder.checkBox.setChecked(false);
		return convertView;
	}

	
	public class ViewHolder{
		CircleImageView headImage;
		public CheckBox checkBox;
		TextView applyNickName,applyContent,applyTime;
	}
}
