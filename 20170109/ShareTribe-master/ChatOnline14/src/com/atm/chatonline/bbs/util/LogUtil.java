/**
 * 
 */
package com.atm.chatonline.bbs.util;

import android.app.Activity;
import android.util.Log;

/**
 * @类 com.atm.charonline.bbs.util ---LogUtil
 * @作用 自定义日志工具：测试时打印，非测试时可屏蔽掉。
 * @作者 陈小二
 * @时间 2015-10-26
 * 
 */
public class LogUtil {
	public static final int VERBOSE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5; 
	public static final int NOTHING = 6;
	public static final int LEVEL = VERBOSE;
	/*
	 *     因为方法中有if判断，只有当LEVEL常量的值小于或等于对应日志级别的时候。
	 * 才会将日志打印出来。
	 * 		所以，可以在开发阶段设定LEVEL值为VERBOSE,当项目正式上线的时候将
	 * LEVEL指定为NOTHING就可以了。
	 */
	
	//获取tag,形式：类名.方法名（）：行号。  这样写获取的是当前这个类
//	public static String getTag() { 
//		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
//		if(elements.length<4) {
//			return "Stack to shallow";
//		}
//		else {
//			String fullClassName = elements[3].getClassName();
//			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
//			String methodName = elements[3].getMethodName();
//			int lineNumber = elements[3].getLineNumber();
//			String tag1 = className+"."+methodName+"():"+lineNumber+"行";
//			return tag1;
//		}
//	}

	/**
	 * 级别为VERBOSE。
	 * @param msg
	 * 要打印的信息
	 */
	public static void v(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil出错", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"行";
			if(LEVEL <= VERBOSE)
				Log.v(tag,msg);
		}
	}
	/**
	 * 级别为DEBUG。
	 * @param msg
	 * 要打印的信息
	 */
	public static void d(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil出错", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"行";
			if(LEVEL <= DEBUG)
				Log.d(tag,msg);
		}
	}
	/**
	 * 级别为INFO。
	 * @param msg
	 * 要打印的信息
	 */
	public static void i(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil出错", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"行";
			if(LEVEL <= INFO)
				Log.i(tag,msg);
		}
	}
	/**
	 * 级别为WARN。
	 * @param msg
	 * 要打印的信息
	 */
	public static void w(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil出错", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"行";
			if(LEVEL <= WARN)
				Log.w(tag,msg);
		}
	}
	/**
	 * 级别为ERROR。
	 * @param msg
	 * 要打印的信息
	 */
	public static void e(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil出错", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"行";
			if(LEVEL <= ERROR)
				Log.e(tag,msg);
		}
	}
	/**
	 * 键值对的方式打印信息，p代表打印,级别为DEBUG。
	 * @param key
	 * 键
	 * @param value
	 * 值
	 */
	public static void p(String key, String value) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil出错", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"行";
			Log.d(tag, key+":"+value);
		}
	}
}
