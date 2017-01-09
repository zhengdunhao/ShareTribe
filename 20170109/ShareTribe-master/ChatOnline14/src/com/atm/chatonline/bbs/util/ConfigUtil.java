/**
 * 
 */
package com.atm.chatonline.bbs.util;

import android.graphics.drawable.Drawable;

/**
 * @类 com.atm.charonline.bbs.util ---Config
 * @作用
 * @作者 陈小二
 * @时间 2015-11-14
 * 
 */
public class ConfigUtil {

	public static String COOKIE = "";
	public static int nums = 0;
	public static Drawable head = null;

	public static Drawable getHead() {
		return head;
	}

	public static void setHead(Drawable head) {
		ConfigUtil.head = head;
	}

	public static String getCookie() {
		return COOKIE;
	}
}
