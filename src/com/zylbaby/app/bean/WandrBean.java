package com.zylbaby.app.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Wandr实体类
 * @author Micheal zhu (zhuyuliang0@126.com)
 * @version 1.0
 * @created 2014-3-11
 */
public class WandrBean{
	
	// 开始时间
	private String TimeBegin;
	// 结束时间
	private String TimeEnd;
	// 作息类型
	private String DiaryType;
	// 说明
	private String DiaryRemark;
	// 说明
	private String Rank;
	// Pics
	private String[] UploadPicStrings;
	// Files
	private List<File> UploadPicFiles = new ArrayList<File>();
	
	private String Important;
	private String Open;
	
	private String WandrId;
	private String ByUserId;
	private String CreatedTime;
	private String ToUserId;
	private String ByClassId;
    private String Flag;
    private String Title;
    private String EntryTime;
    private String Content;
    private String StartTime;
    private String EndTime;
    private String PicCount;
    private String DiaryTypeName;
    private String Guid;
    private String DiaryTypeImg;
    private String DiaryTypeId;
    private String ScheduleId;
    
	
	// 输入人
	private String ByUser;
	// 记录给谁
	private String ToUser;
	
	// 验证
	private String StatusCode;
	
	// 错误log
	private String Error;

	public String getTimeBegin() {
		return TimeBegin;
	}

	public void setTimeBegin(String timeBegin) {
		TimeBegin = timeBegin;
	}

	public String getTimeEnd() {
		return TimeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		TimeEnd = timeEnd;
	}

	public String getDiaryType() {
		return DiaryType;
	}

	public void setDiaryType(String diaryType) {
		DiaryType = diaryType;
	}

	public String getDiaryRemark() {
		return DiaryRemark;
	}

	public void setDiaryRemark(String diaryRemark) {
		DiaryRemark = diaryRemark;
	}

	public String[] getUploadPicStrings() {
		return UploadPicStrings;
	}

	public void setUploadPicStrings(String[] uploadPicStrings) {
		UploadPicStrings = uploadPicStrings;
	}


	public String getImportant() {
		return Important;
	}

	public void setImportant(String important) {
		Important = important;
	}

	public String getOpen() {
		return Open;
	}

	public void setOpen(String open) {
		Open = open;
	}

	public String getByUser() {
		return ByUser;
	}

	public void setByUser(String byUser) {
		ByUser = byUser;
	}

	public String getToUser() {
		return ToUser;
	}

	public void setToUser(String toUser) {
		ToUser = toUser;
	}

	public String getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}

	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	public List<File> getUploadPicFiles() {
		return UploadPicFiles;
	}

	public void setUploadPicFiles(List<File> uploadPicFiles) {
		UploadPicFiles = uploadPicFiles;
	}
	public String getRank() {
		return Rank;
	}
	public void setRank(String rank) {
		Rank = rank;
	}

	public String getWandrId() {
		return WandrId;
	}

	public void setWandrId(String wandrId) {
		WandrId = wandrId;
	}

	public String getByUserId() {
		return ByUserId;
	}

	public void setByUserId(String byUserId) {
		ByUserId = byUserId;
	}

	public String getCreatedTime() {
		return CreatedTime;
	}

	public void setCreatedTime(String createdTime) {
		CreatedTime = createdTime;
	}

	public String getToUserId() {
		return ToUserId;
	}

	public void setToUserId(String toUserId) {
		ToUserId = toUserId;
	}

	public String getByClassId() {
		return ByClassId;
	}

	public void setByClassId(String byClassId) {
		ByClassId = byClassId;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getEntryTime() {
		return EntryTime;
	}

	public void setEntryTime(String entryTime) {
		EntryTime = entryTime;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getPicCount() {
		return PicCount;
	}

	public void setPicCount(String picCount) {
		PicCount = picCount;
	}

	public String getDiaryTypeName() {
		return DiaryTypeName;
	}

	public void setDiaryTypeName(String diaryTypeName) {
		DiaryTypeName = diaryTypeName;
	}

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public String getDiaryTypeImg() {
		return DiaryTypeImg;
	}

	public void setDiaryTypeImg(String diaryTypeImg) {
		DiaryTypeImg = diaryTypeImg;
	}

	public String getDiaryTypeId() {
		return DiaryTypeId;
	}

	public void setDiaryTypeId(String diaryTypeId) {
		DiaryTypeId = diaryTypeId;
	}

	public String getScheduleId() {
		return ScheduleId;
	}

	public void setScheduleId(String scheduleId) {
		ScheduleId = scheduleId;
	}
	
}
