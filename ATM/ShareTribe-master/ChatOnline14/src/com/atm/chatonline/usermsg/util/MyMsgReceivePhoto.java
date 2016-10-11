package com.atm.chatonline.usermsg.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.util.FileUtil;



public class MyMsgReceivePhoto {

	private String tag ="MyMsgReceivePhoto";
	
	private String address;
	private Drawable d;
	private URL url;
	private String subURL = UriAPI.SUB_URL;
	private HttpURLConnection con;
	private InputStream is;
	private Bitmap bm; 
	private CacheManager cacheManager;
	private String key;
	
	public MyMsgReceivePhoto(){}
	
	/**
	 * 
	 * @param cacheManager	缓存管理器
	 * @param key		缓存头像的key,我们把发送者的id作为key
	 * @param address	头像相对地址
	 * @return	返回头像Drawable
	 */
	@SuppressWarnings("deprecation")
	public Drawable getPhoto(CacheManager cacheManager,String key,String address){		
		this.address = subURL + address;
		this.cacheManager=cacheManager;
		this.key=key;
		sendRequestWithHttpURLConnection();
		if(cacheManager.getCache(key)==null){
			cacheManager.addCache(new CacheData<byte[]>(key, FileUtil.BitmapToBytes(bm, false)));
			d = new BitmapDrawable(bm);
		}else{
			d=new BitmapDrawable(FileUtil.ByteToBitmap((byte[])cacheManager.getCache(key).getData()));
			//更新头像
			cacheManager.addCache(new CacheData<byte[]>(key, FileUtil.BitmapToBytes(bm, false)));
		}
		return d;
	}
	
	
	
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
					bm = BitmapFactory.decodeStream(is, null, options);
					
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
	

}
