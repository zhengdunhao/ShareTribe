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

/**
 * 
 * @author yuki 
 * date 2016-07-22
 */
public class BBSHttpClient {
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpPost httpPost;
	//Post方式需要一个NameValuePair集合来存放待提交的参数
	private HttpResponse httpResponse;//服务器返回的所有信息
	private HttpEntity httpEntity;	
	private UrlEncodedFormEntity entity;
	private String subURL = UriAPI.SUB_URL;
	private String relativePath;
	private String URL;
	private String response;
	private String cookie = "";
	private String tag = "BBSHttpClient";
	
	
	public BBSHttpClient(String cookie,String relativePath){
		this.cookie = cookie;
		this.relativePath = relativePath;
		URL = subURL + relativePath;
		Connect(URL, 1);
	}
	
	public BBSHttpClient(String dno,String cookie,String relativePath){
		this.cookie = cookie;
		this.relativePath = relativePath;
		URL = subURL + relativePath;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", dno));
		try {
			entity = new UrlEncodedFormEntity(params,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connect(URL, 2);
	}
	
	public BBSHttpClient(String cookie,String relativePath,int aim,String aimId,String reason){
		this.cookie = cookie;
		this.relativePath = relativePath;
		URL = subURL + relativePath;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("aim", ""+aim));
		params.add(new BasicNameValuePair("aimId", aimId));
		params.add(new BasicNameValuePair("reportReason", reason));
		try {
			entity = new UrlEncodedFormEntity(params,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connect(URL, 2);
	}

	public String getResponse() {
		return response;
	}

	//method=1为get请求，method=2为post请求
	public void Connect(String URL, int METHOD) {
		httpClient = new DefaultHttpClient();
		if (METHOD == 1) {
			httpGet = new HttpGet(URL);
			httpGet.setHeader("Cookie", cookie);
			try {
				httpResponse = httpClient.execute(httpGet);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			httpPost = new HttpPost(URL);
			httpPost.setHeader("Cookie", cookie);
			httpPost.setEntity(entity);
			try {
				httpResponse = httpClient.execute(httpPost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//状态码=200说明请求和响应都成功了
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			httpEntity = httpResponse.getEntity();
			Log.d("BBSHttpClient", "请求成功");
			try {
				String str = EntityUtils.toString(httpEntity, HTTP.UTF_8);
				setResponse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void setResponse(String str) {
		// TODO Auto-generated method stub
		response = str;
	}
}
