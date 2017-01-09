package com.atm.chatonline.share;
/**
 * 该类用于把要分享的网页分享到微信
 * @author Jackbing
 * 2016.4.12
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.util.FileUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WebPageShare {
	private static IWXAPI api;
	private String url,title,desc;
	private Bitmap thumb;
	private static Activity context;
	private final String tag="TAG";
	public WebPageShare(Activity context,String url,String title,String desc,Bitmap thumb){
		api = WXAPIFactory.createWXAPI(context, UriAPI.APP_ID);
		api.registerApp(UriAPI.APP_ID);
		//api.handleIntent(context.getIntent(), this);
		this.context=context;
		this.thumb=thumb;
		this.title=title;
		this.url=url;
		this.desc=desc;
	}

	
//	
//	public static Activity getActivitys(){
//		if(context==null){
//			return null;
//		}
//		return context;
//	}
//	
//	public static IWXAPI getWXAPI(){
//		if(api==null){
//			api = WXAPIFactory.createWXAPI(context, "wx49dfff6ceb8cbbba",false);
//			api.registerApp("wx49dfff6ceb8cbbba");
//			return api;
//		}
//		return api; 
//	}
	
	/**
	 * 判断是否安装微信
	 * @return
	 */
	public boolean isInstalled(){
		if(api.isWXAppInstalled()){
			//还未安装微信
			return true;
		}
		return false;
	}
	
	/**
	 * 分享网页到微信
	 * @param flag 1表示分享到微信朋友圈 0表示分享给朋友
	 */
	public void shareToWx(int flag){
		
		WXWebpageObject webpage = new WXWebpageObject();
		//网页地址
	    webpage.webpageUrl = url; 
	    WXMediaMessage msg = new WXMediaMessage(webpage);  
	    //网页标题 
        msg.title = title; 
        //网页内容（前30个字）
        msg.description = desc; 
        //缩略图
        msg.thumbData=FileUtil.compressBitmapToByte(thumb, true,context);
        //发送到微信
        SendMessageToWX.Req req = new SendMessageToWX.Req();  
        req.transaction =buildTransaction("webpage");
        req.message = msg;  
        req.scene = flag;  
        api.sendReq(req); 
		
	}
	
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}



	

//	@Override
//	public void onReq(BaseReq arg0) {
//		// TODO 自动生成的方法存根
//		
//	}
//
//	/**
//	 * 分享完毕的回调
//	 */
//	@Override
//	public void onResp(BaseResp resp) {
//		
//		 switch (resp.errCode) {  
//	        case BaseResp.ErrCode.ERR_OK:  
//	           Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show(); 
//	           Log.i(tag, "分享成功");
//	            break;  
//	        case BaseResp.ErrCode.ERR_USER_CANCEL:  
//	        	Toast.makeText(context, "分享被取消", Toast.LENGTH_LONG).show();
//	        	Log.i(tag, "分享被取消");
//	            break;  
//	        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
//	        	Toast.makeText(context, "分享被拒绝", Toast.LENGTH_LONG).show();
//	        	Log.i(tag, "分享被拒绝");
//	            break;  
//	        default:  
//	        	Toast.makeText(context, "出现未知错误", Toast.LENGTH_LONG).show(); 
//	        	Log.i(tag, "出现未知错误");
//	            break;  
//	        }  
//		
//		
//	}
	
}
