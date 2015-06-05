package com.zylbaby.app.bean;

import java.util.List;

public class DiaryDto {
	
	private String entrytime;
	private String diarytypename;
	private String diarytitle;
	private String diarycontext;
	private int diarytypeid;
	private int rank;
	private int diaryid;
	
	private int piccount;
	
	private List<String> piclist;
	
	
	public String getEntrytime() {
		return entrytime;
	}
	public void setEntrytime(String entrytime) {
		this.entrytime = entrytime;
	}
	public String getDiarytypename() {
		return diarytypename;
	}
	public void setDiarytypename(String diarytypename) {
		this.diarytypename = diarytypename;
	}
	public String getDiaryTitle() {
		return diarytitle;
	}
	public void setDiaryTitle(String diarytitle) {
		this.diarytitle = diarytitle;
	}
	public String getDiarycontext() {
		return diarycontext;
	}
	public void setDiarycontext(String diarycontext) {
		this.diarycontext = diarycontext;
	}
	public int getDiarytypeId() {
		return diarytypeid;
	}
	public void setDiarytypeId(int diarytypeid) {
		this.diarytypeid = diarytypeid;
	}
	public int getDiaryid() {
		return diaryid;
	}
	public void setDiaryid(int diaryid) {
		this.diaryid = diaryid;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getPiccount() {
		return piccount;
	}
	public void setPiccount(int piccount) {
		this.piccount = piccount;
	}
	public List<String> getPicList() {
		return piclist;
	}
	public void setPicList(List<String> piclist) {
		this.piclist = piclist;
	}

}