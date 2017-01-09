package com.atm.chatonline.usermsg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class CacheManager {

	//被volatile修饰的变量的值，将不会被本地线程缓存，
	//所有对该变量的读写都是直接操作共享内存,从而确保多个线程能正确的处理该变量。
	
	public volatile static CacheManager instance;
	public static Context mContext;
	
	public static void init(Context context){
		mContext=context;
	}
	public CacheManager(){}
	/**
     *双检索
	 * @return
	 */
	public static CacheManager getInstance(){
		if(instance==null) {
			synchronized (CacheManager.class) {
				if(instance==null){
					instance=new CacheManager();
				}
			}
		}
		return instance;
	}
	
	
	public void addCache(CacheData cacheData){
		if (cacheData == null) return;
		
		 ObjectOutputStream oos=null;
       try {
            File file = new File(mContext.getCacheDir(), cacheData.getKey());
            if (!file.exists()) {
                file.createNewFile();
            }
            oos= new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(cacheData);
           
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(oos!=null){
        		try {
					oos.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
        	}
        }
	}
	
	@SuppressWarnings("unused")
	public CacheData getCache(String key){
		ObjectInputStream ois=null;
		try {
			System.out.println(mContext.getCacheDir().toString());
			File file=new File(mContext.getCacheDir(),key);
			
			if(file==null) {
				
				return null;
			}
		
			ois=new ObjectInputStream(new FileInputStream(file));
			
			CacheData cacheData=(CacheData) ois.readObject();
			
			
			System.out.println("缓存数据获取成功");
			return cacheData;
		} catch (Exception e) {
			System.out.println("获取缓存数据失败");
			e.printStackTrace();
		}finally{
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
