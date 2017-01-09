package com.atm.chatonline.share;
/**
 * 该类是一个自定义的PopupWinbdow用于显示分享的弹出窗口
 * @author Jackbing
 * @date 2016.4.12
 */
import com.example.studentsystem01.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class SharePopupWindow extends PopupWindow {

	private View mView;
	private Activity context;
	private LinearLayout friendV,timelineV;
	public SharePopupWindow(Activity context,float defaultAlpha,OnClickListener listener){
		this.context=context;
		
    	setBackAlpha(defaultAlpha);
    	//获取PopWindow的布局
    	//1.获取inflater服务
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	//获取popwindow布局
    	mView=inflater.inflate(R.layout.share_popupwindow, null);
    	
    	timelineV=(LinearLayout) mView.findViewById(R.id.ly_share_timeline);
    	friendV=(LinearLayout) mView.findViewById(R.id.ly_share_friend);
    	timelineV.setOnClickListener(listener);
    	friendV.setOnClickListener(listener);
    	this.setContentView(mView);
    	this.setFocusable(true);
    	this.setTouchable(true);
    	this.setWidth(LayoutParams.MATCH_PARENT);
    	this.setHeight(LayoutParams.WRAP_CONTENT);
    	//背景一定要设置，不然无法dismiss popwindow
    	ColorDrawable dw=new ColorDrawable(0xffffff);
    	this.setBackgroundDrawable(dw);
    	this.setOnDismissListener(new  OnDismissListener(){

			public void onDismiss() {
				setBackAlpha(1f);
				SharePopupWindow.this.dismiss();
				
			}
    		
    	});
    	mView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int height=mView.findViewById(R.id.share_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						
						dismiss();
					}
				}
				return true;
			}
		});
    	
	}

	private void setBackAlpha(float alpha){
		//设置源视图的透明度
		WindowManager.LayoutParams lp=context.getWindow().getAttributes();
		lp.alpha=alpha;
		context.getWindow().setAttributes(lp);
		
	}

}
