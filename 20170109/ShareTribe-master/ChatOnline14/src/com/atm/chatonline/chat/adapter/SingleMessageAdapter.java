package com.atm.chatonline.chat.adapter;

import java.util.List;

import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.info.Friend;
import com.example.studentsystem01.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleMessageAdapter extends ArrayAdapter<Friend> {
	private int resourceId;
	private Context context;
	private String tag = "SingleMessageAdapter";
	String publishEssayId;
	String collectEssayId; 
	public SingleMessageAdapter(Context context, int textViewResourceId, List<Friend> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		resourceId = textViewResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Friend friend = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.signature = (TextView) view.findViewById(R.id.personal_content);
			viewHolder.school = (TextView) view.findViewById(R.id.school_content);
			viewHolder.department = (TextView) view.findViewById(R.id.department_content);
			viewHolder.publishNoteTitle = (TextView) view.findViewById(R.id.publish_note_title);
			viewHolder.publishNoteDetail = (TextView) view.findViewById(R.id.publish_note_detail);
			viewHolder.collectNoteTitle = (TextView) view.findViewById(R.id.collect_note_title);
			viewHolder.collectNoteDetail = (TextView) view.findViewById(R.id.collect_note_details);
			viewHolder.ll_collect_note = (LinearLayout) view.findViewById(R.id.ll_collect_note);
			viewHolder.ll_publish_note = (LinearLayout) view.findViewById(R.id.ll_publish_note);
			view.setTag(viewHolder);

		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		publishEssayId = friend.getPublishEssayId();
		collectEssayId = friend.getCollectEssayId();
		viewHolder.signature.setText(friend.getSignature());
		viewHolder.school.setText(friend.getSchool());
		viewHolder.department.setText(friend.getDepartment());
		viewHolder.publishNoteTitle.setText(friend.getPublishNoteTitle());
		viewHolder.publishNoteDetail.setText(friend.getPublishNoteDetail());
		viewHolder.collectNoteTitle.setText(friend.getCollectNoteTitle());
		viewHolder.collectNoteDetail.setText(friend.getCollectNoteDetail());
		

		viewHolder.ll_publish_note.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.p(tag, "ll_publish_note被点击");
				ExtendsIntent intent = new ExtendsIntent(context, BBSPostDetailView.class, publishEssayId, null, null, 1);
				context.startActivity(intent);
			}
		});
		viewHolder.publishNoteTitle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.p(tag, "ll_publish_note被点击");
				ExtendsIntent intent = new ExtendsIntent(context, BBSPostDetailView.class, publishEssayId, null, null, 1);
				context.startActivity(intent);
			}
		});
		viewHolder.collectNoteTitle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.p(tag, "ll_publish_note被点击");
				ExtendsIntent intent = new ExtendsIntent(context, BBSPostDetailView.class, collectEssayId, null, null, 1);
				context.startActivity(intent);
			}
		});

		viewHolder.ll_collect_note.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.p(tag, "ll_collect_note被点击");
				ExtendsIntent intent = new ExtendsIntent(context, BBSPostDetailView.class, collectEssayId, null, null, 1);
				context.startActivity(intent);
			}
		});

		return view;
	}

	class ViewHolder {
		TextView signature;
		TextView school;
		TextView department;
		TextView publishNoteTitle;
		TextView publishNoteDetail;
		TextView collectNoteTitle;
		TextView collectNoteDetail;
		LinearLayout ll_publish_note;
		LinearLayout ll_collect_note;
	}

}
