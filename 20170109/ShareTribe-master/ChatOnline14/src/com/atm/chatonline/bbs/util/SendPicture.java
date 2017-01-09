package com.atm.chatonline.bbs.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

public class SendPicture {
	
	@SuppressLint("NewApi")
	public static JSONObject post(String url,JSONArray jsonArray,Map<String,File> file){
																	//file的key是文件名			
		JSONObject object=new JSONObject();
		try {
			object.put("tip", "failed");
		} catch (JSONException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		String BOUNDARY=java.util.UUID.randomUUID().toString();//随机生成的字符串
		String PREFIX="--";
		String LINEND="\r\n";
		String MULTIPART_FROM_DATA="multipart/form-data";
		String CHARSET="UTF-8";
		
		
		try {
			URL uri=new URL(url);
			HttpURLConnection con=(HttpURLConnection)uri.openConnection();
			//con.setConnectTimeout(30000);
			con.setReadTimeout(30000);//设置timeout
			con.setDoInput(true);//允许输入
			con.setDoOutput(true);//允许输出
			con.setUseCaches(false);//不允许是使用缓存
			con.setRequestMethod("POST");
			con.setRequestProperty("connection", "keep-alive");
			con.setRequestProperty("Charset", CHARSET);
			con.setRequestProperty("Content-Type", MULTIPART_FROM_DATA+";boundary="+BOUNDARY);
			Log.i("......", "参数设置完毕");
			StringBuilder sb=new StringBuilder();
			/*for(Map.Entry<String, String> entry:params.entrySet()){
				sb.append(PREFIX);
	            sb.append(BOUNDARY);
	            sb.append(LINEND);
	            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
	            sb.append("Content-Type:text/plain; charset=" + CHARSET + LINEND);
	            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
	            sb.append(LINEND);
	            sb.append(entry.getValue());
	            sb.append(LINEND);
			}*/
			sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data;name=\""+"json"+"\""+LINEND);
            sb.append("Content-Type:text/plain; charset="+CHARSET+LINEND);
            sb.append("Content-Transfer-Encoding: 8bit"+LINEND);
            sb.append(LINEND);
            sb.append(jsonArray.toString());
            sb.append(LINEND);
            Log.i(">>>>>>>>", "con.getOutputStream");
			DataOutputStream out=new DataOutputStream(con.getOutputStream());
			Log.i(">>>>>>>>", "write");
			out.write(sb.toString().getBytes());
			Log.i("......", "文本发送完毕");
			InputStream in=null;
			
			if(file!=null){
				for(Map.Entry<String, File> currentfile:file.entrySet()){
					StringBuilder sb1 = new StringBuilder();
	                sb1.append(PREFIX);
	                sb1.append(BOUNDARY);
	                sb1.append(LINEND);
	                // name是post中传参的键 filename是文件的名称
	                sb1.append("Content-Disposition:form-data; name=\"file\"; filename=\"" + currentfile.getKey() + "\"" + LINEND);
	                sb1.append("Content-Type:application/octet-stream; charset=" + CHARSET + LINEND);
	                sb1.append(LINEND);
	                out.write(sb1.toString().getBytes());

	                InputStream is = new FileInputStream(currentfile.getValue());
	                byte[] buffer = new byte[1024];
	                int len = 0;
	                while ((len = is.read(buffer)) != -1)
	                {
	                    out.write(buffer, 0, len);
	                }

	                is.close();
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
            Log.i(">>>>",(con==null)+"");
            int res = con.getResponseCode();
            
            Log.i(">>>>>>>>>", "respCode ="+res);
            if (res == 200)
            {
                in = con.getInputStream();
                int ch;
                StringBuilder sb2 = new StringBuilder();
                while ((ch = in.read()) != -1)
                {
                    sb2.append((char) ch);
                }
                Log.i("resp", sb2.toString());
               JSONArray arr=new JSONArray(sb2.toString());
             
                object=arr.getJSONObject(0);
                Log.i("jsononject", "object="+object.toString());
                return object;
            }
            
            
            out.close();
            con.disconnect();
        
       
		} catch(java.net.ConnectException e){
			return object;
		}catch (MalformedURLException e) {
			
			e.printStackTrace();
			return object;
		} catch (IOException e) {
			
			e.printStackTrace();
			return object;
		} catch (JSONException e) {
			
			return object;
		}catch(Exception e){
			
			return object;
		}
		return object;
		
	}
	
	
	
	/*public static void clientPost(String url,JSONArray jsonArray,Map<String,File> file){
		String BOUNDARY=java.util.UUID.randomUUID().toString();//随机生成的字符串
		String PREFIX="--";
		String LINEND="\r\n";
		String MULTIPART_FROM_DATA="multipart/from-data";
		String CHARSET="UTF-8";
		HttpClient client=new DefaultHttpClient();
		HttpPost post=new HttpPost(url);
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
		
		
	}*/
}
