package com.zylbaby.app.bean;

public class InfoItem {

	private int byuser;
	private int touser;
	private String content;
	private String time;
	private int infotype;
	private String imgpath;
	
	private boolean myself;
	
	public int getByuser() {
		return byuser;
	}
	public void setByuser(int byuser) {
		this.byuser = byuser;
	}
	public int getTouser() {
		return touser;
	}
	public void setTouser(int touser) {
		this.touser = touser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getInfotype() {
		return infotype;
	}
	public void setInfotype(int infotype) {
		this.infotype = infotype;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public boolean isMyself() {
		return myself;
	}
	public void setMyself(boolean myself) {
		this.myself = myself;
	}
}
