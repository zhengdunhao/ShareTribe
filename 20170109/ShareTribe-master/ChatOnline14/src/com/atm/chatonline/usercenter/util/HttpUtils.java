package com.atm.chatonline.usercenter.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 通过URL获取用户的信息，或者通过URL更新用户在本地修改的个人信息，也可以通过url访问用户的头像
 * 
 * @author Jackbing
 * @date 2016-10-2 修改
 */
public class HttpUtils {

	/**
	 * 直接从网络中获取bitmap图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String url) {

		try {
			URL uri = new URL(url);
			HttpURLConnection con = (HttpURLConnection) uri.openConnection();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return BitmapFactory.decodeStream(con.getInputStream());
			}
		} catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 简单的请求方法
	 * 
	 * @param url
	 * @return jsonObject
	 */
	public static JSONObject get(String url, List<BasicNameValuePair> params) {
		/*
		 * try { URL uri=new URL(url); HttpURLConnection con=(HttpURLConnection)
		 * uri.openConnection(); StringBuilder sb=new StringBuilder();
		 * StringBuffer sbf=new StringBuffer();
		 * 
		 * 
		 * 
		 * Log.i("response", "content="+con.getResponseMessage());
		 * con.disconnect();
		 * 
		 * } catch (MalformedURLException e) { // TODO 自动生成的 catch 块
		 * e.printStackTrace(); return null; } catch (IOException e) { // TODO
		 * 自动生成的 catch 块 e.printStackTrace(); return null; }
		 */

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			String out = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			Log.i("get", "out=" + out);
			return new JSONObject(out);

		} catch (ClientProtocolException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块

			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 发送更改过的数据给后台
	 * 
	 * @param url
	 * @param params
	 * @param file
	 * @return jsonobject
	 */
	@SuppressLint("NewApi")
	public static JSONObject post(String url, Map<String, String> params,
			Map<String, File> file) {
		// file的key是文件名
		JSONObject object = new JSONObject();
		try {
			object.put("tip", "failed");
		} catch (JSONException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
			return object;
		}
		String BOUNDARY = java.util.UUID.randomUUID().toString();// 随机生成的字符串
		String PREFIX = "--";
		String LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		HttpURLConnection con = null;
		DataOutputStream out = null;
		BufferedReader reader = null;
		InputStream is = null;
		try {
			URL uri = new URL(url);
			con = (HttpURLConnection) uri.openConnection();
			// con.setConnectTimeout(30000);
			con.setReadTimeout(30000);// 设置timeout
			con.setDoInput(true);// 允许输入
			con.setDoOutput(true);// 允许输出
			con.setUseCaches(false);// 不允许是使用缓存
			con.setRequestMethod("POST");
			con.setRequestProperty("connection", "keep-alive");
			con.setRequestProperty("Charset", CHARSET);
			con.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);
			Log.i("......", "参数设置完毕");
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type:text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			/*
			 * sb.append(PREFIX); sb.append(BOUNDARY); sb.append(LINEND);
			 * sb.append
			 * ("Content-Disposition: form-data;name=\""+"json"+"\""+LINEND);
			 * sb.append("Content-Type:text/plain; charset="+CHARSET+LINEND);
			 * sb.append("Content-Transfer-Encoding: 8bit"+LINEND);
			 * sb.append(LINEND); sb.append(jsonArray.toString());
			 * sb.append(LINEND);
			 */
			Log.i(">>>>>>>>", "con.getOutputStream");
			out = new DataOutputStream(con.getOutputStream());
			Log.i(">>>>>>>>", "write");
			out.write(sb.toString().getBytes());
			Log.i("......", "文本发送完毕");

			if (file != null) {
				for (Map.Entry<String, File> currentfile : file.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					// name是post中传参的键 filename是文件的名称
					sb1.append("Content-Disposition:form-data; name=\"file\"; filename=\""
							+ currentfile.getKey() + "\"" + LINEND);
					sb1.append("Content-Type:application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					out.write(sb1.toString().getBytes());

					is = new FileInputStream(currentfile.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}

					out.write(LINEND.getBytes());
					Log.i("......", "文件发送完毕");
				}
			}

			Log.i("----", "write data flush");

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			out.write(end_data);
			out.flush();
			// 得到响应码
			Log.i("_____", "getResponse");
			Log.i(">>>>", (con == null) + "");
			int res = con.getResponseCode();

			Log.i(">>>>>>>>>", "respCode =" + res);
			if (res == 200) {
				reader = new BufferedReader(new InputStreamReader(
						con.getInputStream(), "UTF-8"));
				int ch;
				String line;
				StringBuilder sb2 = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb2.append(line);
				}
				Log.i("resp", sb2.toString());
				object = new JSONObject(sb2.toString());

				Log.i("jsononject", "object=" + object.toString());
				return object;
			} else {
				int ch;
				StringBuilder ss = new StringBuilder();
				while ((ch = con.getInputStream().read()) != -1) {
					ss.append((char) ch);
				}
				Log.i("resp error", ss.toString());
			}

		} catch (java.net.ConnectException e) {
			return object;
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return object;
		} catch (IOException e) {

			e.printStackTrace();
			return object;
		} catch (JSONException e) {
			Log.i("1>>>>>>>", "error");
			e.printStackTrace();
			return object;
		} catch (Exception e) {
			Log.i("2>>>>>>>", "error");
			return object;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
		Log.i("3>>>>>>>", "error");
		return object;

	}
}
