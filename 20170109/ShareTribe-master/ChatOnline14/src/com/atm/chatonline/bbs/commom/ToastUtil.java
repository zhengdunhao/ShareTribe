package com.atm.chatonline.bbs.commom;
/*
 * 该类用于 自定义Toast 的外观。
 * 2015.07.25-atm 陈小二
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentsystem01.R;

public class ToastUtil {
	@SuppressLint("NewApi")
	public void show(Context context,String str){
		View toastRoot = LayoutInflater.from(context).inflate(R.layout.defined_toast, null); 
		toastRoot.setAlpha(50);
		Toast toast=new Toast(context);  
		toast.setView(toastRoot);  
		TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);  
		tv.setText(str);  
		toast.show(); 
	}
}
