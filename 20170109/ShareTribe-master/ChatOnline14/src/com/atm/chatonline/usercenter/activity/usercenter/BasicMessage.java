package com.atm.chatonline.usercenter.activity.usercenter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.bbs.commom.MyTextListener;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usercenter.util.HttpUtils;
import com.example.studentsystem01.R;

public class BasicMessage extends BaseActivity implements OnClickListener{
	private EditText etMotto,etNickName,etPosition;
	private TextView tvName,tvDept,tvSex,tvMajor,tvEmail;
	private String headImgPath,sex,name,sign,mname,dname,email,nickname,userId,jobTitle;//从后台接收
	private Bitmap img;
	private ImageButton btnEdit;
	private final String tag="BasicMessage";
	private int flag=0;//0表示处于未编辑状态，1表示保存状态但未改动，2表示改动过可保存状态
	private final int BASIC_MSG=13;
	private String path,fileName;
	private String IMAGE_TYPE="image/*";
	private final  int IMAGE_CODE=1,TOCROP=0;
	private final int CAMERA=2;//表示拍照
	private CircleImageView iv;
	private Handler handler;
	private ProgressBar pro;
	private PopupWindow pop;
	/*private String url="http://139.129.131.179/ATM/user/userInfoJ.do";
	private String changeUrl="http://139.129.131.179/ATM/user/changeUserInfo.do";*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bg_basic_message);
		pro=(ProgressBar) findViewById(R.id.progress_basic_message);
		Button btnBackF=(Button)findViewById(R.id.user_center_btn_back);
		btnBackF.setOnClickListener(this);
		initData();
		
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				pro.setVisibility(View.GONE);
				
				//initView();
				switch(msg.what){
				//1表示，首次访问服务器获取个人信息，2表示更改信息后从服务器获取的信息,3表示服务器无响应,只是为了消除
				//progressBar 所以不再switch设置处理方法
				case 1:
					initView();
					initPopWindow();
					break;
				case 2:
					setData();
					break;
				}
			}
			
		};
		
	}

	private void initData() {
		userId=getPreference().getUserID();
		new Thread(runnable).start();
	}

	private void initPopWindow() {
		View v=getLayoutInflater().inflate(R.layout.user_appeal_choose_picture, null);
		
		pop=new PopupWindow(v,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		ColorDrawable cd = new ColorDrawable(-0000);
	    pop.setAnimationStyle(R.style.popwindow_alpha_style); 
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
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			Map<String,String> params=new HashMap<String,String>();
			params.put("userId", userId);
			
			JSONObject o=HttpUtils.post(UriAPI.BASIC_MESSAGE, params, null);
			try {
				if(!o.getString("userId").equals("")){
				getUserInfoFromJSON(o);
				
				handler.sendEmptyMessage(1);
				}else{
					Toast.makeText(BasicMessage.this.getApplicationContext(), "服务器无响应", Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessage(3);
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
	};
	public void getUserInfoFromJSON(JSONObject o){
		if(o!=null){
			Log.i(tag, "o is not null o="+o.toString());
		try {
			headImgPath=o.getString("headImagePath");
			headImgPath=UriAPI.SUB_URL+headImgPath;
			sex=o.getString("sex");
			name=o.getString("name");
			sign=o.getString("sign");
			mname=o.getString("mname");
			dname=o.getString("dname");
			email=o.getString("email");
			nickname=o.getString("nickname");
			userId=o.getString("userId");
			jobTitle=o.getString("jobTitle");
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		img=HttpUtils.getBitmapFromUrl(headImgPath);
		Log.i(tag, "img is null"+(img==null));
		saveBitmapFile(img);
		}
	}
	private void initView() {
		TextListener tl=new TextListener();
		LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rl=inflater.inflate(R.layout.basic_message2, null);
		/*View rl=LayoutInflater.from(this).inflate(R.layout.basic_message, null);*/
		btnEdit=(ImageButton)rl.findViewById(R.id.btn_edit_title_bar);
		//btnEdit.setBackgroundColor(getResources().getColor(R.color.duty_green));//s设置默认的颜色
		etMotto=(EditText)rl.findViewById(R.id.basic_message_motto);
		etNickName=(EditText)rl.findViewById(R.id.basic_message_nickname);
		etPosition=(EditText)rl.findViewById(R.id.basic_message_job);
		iv=(CircleImageView)rl.findViewById(R.id.basic_message_headimg);
		tvName=(TextView)rl.findViewById(R.id.basic_message_name);
		tvDept=(TextView)rl.findViewById(R.id.basic_message_dept);
		tvMajor=(TextView)rl.findViewById(R.id.basic_message_major);
		tvEmail=(TextView)rl.findViewById(R.id.basic_message_email);
		tvSex=(TextView)rl.findViewById(R.id.basic_message_sex);
		Button btnBack=(Button)rl.findViewById(R.id.user_center_btn_back);
		btnBack.setOnClickListener(this);
		setData();
		
		setContentView(rl);
		
		iv.setOnClickListener(this);
		etMotto.addTextChangedListener(tl);
		etNickName.addTextChangedListener(tl);
		etPosition.addTextChangedListener(tl);
		btnEdit.setOnClickListener(this);
		
	}

	private void setData() {
		tvName.setText(name);
		tvDept.setText(dname);
		tvMajor.setText(mname);
		tvEmail.setText(email);
		etMotto.setText(sign);
		etNickName.setText(nickname);
		
		iv.setImageBitmap(img);
		etPosition.setText(jobTitle);
		tvSex.setText(sex);
		
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.btn_edit_title_bar:
			String text=(String)btnEdit.getTag();
			Log.i(tag, "btnText="+text);
			if(flag==0){
				Log.i("duty", "edit duty green");
				btnEdit.setTag("保存");
				btnEdit.setImageResource(R.drawable.basicmessage_save_icon);
				btnEdit.setBackgroundColor(getResources().getColor(R.color.light_green));
				etMotto.setEnabled(true);
				etNickName.setEnabled(true);
				etPosition.setEnabled(true);
				iv.setClickable(true);
				flag=1;
				
				
			}else if(flag==1||flag==2){
				
				Log.i("duty", "save duty green");
				pro.setVisibility(View.VISIBLE);
				btnEdit.setTag("编辑");
				btnEdit.setImageResource(R.drawable.basicmessage_edit_icon);
				btnEdit.setBackgroundColor(getResources().getColor(R.color.duty_green));
				etMotto.setEnabled(false);
				etNickName.setEnabled(false);
				etPosition.setEnabled(false);
				iv.setClickable(false);
			
				if(flag==2){
					Log.i("??????????", "flag is "+flag);
					Log.i(">>>>", "etMotto ="+etMotto.getText().toString());
					Log.i(">>>>", "etNickName ="+etNickName.getText().toString());
					Log.i(">>>>", "etPosition ="+etPosition.getText().toString());
					Log.i(">>>>", "path ="+path);
					post();
					
				}
				flag=0;
				
				
			}
			break;
		case R.id.user_center_btn_back:
			Log.i(tag,"点击返回");
			Intent intent=getIntent();
			intent.putExtra("img", img);
			intent.putExtra("userId", userId);
			intent.putExtra("nickName", nickname);
			setResult(BASIC_MSG,intent);
			this.onBackPressed();
			break;
		case R.id.basic_message_headimg:
			Log.i(tag,"点击头像");
			if(flag!=0){
				pop.showAtLocation(v, Gravity.CENTER, 0, 0);
			}
			break;
		}
		
	}

	/**
	 * 调用图库
	 */
	private void setImage() {
		Intent intent=new Intent(Intent.ACTION_PICK,null);
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, IMAGE_TYPE);
		startActivityForResult(intent,IMAGE_CODE);
	}
	
	private void getCarmera() {
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent,CAMERA);
	}
	
	/**
	 * 剪切图片
	 * @param uri
	 */
	public void startPhotoZoomSec(Uri uri){
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		Log.i("starttocROP", "***");
		startActivityForResult(intent,TOCROP);
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
	/**
	 * 保存图片
	 * @param bm
	 */
	public void saveBitmapFile(Bitmap bm){
//		String filename=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		File filePath=new File(Environment.getExternalStorageDirectory()+"/ATM/userhead");
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
		File file=new File(filePath+"/"+userId+".jpg");
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
		
		path=filePath+"/"+userId+".jpg";
		fileName=userId+".jpg";
		Log.i("carmera", path);
		Log.i("carmera", fileName);
	}
	
	
	
	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(data==null){
			
			return;
		}else{
			flag=2;
		}
		Log.i(tag, "resultCode is "+resultCode);
		switch(requestCode){
		case IMAGE_CODE:
			Uri uri=data.getData();
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
			break;
		case TOCROP://裁切图片
		Bitmap bm=data.getParcelableExtra("data");
		if(bm!=null){
			
			Log.i(tag, "photo is not null");
			iv.setImageBitmap(bm);
			flag=2;
			btnEdit.setBackgroundColor(getResources().getColor(R.color.light_green));
			saveBitmapFile(bm);
		}else{
			Log.i(tag, "photo is  null");
			
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
	
	/**
	 * 监听器，监听文本框的变化
	 * @author Jackbing
	 *
	 */
	class TextListener extends MyTextListener{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(flag==1){
			btnEdit.setBackgroundColor(getResources().getColor(R.color.light_green2));
			flag=2;
			}
			
		}
		
	}
	
	/**
	 * 发送更改的信息到服务器
	 */
	public void post(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				Map<String,String> params=new HashMap<String,String>();
				params.put("sign", etMotto.getText().toString().trim());
				params.put("nickname", etNickName.getText().toString().trim());
				params.put("jobTitle", etPosition.getText().toString().trim());
				params.put("userId", userId);
				
				Map<String,File> files=new HashMap<String,File>();
				files.put(fileName, new File(path));
				JSONObject o=HttpUtils.post(UriAPI.CHANEG_USER_INFO, params, files);
				/*try {
					
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(tag, "json error!");
					return;
				}*/
				getUserInfoFromJSON(o);
				
				handler.sendEmptyMessage(2);
			}
			
		}).start();
		
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}
	
}
