package com.zylbaby.app.bean;

public class TeacherInfoMainBean {
	private int babyid;
	private String name;
	private String babyimg;
	private String infotime;
	private String content;
	private String firstcontent;
	private int count;
	
	public String getInfotime() {
		return infotime;
	}
	public void setInfotime(String infotime) {
		this.infotime = infotime;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFirstcontent() {
		return firstcontent;
	}
	public void setFirstcontent(String firstcontent) {
		this.firstcontent = firstcontent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getBabyid() {
		return babyid;
	}
	public void setBabyid(int babyid) {
		this.babyid = babyid;
	}
	public String getBabyimg() {
		return babyimg;
	}
	public void setBabyimg(String babyimg) {
		this.babyimg = babyimg;
	}
	
}
