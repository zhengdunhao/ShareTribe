package com.atm.chatonline.schoolnews.bean;

import android.graphics.drawable.Drawable;

/**
 * 新闻实体类
 * @Description: 新闻实体类

 * @File: News.java

 * @Package com.atm.chatonline.schoolnews.bean

 * @Author zhengdunhao

 * @Date 2015-10-17 

 * @Version V1.0
 */
public class News {
	// 新闻的ID
	private String newsId;
	// 新闻主标题
	private String mainTitle;
	// 新闻副标题
	private String viceTitle;
	// 查看次数
	private String viewCount;
	//新闻图片路径
	private String newsImagePath;
  
	
	transient private Drawable NewsImage;
//	// 链接地址
//	private String newsUrl;
//	// 新闻内容
//	private String newsContent;
	
	
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	
	public String getMainTitle() {
		return mainTitle;
	}
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	public String getViceTitle() {
		return viceTitle;
	}
	public void setViceTitle(String viceTitle) {
		this.viceTitle = viceTitle;
	}

	public String getViewCount() {
		return viewCount;
	}
	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}
	public String getNewsImagePath() {
		return newsImagePath;
	}
	public void setNewsImagePath(String newsImagePath) {
		this.newsImagePath = newsImagePath;
	}
	
	public Drawable getNewsImage() {
		return NewsImage;
	}

	public void setNewsImage(Drawable newsImage) {
		NewsImage = newsImage;
	}

//	// 评论次数
//	private int commentCount;
//	// 是否被读过
//	private boolean isReaded;
	
	
}
