package com.atm.chatonline.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;

import com.atm.chatonline.chat.util.ImageLoadingDialog;
import com.example.studentsystem01.R;


/**
 * 
 */
public class ImageShower extends Activity {

	private String tag="ImageShower";
	private Intent intent;
	private String content;
	private ImageView bigImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.imageshower);
		initUI();
		intent = getIntent();
		content = intent.getStringExtra("content");
		final ImageLoadingDialog dialog = new ImageLoadingDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Bitmap bm = BitmapFactory.decodeFile(content);
		bigImg.setImageBitmap(bm);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				dialog.dismiss();
			}
		}, 1000 * 1);
		
	}

	void initUI(){
		bigImg = (ImageView)findViewById(R.id.big_img);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}