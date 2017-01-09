package com.atm.chatonline.bbs.bean;

import java.util.Date;
import java.sql.Timestamp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * @帖子显示 用在帖子列表中，包含帖子的大致信息（显示在外面的）
 * @author Jiong
 * @修改时间 2015年07月29日 19:48:59
 */
public class BBS implements java.io.Serializable {
	
	private String clickGoodNum; //点赞数
	private String publishTime; //发布时间
	private String headImagePath; //头像路径
	private String publisherId; //发布者账号
	private String essayId;   //帖子编号（作为点进去获取全部内容的标识）
	private String nickname;   //发布者昵称（15）
	private String replyNum;  //回复数
	private String labName;    //标签名（100）  示例：标签1*#标签2 （分隔符：*#）
	private String someContent;//部分帖子内容（50）
	private String title;      //帖子标题 （100）
	private String essayType;  //帖子类型 （20）
	private String labColor;	//标签颜色（100）示例：颜色1*颜色2（分隔符：*）
	transient private String department; //系别名称（20）（帖子外不显示，不发送给你,可删除）
	private String flag = "a";
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	transient private Drawable HeadImage;

	private String [] labName0 = {" "," "," "};
	private int [] labColor0 = {Color.parseColor("#EED5D2"),Color.parseColor("#EED5D2"),Color.parseColor("#EED5D2")};
	
	
	public String[] getLabName0() {
		return labName0;
	}

	public void setLabName0(String[] labName0) {
		this.labName0 = labName0;
	}

	public int[] getLabColor0() {
		return labColor0;
	}

	public void setLabColor0(int[] color) {
		this.labColor0 = color;
	}


	public Drawable getHeadImage() {
		return HeadImage;
	}

	public void setHeadImage(Drawable headImage) {
		HeadImage = headImage;
	}

	/**
	 * 
	 */
	public BBS() {
	}
	
	/**
	 * @param essayId
	 * @param essayType
	 * @param title
	 * @param labName
	 * @param someContent
	 * @param nickname
	 * @param publishTime
	 * @param clickGoodNum
	 * @param replyNum
	 */

	public String getEssayId() {
		return essayId;
	}
	public void setEssayId(String essayId) {
		this.essayId = essayId;
	}
	public String getEssayType() {
		return essayType;
	}
	public void setEssayType(String essayType) {
		this.essayType = essayType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public String getSomeContent() {
		return someContent;
	}
	public void setSomeContent(String someContent) {
		this.someContent = someContent;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickName) {
		this.nickname = nickName;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getClickGoodNum() {
		return clickGoodNum;
	}
	public void setClickGoodNum(String clickGoodNum) {
		this.clickGoodNum = clickGoodNum;
	}
	public String getReplyNum() {
		return replyNum;
	}
	public void setReplyNum(String collectNum) {
		this.replyNum = collectNum;
	}
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHeadImagePath() {
		return headImagePath;
	}

	public void setHeadImagePath(String headImagePath) {
		this.headImagePath = headImagePath;
	}

	public String getLabColor() {
		return labColor;
	}

	public void setLabColor(String labColor) {
		this.labColor = labColor;
	}
	
	
	
	
}
