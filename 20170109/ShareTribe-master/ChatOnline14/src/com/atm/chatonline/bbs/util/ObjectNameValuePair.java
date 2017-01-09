package com.atm.chatonline.bbs.util;

import org.apache.http.NameValuePair;

public class ObjectNameValuePair implements NameValuePair {
	String name;
	Object o;
	public ObjectNameValuePair(String name,Object o){
		this.o=o;
		this.name=name;
	}
	@Override
	public String getName() {
		// TODO 自动生成的方法存根
		return name;
	}

	@Override
	public String getValue() {
		// TODO 自动生成的方法存根
		return null;
	}
	
	public Object getObject(){
		return o;
	}

}
