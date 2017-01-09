/**
 * 
 */
package com.atm.chatonline.recuit.bean;

/**
 * @类 com.atm.charonline.recuit ---Recuit
 * @作用 
 * @作者 陈小二
 * @时间 2015-9-19
 * 
 */
public class Recuit {
	
	private String reInfoId="";//帖子ID
	private String reTypeName=""; //招聘类型
	private String woTypeName="";//工作类型
	private String workAddress="";//工作地点
	private String salary="";//薪资
	private String publishTime="";//发布时间
	
	public String getReTypeName() {
		return reTypeName;
	}
	public void setReTypeName(String reTypeName) {
		this.reTypeName = reTypeName;
	}
	public String getWoTypeName() {
		return woTypeName;
	}
	public void setWoTypeName(String woTypeName) {
		this.woTypeName = woTypeName;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getReInfoId() {
		return reInfoId;
	}
	public void setReInfoId(String reInfoId) {
		this.reInfoId = reInfoId;
	}
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
}
