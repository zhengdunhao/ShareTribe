/**
 * 
 */
package com.atm.charonline.recuit.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * @类 com.atm.charonline.util ---ExtendsIntent
 * @作用 继承intent,用于传参给下一个class.
 * @作者 陈小二
 * @时间 2015-8-16
 * 
 */
public class ExtendsIntent extends Intent {

	/**
	 * 
	 */
	public ExtendsIntent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param o
	 */
	public ExtendsIntent(Intent o) {
		super(o);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param action
	 */
	public ExtendsIntent(String action) {
		super(action);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param action
	 * @param uri
	 */
	public ExtendsIntent(String action, Uri uri) {
		super(action, uri);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param packageContext
	 * @param cls
	 */
	public ExtendsIntent(Context packageContext, Class<?> cls) {
		super(packageContext, cls);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param action
	 * @param uri
	 * @param packageContext
	 * @param cls
	 */
	public ExtendsIntent(String action, Uri uri, Context packageContext,
			Class<?> cls) {
		super(action, uri, packageContext, cls);
		// TODO Auto-generated constructor stub
	}

	public ExtendsIntent(Context packageContext, Class<?> cls , String id ,String relativePath , String tip , int page) {
		super(packageContext, cls);
		Bundle bundle = new Bundle();
		bundle.putString("relativePath",relativePath);
		bundle.putString("tip", tip);	
		bundle.putInt("page",page);
		bundle.putString("id",id);
		putExtras(bundle);
	}
}
