package com.atm.chatonline.bbs.activity.login;
/**
 * @author Jackbing
 * 
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.MyToast;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SendPicture;
import com.example.studentsystem01.R;

public class UserAppeal extends Activity  implements OnClickListener{
	private EditText etUserId,etUserName,etUserEmail;
	private String tag="UserAppeal";
	private MyToast toast;
	private final String IMAGE_TYPE="image/*";
	private final int IMAGE_CODE=0;//表示相册选择
	private final int TOCROP=1;//表示剪切图片
	private final int CAMERA=2;//表示拍照
	
	private String path,fileName,userId,userName,userEmail,role;
	private ImageView img;
	private Handler handler;
	private PopupWindow pop;
	//2016.8.18接手加冰
	private TextView title,imgExample;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_appeal);
		initView();
		initPopWindow();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				switch(msg.what){
				case 1:
					showToast("申诉成功，请留意邮箱通知！");
					
					break;
				case 2:
					showToast("服务器无响应！");
					break;
				}
			}

			
		};
	}
	
	private void initPopWindow() {
		View v=getLayoutInflater().inflate(R.layout.user_appeal_choose_picture, null);
		
		pop=new PopupWindow(v,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		ColorDrawable cd = new ColorDrawable(-0000);
//	    pop.setAnimationStyle(R.style.popwindow_alpha_style); 
	    pop.setAnimationStyle(R.style.popup_anim_style); 
        pop.setBackgroundDrawable(cd);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		
		pop.setWidth(600);
		
		v.findViewById(R.id.gallery).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				pop.dismiss();
				setImage();
			}
			
		});
		v.findViewById(R.id.carmera).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				pop.dismiss();
				getCarmera();
			}

			
		});
		v.findViewById(R.id.btn_select_picture_cancel).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				pop.dismiss();
			}
		});
	}

	private void initView() {
		etUserId=(EditText)findViewById(R.id.user_appeal_userid);
		etUserName=(EditText)findViewById(R.id.user_appeal_name);
		etUserEmail=(EditText)findViewById(R.id.user_appeal_email);
		title = (TextView)findViewById(R.id.title);
		imgExample = (TextView)findViewById(R.id.img_example);
		
		img=(ImageView)findViewById(R.id.upload_img);
		//img.setScaleType(ImageView.ScaleType.CENTER);
		Button btnBack=(Button)findViewById(R.id.btn_back);
		Button btnSubmit=(Button)findViewById(R.id.btn_user_appeal_submit);
		RadioGroup rg=(RadioGroup)findViewById(R.id.type_btn_group);
		title.setText("申  诉");
		title.setTextSize(18);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO 自动生成的方法存根
				switch(checkedId){
				case R.id.inschool:
					LogUtil.p(tag, "在校生");
					role="在校生";
					break;
				case R.id.graduated:
					LogUtil.p(tag, "毕业生");
					role="毕业生";
					break;
				case R.id.teacher:
					LogUtil.p(tag, "教师");
					role="教师";
					break;
				}
			}
		});
		imgExample.setOnClickListener(this);
		etUserId.setOnClickListener(this);
		etUserName.setOnClickListener(this);
		etUserEmail.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		img.setOnClickListener(this);
	}

	
	
	

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.btn_back:
			Log.i(tag, "返回按钮被点击");
			this.onBackPressed();
			break;
		case R.id.btn_user_appeal_submit:
			Log.i(tag,"确定按钮被点击");
			sendAppealMsg();
			break;
		case R.id.upload_img:
			//初始化一个popwindow来选择
			pop.showAtLocation(v, Gravity.CENTER, 0, 0);
			break;
		case R.id.img_example:
			Intent intent = new Intent(UserAppeal.this,ImgExample.class);
			startActivity(intent);
			
		}
	}
	
	
	private void redirectToLoginView() {
		cancelToast();
		startActivity(new Intent(this,LoginView.class));
		finish();
		
	}
	
	private void sendAppealMsg() {
		userId=etUserId.getText().toString().trim();
		userName=etUserName.getText().toString().trim();
		userEmail=etUserEmail.getText().toString().trim();
		if(userId.equals("")||userName.equals("")||userEmail.equals("")||path.equals("")){
			showToast("请把信息填写完整");
		}else
		{
			if(path.equals("")||fileName.equals("")){
				showToast("请上传正面身份证照片");
			}else{
				Log.i(tag, "userId="+userId);
				Log.i(tag, "userName="+userName);
				Log.i(tag, "userEmail="+userEmail);
				Log.i(tag, "role="+role);
				new Thread(runnable).start();
			}
		}
		
	}
	
	
	Runnable runnable =new Runnable(){

		@Override
		public void run() {
			
			JSONArray params=new JSONArray();
			try {
				params.put(new JSONObject().put("number", userId).put("name", userName)
						.put("informEmail", userEmail).put("role", role));
			} catch (JSONException e) {
				
				e.printStackTrace();
				return;
			}
			Map<String,File> files=new HashMap<String,File>();
			
			files.put(fileName, new File(path));
			Log.i(tag, "post");
			try {
				String respCode=SendPicture.post(UriAPI.USER_APPEAL, params, files).getString("tip");
				if(respCode.equals("success")){
					handler.sendEmptyMessage(1);
					Thread.sleep(4000);
					 redirectToLoginView();
				}else{
					handler.sendEmptyMessage(2);
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			Log.i(tag, "发送完毕");
		}
		
	};
	public void setImage(){
		Intent intent=new Intent(Intent.ACTION_PICK,null);
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, IMAGE_TYPE);
		startActivityForResult(intent,IMAGE_CODE);
	}
	
	private void getCarmera() {
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent,CAMERA);
	}
	
	public void startPhotoZoom(Bitmap data){
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("data", data);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 180);
		intent.putExtra("outputY", 120);
		intent.putExtra("return-data", true);
		Log.i("starttocROP", "***");
		startActivityForResult(intent,TOCROP);
		
	}
	
	
	public void startPhotoZoomSec(Uri uri){
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 180);
		intent.putExtra("outputY", 120);
		intent.putExtra("return-data", true);
		Log.i("starttocROP", "***");
		startActivityForResult(intent,TOCROP);
	}
	/**
	 * 保存图片
	 * @param bm
	 */
	public void saveBitmapFile(Bitmap bm){
		String filename=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		File filePath=new File(Environment.getExternalStorageDirectory()+"/UserCenter/head");
		if(filePath.exists()==false){
			filePath.mkdirs();
			Log.i("carmera","make dir");
		}
		if(filePath.exists()==true){
			File[] files=filePath.listFiles();
			for(File file:files){
				file.delete();
			}
		}
		File file=new File(filePath+"/"+filename+".jpg");
		try {
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return;
		} catch (IOException e) {
			
			e.printStackTrace();
			return;
		}
		
		path=filePath+"/"+filename+".jpg";
		fileName=filename+".jpg";
		Log.i("carmera", path);
		Log.i("carmera", fileName);
	}
	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(data==null){
			return;
		}
		switch(requestCode){
		case IMAGE_CODE:
			Uri uri=data.getData();
			/*Bitmap photo = null;
			try {
				photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}*/
			String[] proj={MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};
			Cursor cursor=managedQuery(uri, proj, null, null, null);
			cursor.moveToFirst();
			int path_columnIndex= cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			int name_columnIndex=cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
			fileName=cursor.getString(name_columnIndex);
			
			path=cursor.getString(path_columnIndex);
			
			Log.i(tag, "path="+path);
			Log.i(tag, "filename="+fileName);
			startPhotoZoomSec(uri);
			Log.i(tag, "startPhotoZoomSec");
			/*if(photo!=null){
				startPhotoZoom(photo);
				Log.i(tag, "photo is not null");
			}else{
				Log.i(tag, "photo is  null");
			}*/
			
			break;
		case TOCROP://裁切图片
		Bitmap bm=data.getParcelableExtra("data");
		if(bm!=null){
			
			Log.i(tag, "photo is not null");
			img.setBackground(new BitmapDrawable(getResources(),bm));
		}else{
			Log.i(tag, "photo is  null");
			if(!path.equals("")){
				bm=BitmapFactory.decodeFile(path);
				startPhotoZoom(bm);
			}else{
				Log.i(tag, "bm get from path is null");
			}
		}
		
		break;
		case CAMERA:
			final Bitmap photo1=data.getParcelableExtra("data");
			if(photo1!=null){
				Log.i(tag, "photo1 is not null");
			saveBitmapFile(photo1);
			startPhotoZoom(photo1);
			}else{
				Log.i(tag, "photo1 is null");
			}
			break;
		}
	} 

	public void showToast(String s)
	{
		if(toast==null)
		{
			toast=MyToast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
		}else{
			toast.setText(s);toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}
	
	//cancel掉toast
	public void cancelToast()
	{
		if(toast!=null){
			toast.cancel();
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		cancelToast();
			Log.i(">>>>>>", "退出");
			finish();
	}
	
}
