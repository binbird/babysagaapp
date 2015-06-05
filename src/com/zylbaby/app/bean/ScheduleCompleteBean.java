package com.zylbaby.app.bean;

import java.io.File;
import java.util.List;

public class ScheduleCompleteBean {
	
	private int id;
	private String starttime;
	private String endtime;
	private String rank;
	private String content;
	private String babyid;
	private String piclist;
	private List<File> uploadfile;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBabyid() {
		return babyid;
	}
	public void setBabyid(String babyid) {
		this.babyid = babyid;
	}
	public String getPiclist() {
		return piclist;
	}
	public void setPiclist(String piclist) {
		this.piclist = piclist;
	}
	public List<File> getUploadfile() {
		return uploadfile;
	}
	public void setUploadfile(List<File> uploadfile) {
		this.uploadfile = uploadfile;
	}

}
