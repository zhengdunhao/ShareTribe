package com.atm.chatonline.bbs.commom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentsystem01.R;

public class MyToast extends Toast{
	final Context context;
	View mNextView;
	int mDuration;
	public MyToast(Context context) {
		super(context);
		this.context=context;
	}
	public static MyToast makeText(Context context,CharSequence text,int duration)
	{
		 MyToast result=new MyToast(context);
		 LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View v = inflate.inflate(R.layout.defined_toast, null);
		 result.setView(v);
		 result.setDuration(duration);
		 TextView tv = (TextView)v.findViewById(R.id.TextViewInfo);
		 tv.setText(text);
		 result.mNextView = v;
		 result.mDuration = duration;
		 return result;
	}
	@Override
	public void cancel() {
		// TODO 自动生成的方法存根
		super.cancel();
	}
	@Override
	public void setDuration(int duration) {
		// TODO 自动生成的方法存根
		super.setDuration(duration);
	}
	
	
	public void setText(CharSequence s)
	{
		 if (mNextView == null) {
			   throw new RuntimeException("This Toast was not created with Toast.makeText()");
			 }
			 TextView tv = (TextView) mNextView.findViewById(R.id.TextViewInfo);
			 if (tv == null) {
			 throw new RuntimeException("This Toast was not created with Toast.makeText()");
			}
			  tv.setText(s);

	}
	
}
