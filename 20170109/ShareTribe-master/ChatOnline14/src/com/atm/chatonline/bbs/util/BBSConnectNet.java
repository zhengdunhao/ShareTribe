package com.atm.chatonline.bbs.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.util.Config;

public class BBSConnectNet {
	
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpResponse httpResponse;
	private HttpEntity httpEntity;
	
	private HttpPost httpPost;
	private UrlEncodedFormEntity entity;
	private String subURL = UriAPI.SUB_URL;
	private String relativePath = "essay_mainEssay.action";
	private String URL;
	private String response;
	private String cookie ="";
	
	private String tag = "BBSConnectNet";
	
	public void Connect(String URL,int METHOD){
		Log.d("BBSConnectNet顺序","connect（）");
		httpClient = new DefaultHttpClient();
		if(METHOD == 1){
			httpGet = new HttpGet(URL);
			Log.d("cookie----method1111",cookie);
			httpGet.setHeader( "Cookie" , cookie);
			try {
				httpResponse = httpClient.execute(httpGet);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			httpPost = new HttpPost(URL);
			Log.d("cookie----method2222",cookie);
			httpPost.setHeader( "Cookie" , cookie);
			httpPost.setEntity(entity);
			try{
				httpResponse = httpClient.execute(httpPost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(httpResponse.getStatusLine().getStatusCode() == 200){
			Log.d("HttpClientGet","请求和响应都成功了1");
			httpEntity = httpResponse.getEntity();
			Log.i("size", "size="+httpEntity.getContentLength());
			
			try {
				Log.d("HttpClientGet","请求和响应都成功了2");
				String str=EntityUtils.toString(httpEntity,HTTP.UTF_8);
				
				Log.d("HttpClientGet",str.length()+"");
				setResponse(str);
				
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	public BBSConnectNet(){}//服务于ReceiveMyLabel
	public BBSConnectNet(String relativePath){
		
		URL = subURL + relativePath;
		Connect(URL,1);
	}

	public BBSConnectNet(int page ){
		this(null,null,page,null,null);
//		URL = subURL + relativePath;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("page",page+""));
//		try {
//			entity = new UrlEncodedFormEntity(params,"utf-8");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		Connect(URL,2);
		
	}
	public BBSConnectNet(String tip , String id , 
							int page ,String relativePath){
		this(tip,id,page,relativePath,null);
//		URL = subURL + relativePath;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("page",page+""));
//		params.add(new BasicNameValuePair("tips",tip));
//		params.add(new BasicNameValuePair("id",id));
//		Log.d("page",page+"");
//		Log.d("URL",URL);
//		Log.d("cookie",cookie);
//		try {
//			entity = new UrlEncodedFormEntity(params,"utf-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//		Log.d("BBSConnectNet顺序","四个参结束");
//		Connect(URL,2);
		
	}

	public BBSConnectNet(String tip , String id , 
							int page ,String relativePath,String cookie){
		this.cookie=cookie;
		URL = subURL + relativePath;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page",page+""));
		params.add(new BasicNameValuePair("tip",tip));
		params.add(new BasicNameValuePair("id",id));
		Log.d("page",page+"");
		Log.d("URL",URL);
		Log.d("cookie",cookie);
		try {
			entity = new UrlEncodedFormEntity(params,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Log.d("BBSConnectNet顺序","四个参结束");
		Connect(URL,2);
		
	}

	
	public BBSConnectNet(int page , String relativePath){
		URL = subURL + relativePath;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page",page+""));
		try {
			entity = new UrlEncodedFormEntity(params,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				Connect(URL,2);
		
	}
	
	public BBSConnectNet(String essayId, String relativePath, String cookie) {
		this.cookie=cookie;
		URL = subURL + relativePath;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("essayId", essayId));
		try {
			entity = new UrlEncodedFormEntity(params, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Connect(URL, 2);

	}
	
	
	public BBSConnectNet(String essayId, String relativePath, String cookie,boolean flag) {
		this.cookie = cookie;
		URL = subURL + relativePath;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("essayId", essayId));
		if(relativePath.equals("essay_collectEssay.action")){			
		params.add(new BasicNameValuePair("collect", flag + ""));
		}else{
			params.add(new BasicNameValuePair("clickGood", flag + ""));
		}
		try {
			entity = new UrlEncodedFormEntity(params, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Connect(URL, 2);

	}
	
	public BBSConnectNet(String type, String title, String content,
			String label, String department, String cookie) {
		this.cookie=cookie;
		//URL = subURL + "essay_publishEssay.action";
		URL = subURL + "essay/publish.do";//修改为这个2015.10.06
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("title", title));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("label", label));
		params.add(new BasicNameValuePair("department", department));
		try {
			entity = new UrlEncodedFormEntity(params, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connect(URL, 2);

	}
	
	//校友动态可以使用
	public BBSConnectNet(int page , String relativePath,String cookie){
		this.cookie=cookie;
		URL = subURL + relativePath;
		Log.i(tag,"BBSConnectNet---URL:"+URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page",page+""));
		try {
			entity = new UrlEncodedFormEntity(params,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				Connect(URL,2);
		
	}
	
	public BBSConnectNet(String topNewsRelativePath,String cookie){
		this.cookie=cookie;
		URL = subURL + topNewsRelativePath;
		Log.i(tag,"BBSConnectNet---URL:"+URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page",""));//对头条新闻来说，没多大帮助
		try {
			entity = new UrlEncodedFormEntity(params,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				Connect(URL,2);
	}
	
	//改密码或换绑邮箱 通过flag来判断是改密码还是换绑邮箱还是反馈消息
	public BBSConnectNet(String changeThingRelativePath,String cookie,String userID,String newThing,int flag){
		this.cookie = cookie;
		URL = subURL + changeThingRelativePath;
		Log.i(tag,"BBSConnectNet---URL:"+URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId",userID));
		//通过flag来判断是改密码还是换绑邮箱
		if(flag==Config.CHANGE_PWD){
			params.add(new BasicNameValuePair("userPwd",newThing));
		}else if(flag==Config.CHANGE_EMAIL){
			params.add(new BasicNameValuePair("email",newThing));
		}else if(flag==Config.FEEDBACK_MESSAGE){
			params.add(new BasicNameValuePair("fee",newThing));
		}
		try{
			entity = new UrlEncodedFormEntity(params,"utf-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		Connect(URL,2);
	}
	
	public String getResponse() {
		Log.i(tag, "getResponse（）");
//		Log.i("77777","response.length()"+response.length()+"");
		Log.i(tag, "response:"+response);
		
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
		Log.d("BBSConnectNet.setCookie",cookie);
	}
/*
    private void getCookie(DefaultHttpClient httpClient) {
         List<Cookie> cookies = httpClient.getCookieStore().getCookies();
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < cookies.size(); i++) {
             Cookie cookie = cookies.get(i);
             String cookieName = cookie.getName();
             String cookieValue = cookie.getValue();
             if (!TextUtils.isEmpty(cookieName)
                      && !TextUtils.isEmpty(cookieValue)) {
                 sb.append(cookieName + "=" );
                 sb.append(cookieValue + ";" );
            }
        }
        Log. e( "cookie", sb.toString());
        Util.savePreference( "cookie", sb.toString());
   }*/
}
