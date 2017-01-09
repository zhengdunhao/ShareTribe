package com.atm.chatonline.bbs.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MyWebChromeClient extends WebChromeClient {

	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			final JsResult result) {
		// TODO Auto-generated method stub
		   final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());  
	        builder.setTitle("对话框")  
	                .setMessage(message)  
	                .setPositiveButton("确定",new OnClickListener() {  
	                            public void onClick(DialogInterface dialog,int which) {  
	                                result.confirm();  
	                            }  
	                        })  
	                .setNeutralButton("取消", new OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int which) {  
	                        result.cancel();  
	                    }  
	                });  
	        builder.setOnCancelListener(new OnCancelListener() {  
	            @Override  
	            public void onCancel(DialogInterface dialog) {  
	                result.cancel();  
	            }  
	        });  
	  
	        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题  
	        builder.setOnKeyListener(new OnKeyListener() {  
	            @Override  
	            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {  
	                Log.v("onJsConfirm", "keyCode==" + keyCode + "event="+ event);  
	                return true;  
	            }
	        });  
	        // 禁止响应按back键的事件  
	        // builder.setCancelable(false);  
	        AlertDialog dialog = builder.create();  
	        dialog.show();  
	        return true;  
	        // return super.onJsConfirm(view, url, message, result);  
	}
	
}
