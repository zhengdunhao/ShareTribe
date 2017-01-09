package com.atm.chatonline.bbs.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @TODO：多文件上传测试，中文会乱码
 * @fileName : com.atm.test.image.manyFile.java date | author | version |
 *           2015年10月5日 | Jiong | 1.0 |
 */

/*
 * 修改历史：
 */
public class SendDataToServer {
	String multipart_form_data = "multipart/form-data";
	String twoHyphens = "--";
	String boundary = "****************fD4fH3gL0hK7aI6"; // 数据分隔符
	String lineEnd = "\r\n";
	String cookie = null;														 

	/*
	 * 上传图片内容，格式请参考HTTP 协议格式。
	 * 人人网Photos.upload中的”程序调用“http://wiki.dev.renren.com/
	 * wiki/Photos.upload#.E7.A8.8B.E5.BA.8F.E8.B0.83.E7.94.A8 对其格式解释的非常清晰。
	 * 格式如下所示： --****************fD4fH3hK7aI6 Content-Disposition: form-data;
	 * name="upload_file"; filename="apple.jpg" Content-Type: image/jpeg
	 * 
	 * 这儿是文件的内容，二进制流的形式
	 */
	private void addFileContent(Bitmap[] files, DataOutputStream output) {
		if (files == null) { // 没有文件直接退出方法
			return;
		}
		// 循环将文件格式写入到头文件中
		for (Bitmap file : files) {
			StringBuilder split = new StringBuilder();
			split.append(twoHyphens + boundary + lineEnd);
			// 这里的files需要和服务器接收的名字一样，服务器为files
			split.append("Content-Disposition: form-data; name=\"files\"; filename=\""
					+ "picture1.jpg" + "\"" + lineEnd);
			split.append("Content-Type: application/octet-stream; charset=utf-8"
					+ lineEnd);
			split.append(lineEnd);
			try {

				output.writeBytes(split.toString()); // 写入前面的格式
				// 将文件化作字节流并写入

				byte[] bytes = Bitmap2Bytes(file);

				output.write(bytes, 0, bytes.length);

				output.writeBytes(lineEnd); // 写入后面的格式
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/*
	 * 构建表单字段内容，格式请参考HTTP 协议格式（用FireBug可以抓取到相关数据）。(以便上传表单相对应的参数值) 格式如下所示：
	 * --****************fD4fH3hK7aI6 Content-Disposition: form-data;
	 * name="action" // 一空行，必须有 upload
	 */
	private void addFormField(Map<Object, Object> params,
			DataOutputStream output) {
		StringBuilder sb = new StringBuilder();
		// 循环将表单格式写入到头文件中
		// entrySet()---------------------
		for (Map.Entry<Object, Object> param : params.entrySet()) {
			sb.append(twoHyphens + boundary + lineEnd);
			sb.append("Content-Disposition: form-data; name=\""
					+ param.getKey() + "\"" + lineEnd);
			sb.append(lineEnd);
			sb.append(param.getValue() + lineEnd);
		}
		try {
			output.write(sb.toString().getBytes("UTF-8"));// 写入表单字段数据
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 直接通过 HTTP 协议提交数据到服务器，实现表单提交功能。
	 * 
	 * @param actionUrl
	 *            上传路径
	 * @param params
	 *            请求参数key为参数名，value为参数值
	 * @param files
	 *            上传文件信息
	 * @return 返回请求结果
	 */
	//List<String> cookies = null;// 多次请求时可能用到cookie（我是直接用纯java作测试）

	public String post(String actionUrl, Map<Object, Object> params,
			Bitmap[] files,String cookie) {
		Log.i("SendDataToServer", lineEnd);
		HttpURLConnection conn = null;
		DataOutputStream output = null;
		BufferedReader input = null;
		try {
			URL url = new URL(actionUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(120000);
			conn.setRequestProperty("Charset", "utf-8"); // 设置编码
			conn.setDoInput(true); // 允许输入
			conn.setDoOutput(true); // 允许输出
			conn.setUseCaches(false); // 不使用Cache
			conn.setRequestMethod("POST");
			if (cookie != null) {
				//System.out.println("获取到cookie");
				
					conn.addRequestProperty("Cookie", cookie);
				
			}
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", multipart_form_data
					+ "; boundary=" + boundary);

			conn.connect();
			output = new DataOutputStream(conn.getOutputStream());

			addFileContent(files, output); // 添加图片内容
			addFormField(params, output); // 表单内容

			output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// 数据结束标志
			output.flush();
			int code = conn.getResponseCode(); // 获取服务器的响应代码
			cookie = conn.getHeaderField("Set-Cookie");//.get("Set-Cookie");// 获取响应头字段的值
			
		
			if (code != 200) {
				Log.i("code", "" + code);
				throw new RuntimeException("请求‘" + actionUrl + "’失败！");
			}

			input = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String oneLine;
			while ((oneLine = input.readLine()) != null) {
				response.append(oneLine + lineEnd);
			}

			return response.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// 统一释放资源
			try {
				if (output != null) {
					output.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (conn != null) {
				conn.disconnect();
			}
			Log.i("SendDataToServer", "结束方法");
		}

	} /*
	 * public static void main(String[] args) {
	 * 
	 * SendDataToServer go = new SendDataToServer();
	 * 
	 * String response = ""; //包含了图片的发布帖子路径改成这个！！！！！！ String actionUrl =
	 * "http://139.129.131.179/ATM/essay/publish.do";
	 * 
	 * //先登录 String loginUrl = "http://139.129.131.179/ATM/user_login.action";
	 * Map<Object,Object> loginParams = new HashMap<Object,Object>();
	 * loginParams.put("userId", "1"); loginParams.put("password", "1");
	 * response = go.post(loginUrl, loginParams, null);
	 * System.out.println("返回结果：" + response);
	 * 
	 * //表单，即是哪些标题啊什么乱七八糟的内容 Map<Object,Object> params = new
	 * HashMap<Object,Object>(); params.put("type", "测试"); params.put("label",
	 * "上传*#多文件"); params.put("title", "标题时什么"); params.put("department",
	 * "外卖系"); params.put("content", "多文件上传测试");
	 * 
	 * //文件（图片） ArrayList<File> list = new ArrayList<File>(); list.add(new
	 * File("E:\\test-0.jpg")); list.add(new File("E:\\test-1.jpg")); File[]
	 * files = (File[])list.toArray(new File[list.size()]);
	 * 
	 * //发送请求 response = go.post(actionUrl, params, files);
	 * System.out.println("返回结果：" + response);
	 * 
	 * }
	 */
}
