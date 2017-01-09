package com.example.studentsystem01.wxapi;
/**
 * 该类用于微信分享结果的回调，包名必须以项目包名+.wxapi来做为该类的包
 * 而且该类必须以WXEntryActivity命名，继承Acivity 实现IWXAPIEventHandler才能实现回调
 * @author Jackbing
 * 2016.4.13
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	
	private final  String tag="******";
	private IWXAPI api;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this,  
                UriAPI.APP_ID, false); 
		api.registerApp(UriAPI.APP_ID);
        api.handleIntent(getIntent(), this); 
	}

	@Override
	public void onReq(BaseReq rep) {
		 
		

	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {  
        case BaseResp.ErrCode.ERR_OK:  
           Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show(); 
           Log.i(tag, "分享成功");
            break;  
        case BaseResp.ErrCode.ERR_USER_CANCEL:  
        	Toast.makeText(this, "分享被取消", Toast.LENGTH_LONG).show();
        	Log.i(tag, "分享被取消");
            break;  
        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
        	Toast.makeText(this, "分享被拒绝", Toast.LENGTH_LONG).show();
        	Log.i(tag, "分享被拒绝");
            break;  
        default:  
        	Toast.makeText(this, resp.errStr+resp.errCode, Toast.LENGTH_LONG).show(); 
        	Log.i(tag, resp.errStr+resp.errCode);
            break;  
        }  

		
		this.finish();
	}

}
