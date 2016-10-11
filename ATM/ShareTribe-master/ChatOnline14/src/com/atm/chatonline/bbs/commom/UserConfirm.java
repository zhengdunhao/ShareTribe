package com.atm.chatonline.bbs.commom;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

public class UserConfirm {
	private String tag = "UserConfirm";
	
	public UserConfirm(){}
	
	public String userConfirm(String number, String password, String flag) {
		Log.i(tag,"number:" + number + ",pasword:" + password + ",flag:" + flag);
		
		Connection connection = getConnection("http://jwc.gduf.edu.cn/")
				.header("Host", "jwc.gduf.edu.cn")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
				.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
				.header("Accept-Encoding", "gzip, deflate")
				.header("referer", "http://jwc.gduf.edu.cn/")
				.header("Connection", "Keep-Alive")
				.header("Cache-Control", "max-age=0");
		Response response = null;
		try {
			Log.i(tag,"连接教务系统首页");
			response = connection.execute();// 获取响应
			Log.i(tag,"获取document");
			Document document = Jsoup.parse(response.body());// 转换为Dom树
			Log.i(tag,"title:" + document.title() + ",body:" + document.data());
			Map<String, String> map = response.cookies();
			for (String s : map.keySet()) {
				System.out.println(s + "      " + map.get(s));
			}
			// 参数
			Map<String, String> params = new HashMap<String, String>();
			String __VIEWSTATE = "dDwyODE2NTM0OTg7Oz4faMm4vKVufBJeqp0/4YQv7YksRA==";
			params.put("txtUserName", number);
			params.put("TextBox2", password);
			params.put("__VIEWSTATE", __VIEWSTATE);
			params.put("txtSecretCode", "");
			params.put("RadioButtonList1", flag);
			params.put("Button1", "登录");
			Log.i(tag,"设置参数完毕");
			// header
			String referer = "http://jwc.gduf.edu.cn/xs_main.aspx?xh="
					+ number;
			Log.i(tag,"准备再次连接");
			Connection connection2 = getConnection(referer);//getConnection("http://jwc.gduf.edu.cn/");
			// 设置cookie和post上面的map数据
			Log.i(tag,"response");
			Response login=null;
			Log.i(tag,"---");
			try{
				Log.i(tag,"进入连接登录教务系统");
				connection2 = connection2
						.ignoreContentType(true)
						.data(params)
						.data("query", "Java")   // 请求参数
						/*.userAgent(
								"Mozilla/5.0 (Windows NT 6.2; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")*/
						.header("Host", "jwc.gduf.edu.cn")
						.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
						.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
						.header("Accept-Encoding", "gzip, deflate")
						.header("referer", referer)
						.header("Connection", "Keep-Alive")
						.cookies(map)
						.timeout(3000)
						.method(Method.POST);
				Log.i(tag,"connect-------");
				login=connection2.execute();
				Log.i(tag,"连接成功");
			}catch(Exception e){
				Log.i(tag,"连接失败");
				e.printStackTrace();
				throw e;
			}
			Log.i(tag,"获取成功");
			// 打印，登陆成功后的信息
			//System.out.println(login.body());
			System.out.println("-----------------------------");
			// 登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
			map = login.cookies();
			for (String s : map.keySet()) {
				System.out.println(s + "      " + map.get(s));
			}
			Document document2 = login.parse();
			Log.i(tag,"title:" + document.title());
			Element element = document2.getElementById("xhxm");
			if (element != null&&element.tagName().equals("span")) {
				//是否有这个元素
				System.out.println("success-tag:" + element.tagName());
				String name = element.text();
				name = name.substring(0, name.length()-2);
				System.out.println(name);
				return name;
			} else {
				System.out.println("error");
				return "";
			}
		} catch (Exception e) {
			Log.i(tag,"error--");
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取连接
	 * 
	 * @param url
	 * @return
	 */
	public Connection getConnection(String url) {
		// 获取连接
		Connection connection = Jsoup.connect(url);
		// 配置模拟浏览器
		connection
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.2; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
		return connection;
	}
}
