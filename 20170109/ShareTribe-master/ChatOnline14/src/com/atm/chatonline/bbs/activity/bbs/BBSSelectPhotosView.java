package com.atm.chatonline.bbs.activity.bbs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atm.chatonline.bbs.adapter.PhotoAdapter;
import com.atm.chatonline.bbs.util.PhotoItem;
import com.example.studentsystem01.R;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore.Images.Media;

public class BBSSelectPhotosView extends Activity implements OnClickListener{
	private List<PhotoItem> photoItem = new ArrayList<PhotoItem>();//存储全部图片
	private List<PhotoItem> selectedPic = new ArrayList<PhotoItem>();//存储选中的图片
	private GridView gv_photo;
	private PhotoAdapter photo_adap,select_adap;
	private TextView tv_cancel, tv_sure;
	//private ImageView iv_return;
	private Context context;
	private static final int RESULT_PHOTO = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_select_photo_view);
		initView();
		initEvent();
		context = getApplicationContext();
		Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
				null, null, null, null);
		while (cursor.moveToNext()) {
			// 获取图片的保存路径
			String id = cursor.getString(cursor.getColumnIndex(Media._ID));
			byte[] data = cursor.getBlob(cursor.getColumnIndex(Media.DATA));
			String path = new String(data, 0, data.length - 1);
			photoItem.add(new PhotoItem(Integer.valueOf(id), path));

		}
		cursor.close();
		photo_adap = new PhotoAdapter(context, photoItem, null);
		gv_photo.setAdapter(photo_adap);
		gv_photo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				boolean checked = photoItem.get(arg2).isSelect();
				if (!checked) {//图片被选中
					photoItem.get(arg2).setSelect(true);
					selectedPic.add(photoItem.get(arg2));
					tv_sure.setText("确定(" + selectedPic.size() + ")");
					Log.d("MainActivity","selectedPic.size()"+selectedPic.size() );
				} else {//取消选中图片
					photoItem.get(arg2).setSelect(false);
					selectedPic.remove(photoItem.get(arg2));
					tv_sure.setText("确定(" + selectedPic.size() + ")");
				}
				if(selectedPic.size()>0){
					tv_sure.setClickable(true);
					tv_sure.setTextColor(Color.WHITE);
				}else{
					tv_sure.setClickable(false);
					tv_sure.setTextColor(Color.GRAY);
				}
				photo_adap.notifyDataSetChanged();
			}
		});
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Intent intent = getIntent();
				Intent intent = new Intent(BBSSelectPhotosView.this,BBSPublishPostView.class);//2016.07.19修改
				intent.putExtra("selectedPic", (Serializable)selectedPic);
				intent.putExtra("test", "This is a test");
				BBSSelectPhotosView.this.setResult(RESULT_PHOTO, intent);
				BBSSelectPhotosView.this.finish();
			}
		});

	}
	

	private void initEvent() {
		// TODO Auto-generated method stub
		tv_cancel.setOnClickListener(this);
		//iv_return.setOnClickListener(this);
	}


	private void initView() {
		// TODO Auto-generated method stub
		gv_photo = (GridView) findViewById(R.id.gv_photo);
		tv_sure = (TextView) findViewById(R.id.tv_sure);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		//iv_return = (ImageView) findViewById(R.id.iv_return);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.iv_return:
			BBSSelectPhotosView.this.finish();
			break;
		case R.id.tv_cancel:
			BBSSelectPhotosView.this.finish();
			Log.d("MainActivity", "finish");
			break;
		}
	}

}
