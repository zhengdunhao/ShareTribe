package com.atm.chatonline.bbs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.atm.chatonline.bbs.commom.NewMessage;

public class SharedPreferenceUtils {
	static  SharedPreferenceUtils preferenceUtils=null;


    public static SharedPreferenceUtils getInstance(){
        if(preferenceUtils==null){
            synchronized (SharedPreferenceUtils.class){
                if(preferenceUtils==null){
                    preferenceUtils=new SharedPreferenceUtils();
                }
            }
        }
        return preferenceUtils;

    }
	
	public   void saveNewMessage(NewMessage newMessage,Context context){
		SharedPreferences sf=context.getSharedPreferences("newMessage",Context.MODE_PRIVATE);
		Editor editor=sf.edit();
		editor.putInt("0", newMessage.getApplySum());
		editor.putInt("1", newMessage.getReplySum());
		editor.putInt("2", newMessage.getSystemSum());
		editor.putInt("sum", newMessage.getSum());
		editor.commit();
	}
	
	public  NewMessage getNewMessage(Context context){
		SharedPreferences sf=context.getSharedPreferences("newMessage",Context.MODE_PRIVATE);
		if(sf.getInt("sum", 0)!=0){
			return new NewMessage(sf.getInt("sum", 0),sf.getInt("0", 0),sf.getInt("1", 0),sf.getInt("2", 0));
		}
		return null;
	}
	
	
	 public   void clearLoginInfo(Context context){
	        SharedPreferences sf=context.getSharedPreferences("newMessage",Context.MODE_PRIVATE);
	        SharedPreferences.Editor editor=sf.edit();
	        editor.clear();
	        editor.commit();
	    }
	

}
