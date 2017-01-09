package com.atm.chatonline.bbs.util;


import com.example.studentsystem01.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GridViewItem extends RelativeLayout implements Checkable {

	private Context mContext;
	private boolean mCheck;
	private ImageView mImageView;
	public ImageView mSelect;

	public GridViewItem(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	public GridViewItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public GridViewItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.gridview_item, this);
		mImageView = (ImageView) findViewById(R.id.photo_img_view);
		mSelect = (ImageView) findViewById(R.id.photo_select);
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return mCheck;
	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub
		mCheck = checked;
	//	System.out.println(checked);
		mSelect.setImageDrawable(checked ? getResources().getDrawable(
				R.drawable.cb_on) : getResources().getDrawable(
				R.drawable.cb_normal));
		 refreshDrawableState();  
	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		setChecked(!mCheck);
	}
	
	public void mySetBitmap(Bitmap bit){
		if(mImageView != null){
			mImageView.setImageBitmap(bit);
		}
	}

}
