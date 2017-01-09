package com.atm.chatonline.bbs.activity.bbs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

public class BBSPreviewPicture extends BaseActivity implements OnClickListener {
	private ImageView photo_one,iv_delete,iv_return;
	private String picturePath = "";
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_preview_picture);
		
		
		Intent intent = getIntent();
		picturePath = intent.getStringExtra("picturePath");
		
		initView();
		initEvent();
		
		bitmap = BitmapFactory.decodeFile(picturePath);
		photo_one.setImageBitmap(bitmap);
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		iv_delete.setOnClickListener(this);
		iv_return.setOnClickListener(this);
	}
	private void initView() {
		// TODO Auto-generated method stub
		photo_one =(ImageView) findViewById(R.id.photo_one);
		iv_delete = (ImageView) findViewById(R.id.iv_delete);
		iv_return = (ImageView) findViewById(R.id.iv_return);
	}
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.iv_delete:
			break;
		case R.id.iv_return:
			BBSPreviewPicture.this.finish();
			break;
		}
	}

}
