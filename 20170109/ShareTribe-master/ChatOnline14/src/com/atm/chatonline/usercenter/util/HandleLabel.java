package com.atm.chatonline.usercenter.util;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class HandleLabel {
	private HttpClient httpClient;
	private HttpResponse httpResponse;
	private HttpEntity httpEntity;
	private HttpPost httpPost;
	private String response;
	private String tag = "handleLabel";

	public void doPost(String URL, String cookie,JSONArray arr) {

		if(httpClient==null){
			httpClient = new DefaultHttpClient();
		}
		if(httpPost==null){
			httpPost = new HttpPost(URL);
		}
		
		Log.d("cookie----method2222", cookie);
		httpPost.setHeader("Cookie", cookie);
		httpPost.setEntity(dataToEntity(arr));
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			Log.d("HttpClientGet", "请求和响应都成功了1");
			httpEntity = httpResponse.getEntity();
			Log.i("size", "size=" + httpEntity.getContentLength());

			try {
				Log.d("HttpClientGet", "请求和响应都成功了2");
				String str = EntityUtils.toString(httpEntity, HTTP.UTF_8);

				Log.d("HttpClientGet", str.length() + "");
				setResponse(str);

			} catch (ParseException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public void startLoadBBS(String URL,String cookie,String tag,int page){
		if(httpClient==null){
			httpClient = new DefaultHttpClient();
		}
		if(httpPost==null){
			httpPost = new HttpPost(URL);
		}
		
		Log.d("cookie----method2222", cookie);
		httpPost.setHeader("Cookie", cookie);
		httpPost.setEntity(setParams(tag,page));
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			Log.d("HttpClientGet", "请求和响应都成功了1");
			httpEntity = httpResponse.getEntity();
			Log.i("size", "size=" + httpEntity.getContentLength());

			try {
				Log.d("HttpClientGet", "请求和响应都成功了2");
				String str = EntityUtils.toString(httpEntity, HTTP.UTF_8);

				Log.d("HttpClientGet", str.length() + "");
				setResponse(str);

			} catch (ParseException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public UrlEncodedFormEntity setParams(String tag,int page){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",tag));
		params.add(new BasicNameValuePair("page",page+""));
		try {
			return new UrlEncodedFormEntity(params,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return null;
	}
	public UrlEncodedFormEntity dataToEntity(JSONArray arr){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",arr.toString()));
		try {
			return new UrlEncodedFormEntity(params,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public StringEntity getStringEntity(JSONObject s){
		try {
			return new StringEntity(s.toString(),HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
	}
	public String getResponse() {
		Log.i(tag, "response:" + response);
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
