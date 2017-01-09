package com.atm.chatonline.usermsg.util;

import java.io.Serializable;

public class CacheData<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T data;
	private String key;
	private long lastUpdated;
	public CacheData(String key,T data){
		this.key=key;
		setData(data);
	}
	
	public void setData(T data){
		this.data=data;
		this.lastUpdated=System.currentTimeMillis();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getData() {
		return data;
	}
	
	
	
}
