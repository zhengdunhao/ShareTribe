package com.atm.chatonline.usermsg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class CacheManager {

	
	
	public static CacheManager instance;
	public static Context mContext;
	
	public static void init(Context context){
		mContext=context;
	}
	public CacheManager(){}
	public static CacheManager getInstance(){
		if(instance==null) instance=new CacheManager();
		return instance;
	}
	
	
	public void addCache(CacheData cacheData){
		if (cacheData == null) return;
        try {
            File file = new File(mContext.getCacheDir(), cacheData.getKey());
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(cacheData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入缓存出错");
        }
	}
	
	@SuppressWarnings("unused")
	public CacheData getCache(String key){
		try {
			System.out.println(mContext.getCacheDir().toString());
			File file=new File(mContext.getCacheDir(),key);
			
			if(file==null) {
				
				return null;
			}
		
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
			
			CacheData cacheData=(CacheData) ois.readObject();
			
			ois.close();
			System.out.println("缓存数据获取成功");
			return cacheData;
		} catch (Exception e) {
			System.out.println("获取缓存数据失败");
		}
		return null;
	}
	
}
