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
import com.atm.chatonline.usermsg.bean.ReplyMessage;
import com.example.studentsystem01.R;

public class ReplyAdapter extends BaseAdapter {

	private Context mContext;
	private int resId;
	private List<ReplyMessage> list;
	private boolean isShowCheckBox;
	
	public ReplyAdapter(Context mContext, int resId, List<ReplyMessage> list,boolean isShowCheckBox) {
		super();
		this.mContext = mContext;
		this.resId = resId;
		this.list = list;
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
		ReplyMessage msg=list.get(position);
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(resId, null);
			viewHolder=new ViewHolder();
			viewHolder.headImage=(CircleImageView) convertView.findViewById(R.id.replymsg_headimg);
			viewHolder.replyContent=(TextView) convertView.findViewById(R.id.replymsg_test_content);
			viewHolder.replyNickName=(TextView) convertView.findViewById(R.id.replymsg_nickname);
			viewHolder.replyTime=(TextView) convertView.findViewById(R.id.replyMsgTime);
			viewHolder.check_box=(CheckBox)convertView.findViewById(R.id.check_btn);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.headImage.setImageBitmap(msg.getContent().getHeadImage());
		viewHolder.replyContent.setText(msg.getContent().getEssayTitle());
		viewHolder.replyNickName.setText(msg.getContent().getNickname());
		viewHolder.replyTime.setText(msg.getContent().getCreateTime());
		
		if(!isShowCheckBox){
			viewHolder.check_box.setVisibility(View.GONE);
			
		}else{
			viewHolder.check_box.setVisibility(View.VISIBLE);
		}
		viewHolder.check_box.setChecked(false);
		
		return convertView;
	}

	public class ViewHolder {
		public CheckBox check_box;
		CircleImageView headImage;
		TextView replyNickName,replyContent,replyTime;
		
	}
}

