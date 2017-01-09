package com.atm.chatonline.bbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.atm.chatonline.bbs.commom.UriAPI;

public class ReceivePhoto {
	private String tag ="ReceivePhoto";
	
	private String address;
	private Drawable d;
	private URL url;
	private String subURL = UriAPI.SUB_URL;
	private HttpURLConnection con;
	private InputStream is;

	@SuppressWarnings("deprecation")
	public ReceivePhoto(String address){
		this.address = subURL + address;	
		LogUtil.d("图片缓存：判断是否有缓存？");
		if(ImageFileCacheUtils.getImage(address)==null) {
			LogUtil.d("图片缓存：没有图片，去下载图片");
			sendRequestWithHttpURLConnection();
		}else {
			LogUtil.d("图片缓存：有图片，去缓存拿图片");
			d = new BitmapDrawable(ImageFileCacheUtils.getImage(address));
		}
	}
//	public ReceivePhoto(String address){
//		this.address = subURL + address;		
//		sendRequestWithHttpURLConnection();
//	}
	
	private void sendRequestWithHttpURLConnection() {
		Thread thread1 =new Thread(new Runnable(){
			public void run(){
				try{

					url = new URL(address);
					con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					is = con.getInputStream();
					BitmapFactory.Options options = new Options();
					options.inDither = false; //不进行图片抖动处理；
					options.inPreferredConfig = null;//设置最佳解码器解码
					options.inSampleSize = 2;
					Bitmap bm = BitmapFactory.decodeStream(is, null, options);
					d = new BitmapDrawable(bm);
//				    d = Drawable.createFromStream(is,"btn_msg.png");
//				    d = zoomDrawable(Drawable.createFromStream(is,"btn_msg.png"),800,480);//试一下
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		thread1.start();
		try {
			thread1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}
	
	//新添加，作为图片处理,进过测试，发现无用
//	private Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成 bitmap   
//    {  
//              int width = drawable.getIntrinsicWidth();   // 取 drawable 的长宽   
//              int height = drawable.getIntrinsicHeight();  
//              Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;    // 取 drawable 的颜色格式   
//              Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应 bitmap   
//              Canvas canvas = new Canvas(bitmap);         // 建立对应 bitmap 的画布   
//              drawable.setBounds(0, 0, width, height);  
//              drawable.draw(canvas);      // 把 drawable 内容画到画布中   
//              return bitmap;  
//    } 
//
//	private Drawable zoomDrawable(Drawable drawable, int w, int h)  
//    {  
//              int width = drawable.getIntrinsicWidth();  
//              int height= drawable.getIntrinsicHeight();  
//              Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap   
//              Matrix matrix = new Matrix();   // 创建操作图片用的 Matrix 对象   
//              float scaleWidth = ((float)w / width);   // 计算缩放比例   
//              float scaleHeight = ((float)h / height);  
//              matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例   
//              Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图  
//              
//              return new BitmapDrawable(newbmp);       // 把 bitmap 转换成 drawable 并返回   
//    }  

	
	public Drawable getPhoto(){		
		Log.d("Receive网络测试连接","成+4");
		return d;
	}
}
