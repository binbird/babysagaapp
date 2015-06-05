package com.zylbaby.app.bean;

import java.util.ArrayList;

public class BabyDiaryByClass {
	private String BabyName;
	private String BabyPic;
	private ArrayList<DiaryDto> DiaryList;
	public String getBabyName() {
		return BabyName;
	}
	public void setBabyName(String babyName) {
		BabyName = babyName;
	}
	public String getBabyPic() {
		return BabyPic;
	}
	public void setBabyPic(String babyPic) {
		BabyPic = babyPic;
	}
	public ArrayList<DiaryDto> getDiaryList() {
		return DiaryList;
	}
	public void setDiaryList(ArrayList<DiaryDto> diaryList) {
		DiaryList = diaryList;
	}

}
