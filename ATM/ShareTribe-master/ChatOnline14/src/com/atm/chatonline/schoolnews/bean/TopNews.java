package com.atm.chatonline.schoolnews.bean;

import android.graphics.drawable.Drawable;

public class TopNews {
	// 新闻的ID
	private String newsId;
	// 新闻主标题
	private String mainTitle;
	// 新闻图片路径
	private String newsImagePath;

	transient private Drawable NewsImage;

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
}
